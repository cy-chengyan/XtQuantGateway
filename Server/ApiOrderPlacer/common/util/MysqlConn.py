
import pymysql

from common.util.Configure import Configure


class MysqlConn:
    """Mysql connection class, attention: not thread safe!"""

    @staticmethod
    def new(conf: Configure, confSection: str, isAutoPing=True):
        """
        Create a new MySQL connection object
        :param conf: Configure object
        :param confSection: Section name of the connection configuration
        :param isAutoPing: Whether to automatically ping the connection
        :return:
        """
        ret = MysqlConn(isAutoPing=isAutoPing)
        ret.createConn(conf, confSection)
        return ret

    def __init__(self, isAutoPing=True):
        self._conf = None
        self._confSection = None
        self._mysqlConn = None
        self._isAutoPing = isAutoPing

    def __del__(self):
        self.closeConn()

    def __createMysqlConn(self, conf: Configure, confSection: str, autoCommit=True):
        host = conf.getConfValue(confSection, 'host')
        port = int(conf.getConfValue(confSection, 'port'))
        user = conf.getConfValue(confSection, 'user')
        pwd = conf.getConfValue(confSection, 'pwd')
        db = conf.getConfValue(confSection, 'db')
        return pymysql.connect(host=host,
                               port=port,
                               user=user,
                               password=pwd,
                               db=db,
                               charset='utf8mb4',
                               autocommit=autoCommit)

    def createConn(self, conf: Configure, confSection: str):
        self.closeConn()
        self._conf = conf
        self._confSection = confSection
        self._mysqlConn = self.__createMysqlConn(self._conf, self._confSection)

    def closeConn(self):
        if self._mysqlConn:
            self._mysqlConn.close()
            self._mysqlConn = None

    def insert(self, tableName, data, isGetLastInsertId: bool = False):
        if self._isAutoPing:
            self._mysqlConn.ping()
        columns = data.keys()
        prefix = ''.join(['INSERT INTO `', tableName, '`'])
        fields = ','.join([''.join(['`', column, '`']) for column in columns])
        values = ','.join(["%s" for _ in range(len(columns))])
        sql = ''.join([prefix, "(", fields, ") VALUES (", values, ")"])
        # print(sql)
        params = [data[key] for key in columns]
        cur = self._mysqlConn.cursor()
        cur.execute(sql, tuple(params))
        ret = None
        if isGetLastInsertId:
            ret = cur.lastrowid
        cur.close()
        return ret

    def insertMany(self, tableName, data, isGetLastInsertId: bool = False):
        if self._isAutoPing:
            self._mysqlConn.ping()
        columns = data[0].keys()
        prefix = ''.join(['INSERT INTO `', tableName, '`'])
        fields = ','.join([''.join(['`', column, '`']) for column in columns])
        values = ','.join(["%s" for _ in range(len(columns))])
        sql = ''.join([prefix, "(", fields, ") VALUES (", values, ")"])
        # print(sql)
        params = []
        for d in data:
            params.append([d[key] for key in columns])
        cur = self._mysqlConn.cursor()
        cur.executemany(sql, params)
        ret = None
        if isGetLastInsertId:
            ret = cur.lastrowid
        cur.close()
        return ret

    def replace(self, tableName, data, isGetLastInsertId: bool = False):
        if self._isAutoPing:
            self._mysqlConn.ping()
        columns = data.keys()
        prefix = ''.join(['REPLACE INTO `', tableName, '`'])
        fields = ','.join([''.join(['`', column, '`']) for column in columns])
        values = ','.join(["%s" for _ in range(len(columns))])
        sql = ''.join([prefix, "(", fields, ") VALUES (", values, ")"])
        # print(sql)
        params = [data[key] for key in columns]
        cur = self._mysqlConn.cursor()
        cur.execute(sql, tuple(params))
        ret = None
        if isGetLastInsertId:
            ret = cur.lastrowid
        cur.close()
        return ret

    def replaceMany(self, tableName, data, isGetLastInsertId: bool = False):
        if self._isAutoPing:
            self._mysqlConn.ping()
        columns = data[0].keys()
        prefix = ''.join(['REPLACE INTO `', tableName, '`'])
        fields = ','.join([''.join(['`', column, '`']) for column in columns])
        values = ','.join(["%s" for _ in range(len(columns))])
        sql = ''.join([prefix, "(", fields, ") VALUES (", values, ")"])
        # print(sql)
        params = []
        for d in data:
            params.append([d[key] for key in columns])
        cur = self._mysqlConn.cursor()
        cur.executemany(sql, params)
        ret = None
        if isGetLastInsertId:
            ret = cur.lastrowid
        cur.close()
        return ret

    def delete(self, tableName: str, where: str):
        if self._isAutoPing:
            self._mysqlConn.ping()
        sql = "DELETE FROM `%s`" % tableName
        sql += " WHERE %s" % where
        # print(sql)
        cur = self._mysqlConn.cursor()
        cur.execute(sql)
        cur.close()

    def update(self, table, data, where=None):
        if self._isAutoPing:
            self._mysqlConn.ping()
        query = ''
        for k, v in data.items():
            if len(query) > 0:
                query += ','
            if v is None:
                v = 'NULL'
            elif type(v) == str:
                v = v.replace('\\', '\\\\')
                v = '\'' + v.replace('\'', '\'\'') + '\''
            else:
                v = str(v)
            query += '`' + k + '`= ' + v
        sql = "UPDATE `%s` SET %s" % (table, query)
        if where and len(where) > 0:
            sql += " WHERE %s" % where
        # print(sql)
        cur = self._mysqlConn.cursor()
        cur.execute(sql)
        cur.close()

    def execute(self, sql):
        if self._isAutoPing:
            self._mysqlConn.ping()
        cur = self._mysqlConn.cursor()
        cur.execute(sql)
        cur.close()

    def executeMany(self, sql, data):
        if self._isAutoPing:
            self._mysqlConn.ping()
        cur = self._mysqlConn.cursor()
        cur.executemany(sql, data)
        cur.close()

    def queryAll(self, sql):
        # print(sql)
        if self._isAutoPing:
            self._mysqlConn.ping()
        cur = self._mysqlConn.cursor()
        cur.execute(sql)
        result = cur.fetchall()
        cur.close()
        return result

    def queryOne(self, sql):
        # print(sql)
        if self._isAutoPing:
            self._mysqlConn.ping()
        cur = self._mysqlConn.cursor()
        cur.execute(sql)
        result = cur.fetchone()
        cur.close()
        return result
