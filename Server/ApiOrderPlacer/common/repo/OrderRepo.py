
from common.instance import defaultConfigure
from common.util import MysqlConn
from common.model import Order

class OrderRepo:

    def __init__(self, isAutoPing: bool = True):
        self.__table = 't_order'
        self.__mysqlConn = MysqlConn.new(defaultConfigure, 'mysql-xtquant', isAutoPing=isAutoPing)

    def queryOrderByOrderRemark(self, orderRemark: str):
        sql = f"select `id`, order_remark, account_id, `date`, stock_code, order_id, order_sys_id, order_time, order_type, order_volume, price_type, price, traded_volume, traded_price, order_status, canceled_volume, strategy_name, error_msg, created_at, updated_at, manual_updated_at, memo from {self.__table} where order_remark='{orderRemark}'"
        row = self.__mysqlConn.queryOne(sql)
        if row is None:
            return None
        return self.__parseOrder(row)

    def updateOrderStatus(self, orderRemark: str, orderStatus: int):
        sql = f"update {self.__table} set order_status={orderStatus} where order_remark='{orderRemark}'"
        self.__mysqlConn.execute(sql)

    def updateOrderId(self, orderRemark: str, orderId: int):
        sql = f"update {self.__table} set order_id={orderId} where order_remark='{orderRemark}'"
        self.__mysqlConn.execute(sql)

    def __parseOrder(self, row):
        order = Order()
        order.id = row[0]
        order.orderRemark = row[1]
        order.accountId = row[2]
        order.date = row[3]
        order.stockCode = row[4]
        order.orderId = row[5]
        order.orderSysId = row[6]
        order.orderTime = row[7]
        order.orderType = row[8]
        order.orderVolume = row[9]
        order.priceType = row[10]
        order.price = row[11]
        order.tradedVolume = row[12]
        order.tradedPrice = row[13]
        order.orderStatus = row[14]
        order.canceledVolume = row[15]
        order.strategyName = row[16]
        order.errorMsg = row[17]
        order.createdAt = row[18]
        order.updatedAt = row[19]
        order.manualUpdatedAt = row[20]
        order.memo = row[21]
        return order


if __name__ == '__main__':
    repo = OrderRepo()
    order = repo.queryOrderByOrderRemark('TEST-20241129-00011')
    print(order)
