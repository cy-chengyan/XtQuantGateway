
import redis


class RedisConn:

    def __init__(self, host: str, port: int, pwd: str, db: int):
        self.__pool = redis.ConnectionPool(host=host, port=port, password=pwd, db=db, encoding='utf-8', decode_responses=True)
        self.__redis = redis.Redis(connection_pool=self.__pool)

    def __del__(self):
        self.__redis.close()

    def getRedis(self) -> redis.Redis:
        return self.__redis
