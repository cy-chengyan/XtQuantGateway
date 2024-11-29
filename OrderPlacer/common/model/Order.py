

class Order:

    ORDER_STATUS_LOCAL_SAVED = -1  # 保存在本地
    ORDER_STATUS_LOCAL_SUBMIT_FAILED = -2  # 本地向QMT下单提交失败
    ORDER_STATUS_LOCAL_SUBMITTING = -3  # 本地正在向QMT下单中
    ORDER_STATUS_LOCAL_SUBMIT_CANCELING = -4  # 本地正在向QMT提交撤单中
    ORDER_STATUS_CANCELED = 54  # 已撤
    ORDER_STATUS_SUCCEEDED = 56 # 已成
    ORDER_STATUS_JUNK = 57 # 废单

    def __init__(self):
        self.id = None
        self.orderRemark = None
        self.accountId = None
        self.date = None
        self.stockCode = None
        self.orderId = None
        self.orderSysId = None
        self.orderTime = None
        self.orderType = None
        self.orderVolume = None
        self.priceType = None
        self.price = None
        self.tradedVolume = None
        self.tradedPrice = None
        self.orderStatus = None
        self.canceledVolume = None
        self.strategyName = None
        self.errorMsg = None
        self.createdAt = None
        self.updatedAt = None
        self.manualUpdatedAt = None
        self.memo = None
