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

from configparser import RawConfigParser


class Configure:
    """Configure wrapper class"""

    def __init__(self, confPath: str):
        self.__cfg = RawConfigParser()
        self.__cfg.read(confPath, encoding='utf8')

    def getConfValue(self, section, item, default=None):
        """
        Get configuration item
        :param section: configuration section
        :param item: configuration item
        :param default: default value
        :return: Return the value of the configuration item if found; return the default value if not found
        """
        v = self.__cfg.get(section, item)
        if v is None:
            v = default
        return v
