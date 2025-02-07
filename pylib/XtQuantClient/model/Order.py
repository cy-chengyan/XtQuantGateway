
from decimal import Decimal
from XtQuantClient.enums import OrderBusiness, OrderPrice, OrderStatus
from XtQuantClient.misc import decXtQuantPricePrecision
# from enums import OrderBusiness, OrderPrice, OrderStatus
# from misc import decXtQuantPricePrecision

class Order:

    def __init__(self, id, orderRemark, accountId, date, stockCode, orderId, orderSysId, orderTime, orderType, orderVolume, priceType, price, tradedVolume, tradedPrice, orderStatus, canceledVolume, strategyName, errorMsg, createdAt, updatedAt, manualUpdatedAt, memo):
        self.id = id
        """交易网关中的数据表的主键ID, 无实际意义"""
        self.orderRemark = orderRemark
        """定单备注(实为下游调用者设定的定单id, 具有唯一性)"""
        self.accountId = accountId
        """账号ID"""
        self.date = date
        """日期, format: YYYYMMDD"""
        self.stockCode = stockCode
        """股票代码"""
        self.orderId = orderId
        """订单编号"""
        self.orderSysId = orderSysId
        """委托编号(柜台合同编号)"""
        self.orderTime = orderTime
        """报单时间, format: HHMMSS"""
        self.orderType = OrderBusiness(orderType)
        """定单类型"""
        self.orderVolume = orderVolume
        """定单数量"""
        self.priceType = OrderPrice(priceType)
        """定单价格类型"""
        self.price = Decimal(price).quantize(decXtQuantPricePrecision)
        """定单价格"""
        self.tradedVolume = tradedVolume
        """成交数量"""
        self.tradedPrice =  Decimal(tradedPrice).quantize(decXtQuantPricePrecision)
        """成交均价"""
        self.orderStatus = OrderStatus(orderStatus)
        """定单状态"""
        self.canceledVolume = canceledVolume
        """已撤单数量"""
        self.strategyName = strategyName
        """策略名称"""
        self.errorMsg = errorMsg
        """错误信息(废单原因)"""
        self.createdAt = createdAt
        """记录创建时间戳(秒)"""
        self.updatedAt = updatedAt
        """记录更新时间戳(秒)"""
        self.manualUpdatedAt = manualUpdatedAt
        """手动更新时间(毫秒)"""
        self.memo = memo
        """备注"""

    @staticmethod
    def fromJson(data):
        return Order(
            data['id'],
            data['orderRemark'],
            data['accountId'],
            data['date'],
            data['stockCode'],
            data['orderId'],
            data['orderSysId'],
            data['orderTime'],
            data['orderType'],
            data['orderVolume'],
            data['priceType'],
            data['price'],
            data['tradedVolume'],
            data['tradedPrice'],
            data['orderStatus'],
            data['canceledVolume'],
            data['strategyName'],
            data['errorMsg'],
            data['createdAt'],
            data['updatedAt'],
            data['manualUpdatedAt'],
            data['memo'],
        )