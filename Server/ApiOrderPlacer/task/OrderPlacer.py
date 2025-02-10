
import logging

from common.util import MiscUtil, LogHelper
from common.service import OrderPlaceService


if __name__ == '__main__':

    logPath = MiscUtil.getCurRootPath() + '/log/OrderPlacer.log'
    LogHelper.initLog(path=logPath, level=logging.INFO)

    service = OrderPlaceService()
    service.run()
