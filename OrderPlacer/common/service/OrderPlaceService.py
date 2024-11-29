import json
import logging
from types import SimpleNamespace

from common.instance import defaultConfigure, defaultRedis
from common.model import Order
from common.repo import OrderRepo
from common.util import DateUtil
from .XtTradeService import XtTradeService


class OrderPlaceService:

    TASK_TYPE_PLACE_ORDER = 1
    TASK_TYPE_CANCEL_ORDER = 2

    def __init__(self):
        self.__taskQueueName = defaultConfigure.getConfValue('redis-default', 'taskQueue')
        self.__redis = defaultRedis.getRedis()
        self.__xtTradeService = XtTradeService(defaultConfigure)
        self.__orderRepo = OrderRepo()

    def placeOrder(self, o):
        logging.info(f'===开始处理下单任务:{o}===')
        orderRemark = o.orderRemark
        order = self.__orderRepo.queryOrderByOrderRemark(orderRemark)
        if order is None:
            logging.error(f'找不到定单({orderRemark})')
            return

        if order.orderStatus != Order.ORDER_STATUS_LOCAL_SAVED:
            logging.error(f'当前定单({orderRemark})状态为:{order.orderStatus}, 不能下单')
            return

        logging.info(f'开始下单({orderRemark}), 将定单状态更新为:本地提交中')
        try:
            self.__orderRepo.updateOrderStatus(orderRemark, Order.ORDER_STATUS_LOCAL_SUBMITTING)
        except Exception as e:
            logging.error(f'更新定单({orderRemark})状态出错:{repr(e)}')
            return

        orderId = None
        try:
            logging.info(f'将定单(orderRemark)提交到QMT')
            r = self.__xtTradeService.placeOrder(accountId=order.accountId, stockCode=order.stockCode, orderType=order.orderType,
                                                orderVolume=order.orderVolume, priceType=order.priceType, price=order.price,
                                                strategyName=order.strategyName, orderRemark=order.orderRemark)
            orderId = r.get('orderId')
            logging.info(f'下单({orderRemark})成功, QMT返回的订单ID为:{orderId}')
        except Exception as e:
            logging.error(f'下单({orderRemark})出错:{repr(e)}')

        if orderId is None or orderId < 0:
            logging.error(f'下单({orderRemark})失败,QMT返回的订单ID为:{orderId}')
            logging.info(f'将定单({orderRemark})状态更新为:本地提交失败')
            try:
                self.__orderRepo.updateOrderStatus(orderRemark, Order.ORDER_STATUS_LOCAL_SUBMIT_FAILED)
            except Exception as e:
                logging.error(f'[严重错误]更新({orderRemark}定单状态出错:{repr(e)}')
            return

        try:
            self.__orderRepo.updateOrderId(orderRemark, orderId)
        except Exception as e:
            logging.error(f'更新定单({orderRemark})订单ID(orderId)为({orderId})出错:{repr(e)}')
            return

    def cancelOrder(self, o):
        logging.info(f'===开始处理下单任务:{o}===')
        orderRemark = o.orderRemark
        order = self.__orderRepo.queryOrderByOrderRemark(orderRemark)
        if order is None:
            logging.error(f'找不到定单({orderRemark})')
            return

        if order.orderStatus == Order.ORDER_STATUS_SUCCEEDED:
            logging.error(f'当前定单({orderRemark})状态为已成, 不能撤单')
            return

        if order.orderStatus == Order.ORDER_STATUS_CANCELED:
            logging.error(f'当前定单({orderRemark})状态为已撤, 不能再次撤单')
            return

        if order.orderStatus == Order.ORDER_STATUS_JUNK:
            logging.error(f'当前定单({orderRemark})状态为废单, 不能撤单')
            return

        if order.orderStatus == Order.ORDER_STATUS_LOCAL_SUBMIT_CANCELING:
            logging.error(f'当前定单({orderRemark})状态为本地正在向QMT提交撤单中, 不能再次撤单')
            return

        if not order.orderId:
            logging.error(f'当前定单({orderRemark})没有委托编号(orderId字段), 不能撤单')
            return

        if order.orderStatus == Order.ORDER_STATUS_LOCAL_SAVED or order.orderStatus == Order.ORDER_STATUS_LOCAL_SUBMIT_FAILED:
            logging.error(f'当前定单({orderRemark})状态{order.orderStatus}, 可本地直接更新(-1:保存在本地 或 -2:本地向QMT下单提交失败), 直接更新为已撤单')
            try:
                self.__orderRepo.updateOrderStatus(orderRemark, Order.ORDER_STATUS_CANCELED)
                logging.info(f'更新定单({orderRemark})状态为已撤单')
            except Exception as e:
                logging.error(f'更新定单({orderRemark})状态出错:{repr(e)}')
            return

        try:
            logging.info(f'将定单({orderRemark})状态更新为:本地提交撤单中')
            self.__orderRepo.updateOrderStatus(orderRemark, Order.ORDER_STATUS_LOCAL_SUBMIT_CANCELING)
        except Exception as e:
            logging.error(f'更新定单({orderRemark})状态更新为(-4:本地正在向QMT提交撤单中)时出错:{repr(e)}')
            return

        try:
            logging.info(f'将撤单(orderRemark)提交到QMT')
            r = self.__xtTradeService.cancelOrder(orderId=order.orderId)
            if r:
                logging.error(f'提交撤单({orderRemark})成功')
            else:
                logging.error(f'提交撤单({orderRemark})到QMT失败')
        except Exception as e:
            logging.info(f'提交撤单({orderRemark})到QMT失败, {repr(e)}')


    def run(self):
        while DateUtil.curHmsInt(zoneStr='Asia/Shanghai') < 150100:
            raw = self.__redis.brpop(self.__taskQueueName, timeout=1)
            if raw is None:
                continue

            s = str(raw[1])
            logging.info(f'从消息队列中取出了任务消息:{s}')
            fields = s.split('|')
            if len(fields) != 2:
                logging.error(f'[无效的任务消息]:{s}')
                continue
            taskType = int(fields[0])
            order = json.loads(fields[1], object_hook=lambda d: SimpleNamespace(**d))
            if taskType == OrderPlaceService.TASK_TYPE_PLACE_ORDER:
                self.placeOrder(order)
            else:
                self.cancelOrder(order)


if __name__ == '__main__':
    OrderPlaceService().run()
