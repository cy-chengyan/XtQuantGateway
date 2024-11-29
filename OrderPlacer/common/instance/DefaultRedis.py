
from common.util import RedisConn
from .DefaultConfigure import defaultConfigure

__section = 'redis-default'
__host = defaultConfigure.getConfValue(__section, 'host')
__port = int(defaultConfigure.getConfValue(__section, 'port'))
__pwd = defaultConfigure.getConfValue(__section, 'pwd')
__db = defaultConfigure.getConfValue(__section, 'db')
defaultRedis = RedisConn(__host, __port, __pwd, __db)
