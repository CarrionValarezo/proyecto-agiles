import MySQLdb.cursors
import pymysql


class Conexion:

    def __init__(self):
        self.connection = pymysql.connect(
            host='localhost',
            port=3308,
            user='rmcv',
            password='',
            db='agiles_pruebas'
        )

    def get_cursor(self):
        return self.connection.cursor(pymysql.cursors.DictCursor)


if __name__ == "__main__":
    con = Conexion()