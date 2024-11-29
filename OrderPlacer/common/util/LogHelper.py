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

import logging
from logging.handlers import TimedRotatingFileHandler


class LogHelper:
    """Log helper class"""

    LEVEL_DEBUG = logging.DEBUG
    LEVEL_INFO = logging.INFO
    LEVEL_WARNING = logging.WARNING
    LEVEL_ERROR = logging.ERROR
    LEVEL_CRITICAL = logging.CRITICAL

    LEVELS = {
        'DEBUG': LEVEL_DEBUG,
        'INFO': LEVEL_INFO,
        'WARNING': LEVEL_WARNING,
        'ERROR': LEVEL_ERROR,
        'CRITICAL': LEVEL_CRITICAL
    }

    @staticmethod
    def initLog(path: str, level=logging.INFO):
        if type(level) is str:
            level = LogHelper.LEVELS[level] if level in LogHelper.LEVELS else LogHelper.LEVEL_INFO

        rotatingHandler = TimedRotatingFileHandler(filename=path, when='D', encoding="utf-8")
        logging.basicConfig(format='[%(asctime)s.%(msecs)03d][%(module)s.%(funcName)s:%(lineno)d][%(levelname)s] %(message)s', datefmt='%Y-%m-%d %H:%M:%S',
                            level=level,
                            handlers=[rotatingHandler])


if __name__ == '__main__':
    from common.util.MiscUtil import MiscUtil
    logFilePath = MiscUtil.getCurRootPath() + '/log/loghelper-test.log'
    LogHelper.initLog(logFilePath, LogHelper.LEVEL_DEBUG)
    logging.debug('debug')
    logging.info('info')
    logging.warning('warning')
    logging.error('error')
    logging.critical('critical')
