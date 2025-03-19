import os
import logging
import requests

from XtQuantClient.misc import DateUtil
from XtQuantClient.model import Asset, Order, Position
# from misc import DateUtil
# from model import Asset, Order, Position


class XtQuantClient:
    """XtQuant API客户端"""

    def __init__(self, endpoint, timeout=10):
        """
        :param endpoint: JueQuant API的地址
        :param timeout: 超时时间, 单位:秒, 默认10秒
        """
        self.__endpoint = endpoint
        self.__timeout = timeout
        self.__headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        }

    def queryCurrentAsset(self, accountId: str) -> Asset or None:
        """
        获取当前资产概要信息
        :param accountId: 交易账号ID
        :return: 资产概要信息
        """
        data = {
            'reqId': self.__generateReqId(),
            'data': {
                'accountId': accountId
            }
        }

        try:
            responseData = self.__post('/v1/cash/current', data=data)
            summary = responseData['cash']
            if not summary:
                return None
        except Exception as e:
            logging.error(f"Query current asset error: {e}")
            return None

        return Asset.fromJson(summary)

    '''
    def querySpecialDateAsset(self, accountId: str, date: int) -> Asset or None:
        """
        获取指定日期的资产概要信息
        :param accountId: 交易账号ID
        :param date: 日期, 格式为yyyyMMdd
        :return: 指定日期的资产概要信息
        """
        data = {
            'reqId': self.__generateReqId(),
            'data': {
                'accountId': accountId,
                'date': date
            }
        }

        try:
            responseData = self.__post('/v1/cash/list', data=data)
            summaries = responseData['cashes']
            if not summaries:
                return None
        except Exception as e:
            logging.error(f"Query special date asset error: {e}")
            return None

        return Asset.fromJson(summaries[0])
    '''

    def queryCurrentPosition(self, accountId: str) -> list or None:
        """
        获取当前持仓信息
        :param accountId: 交易账号ID
        :return: 持仓信息列表. 如果发生异常, 返回None
        """
        data = {
            'reqId': self.__generateReqId(),
            'data': {
                'accountId': accountId
            }
        }

        try:
            responseData = self.__post('/v1/position/current', data=data)
            positions = responseData['positions']
            if not positions:
                return []
        except Exception as e:
            logging.error(f"Query current position error: {e}")
            return None

        return [Position.fromJson(p) for p in positions]

    '''
    def querySpecialDatePosition(self, accountId: str, date: int, symbol: str = None, pageSize: int = 5000, pageNum: int = 0) -> list or None:
        """
        获取指定日期的持仓信息
        :param accountId: 交易账号ID
        :param date: 日期, 格式为yyyyMMdd
        :param symbol: 标的代码, 例如: 'SZSE.000736'
        :param pageSize: 每页数量, 最大值 5000
        :param pageNum: 页码, 从0开始
        :return: 指定日期的持仓信息列表. 如果发生异常, 返回None
        """
        data = {
            'reqId': self.__generateReqId(),
            'data': {
                'accountId': accountId,
                'date': date,
                'symbol': symbol,
                'pageSize': pageSize,
                'pageNum': pageNum,
            }
        }

        try:
            responseData = self.__post('/v1/position/list', data=data)
            positions = responseData['positions']
            if not positions:
                return []
        except Exception as e:
            logging.error(f"Query special date position error: {e}")
            return None

        return [Position.fromJson(p) for p in positions]
    '''

    def queryCurrentOrder(self, accountId: str, statuses: list = None) -> list or None:
        """
        获取当前委托信息
        :param accountId: 交易账号ID
        :param statuses: 委托状态列表, 例如: []
        :return: 委托信息列表. 如果发生异常, 返回None
        """
        data = {
            'reqId': self.__generateReqId(),
            'data': {
                'accountId': accountId,
                'status': statuses,
            }
        }

        try:
            responseData = self.__post('/v1/order/current', data=data)
            orders = responseData['orders']
            if not orders:
                return []
        except Exception as e:
            logging.error(f"Query current order error: {e}")
            return None

        return [Order.fromJson(o) for o in orders]

    '''
    def querySpecialDateOrder(self, accountId: str, date: int, statuses: list = None, symbol: str = None, pageSize: int = 5000, pageNum: int = 0) -> list or None:
        """
        获取指定日期的委托信息
        :param accountId: 交易账号ID
        :param date: 日期, 格式为yyyyMMdd
        :param statuses: 委托状态列表, 例如: []
        :param symbol: 标的代码, 例如: 'SZSE.000736'
        :param pageSize: 每页数量, 最大值 5000
        :param pageNum: 页码, 从0开始
        :return: 指定日期的委托信息列表. 如果发生异常, 返回None
        """
        data = {
            'reqId': self.__generateReqId(),
            'data': {
                'accountId': accountId,
                'date': date,
                'status': statuses,
                'symbol': symbol,
                'pageSize': pageSize,
                'pageNum': pageNum,
            }
        }

        try:
            responseData = self.__post('/v1/order/list', data=data)
            orders = responseData['orders']
            if not orders:
                return []
        except Exception as e:
            logging.error(f"Query special date order error: {e}")
            return None

        return [Order.fromJson(o) for o in orders]
    '''

    def __generateReqId(self) -> str:
        return DateUtil.nowString('PYLIB-%y%m%d%H%M%S%f') + '-' + os.urandom(3).hex()

    def __parseResponseData(self, response):
        logging.info(f"Response: {response.status_code}, {response.text}")
        # print(f"Response: {response.status_code}, {response.text}")
        # print(response.content)
        j = response.json()
        if j['code'] == 0:
            return j['data']
        else:
            logging.info(f"Response: {response.status_code}, {response.text}")
            raise ValueError(j['msg'])

    def __post(self, path, params: dict = None, data: dict = None):
        url = self.__endpoint + path
        logging.info(f"POST:{url}, params:{params}, data:{data}")
        response = requests.post(url, headers=self.__headers, params=params, json=data, timeout=self.__timeout)
        return self.__parseResponseData(response)


if __name__ == '__main__':

    accountId = '55004763'
    date = 20250211

    client = XtQuantClient('http://172.23.171.157:1091/api')

    print(f'accountId: {accountId}')
    print('\ncurrent asset ------------------')
    asset = client.queryCurrentAsset(accountId)
    print(asset.__dict__)

    print('\ncurrent position ------------------')
    positions = client.queryCurrentPosition(accountId)
    for p in positions:
        print(p.__dict__)
    print('\ncurrent order ------------------')
    orders = client.queryCurrentOrder(accountId)
    for o in orders:
        print(o.__dict__)

    '''
    print(f'\n{date} asset ------------------')
    asset = client.querySpecialDateAsset(accountId, date)
    print(asset.__dict__)
    print(f'\n{date} position ------------------')
    # positions = client.querySpecialDatePosition(accountId, date, symbol='SZSE.000736')
    positions = client.querySpecialDatePosition(accountId, date)
    for p in positions:
        print(p.__dict__)
    print(f'\n{date} order ------------------')
    orders = client.querySpecialDateOrder(accountId, date)
    for o in orders:
        print(o.__dict__)
    '''
