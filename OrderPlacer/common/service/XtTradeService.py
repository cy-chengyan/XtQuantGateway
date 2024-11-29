#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

import time
import logging
from xtquant.xttrader import XtQuantTrader
from xtquant.xttype import StockAccount
from xtquant import xtconstant
from common.util import Configure
from common.exception import ParamException, ProcessException


# Trade service class
class XtTradeService:

    # Price type set
    PRICE_TYPE_SET = {
        xtconstant.LATEST_PRICE,
        xtconstant.FIX_PRICE,
        xtconstant.MARKET_SH_CONVERT_5_CANCEL,
        xtconstant.MARKET_SH_CONVERT_5_LIMIT,
        xtconstant.MARKET_PEER_PRICE_FIRST,
        xtconstant.MARKET_MINE_PRICE_FIRST,
        xtconstant.MARKET_SZ_INSTBUSI_RESTCANCEL,
        xtconstant.MARKET_SZ_CONVERT_5_CANCEL,
        xtconstant.MARKET_SZ_FULL_OR_CANCEL
    }

    # Constants
    QMT_API_OK = 0

    def __init__(self, conf: Configure):
        confSection = 'qmt'
        self.__qmtPath = conf.getConfValue(confSection, 'path') + '\\userdata_mini'

        self.accountId = conf.getConfValue(confSection, 'accountId')
        self.accountType = conf.getConfValue(confSection, 'accountType')
        self.__account = StockAccount(self.accountId, self.accountType)

        if not self.__connect():
            raise ProcessException('连接QMT失败')

    def __del__(self):
        self.__disconnect()

    def __connect(self) -> bool:
        logging.info('连接QMT...')
        self.__sessionId = int(time.time())
        self.__xtTrader = XtQuantTrader(self.__qmtPath, self.__sessionId)
        self.__xtTrader.start()
        r = self.__xtTrader.connect()
        if r != XtTradeService.QMT_API_OK:
            logging.error(r'连接QMT失败(错误码:%d)' % r)
            return False
        else:
            logging.info('连接QMT成功')
            return True

    def __disconnect(self):
        logging.info('断开QMT...')
        try:
            if self.__xtTrader:
                self.__xtTrader.stop()
                logging.info('断开QMT成功')
        except Exception as e:
            logging.info('断开QMT出错: %s' % repr(e))
        finally:
            self.__xtTrader = None

    def placeOrder(self, accountId: str, stockCode: str, orderType: int, orderVolume: int,
                   priceType: int, price: float or str or None,
                   strategyName: str, orderRemark: str):
        if accountId != self.accountId:
            raise ParamException('账户ID无效')
        '''
        # check the parameters
        stockCode = stockCode.strip() if type(stockCode) is str else None
        if not stockCode or len(stockCode) != 9:
            raise ParamException('股票代码无效')

        if type(orderType) is not int or orderType not in xtconstant.ORDER_TYPE_SET:
            raise ParamException('定单类型无效')

        if type(orderVolume) is not int or orderVolume <= 0:
            raise ParamException('定单股数无效')

        if type(priceType) is not int or priceType not in XtTradeService.PRICE_TYPE_SET:
            raise ParamException('价格类型无效')
        '''

        # may be a string or None
        orderPrice = None
        if price is not None:
            try:
                orderPrice = float(price)
            except Exception as e:
                raise ParamException(f'定单价格无效:{repr(e)}')
        if priceType != xtconstant.LATEST_PRICE:
            if orderPrice is None or orderPrice <= 0.0:
                raise ParamException('定单价格无效')
        else:
            orderPrice = 0.0

        '''
        if type(strategyName) is str:
            strategyName = strategyName.strip()
            strategyName = strategyName if strategyName else None
        elif strategyName is not None:
            raise ParamException('策略名称无效')

        if type(orderRemark) is str:
            orderRemark = orderRemark.strip()
        if type(orderRemark) is not str or not orderRemark:
            raise ParamException('定单备注(实为客户端定单ID)无效, 定单备注为字符串类型, 并且不能为空')
        isExist = self.__orderRepo.isExistByRemark(orderRemark)
        if isExist:
            raise ParamException('定单已存在')
        '''
        # submit the order to the QMT
        try:
            if not strategyName:  # must be a string, not None
                strategyName = ''
            return {
                'orderId': self.__xtTrader.order_stock(account=self.__account,
                                                       stock_code=stockCode,
                                                       order_type=orderType,
                                                       order_volume=orderVolume,
                                                       price_type=priceType,
                                                       price=orderPrice,
                                                       strategy_name=strategyName,
                                                       order_remark=orderRemark)
            }
        except Exception as e:
            logging.error(f'提交定单失败:{orderRemark}, {repr(e)}')
            raise ProcessException('提交定单到QMT终端失败')

    def cancelOrder(self, orderId: int) -> bool:
        r = self.__xtTrader.cancel_order_stock(self.__account, orderId)
        return True if r == XtTradeService.QMT_API_OK else False
