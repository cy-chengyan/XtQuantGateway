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

import os
import json
import hashlib
from pathlib import Path


class MiscUtil:
    """Miscellaneous utilities"""

    @staticmethod
    def getCurRootPath() -> str:
        """Get the root path of the current project"""
        curDir = str(Path('/common/util'))
        curPath = os.path.abspath(os.path.dirname(__file__))
        idx = curPath.find(curDir)
        if idx >= 0:
            return curPath[:idx]
        else:
            print('Warning: MiscUtil.py is not in the common/util directory')
            return curPath

    @staticmethod
    def obj2json(src):
        return json.dumps(src,
                          ensure_ascii=False,
                          separators=(',', ':'),
                          default=lambda o: o.__dict__ if hasattr(o, '__dict__') else str(o) if o is not None else None)

    @staticmethod
    def json2obj(src: str):
        return json.loads(src)

    @staticmethod
    def md5(src: bytes) -> str:
        md5 = hashlib.md5()
        md5.update(src)
        return md5.hexdigest()


if __name__ == '__main__':
    # path
    print(MiscUtil.getCurRootPath())

    # jsonSerialize
    d = {
        'k1': 'key1',
        'k2': 100,
        'k3': [1, 2, 3],
        'k4': {
            'k41': 'v41',
            'k42': 200
        },
        'k5': (1, 2, 3),
        'k6': b'bytes',
        'k7': None
    }
    print(MiscUtil.obj2json(d))
