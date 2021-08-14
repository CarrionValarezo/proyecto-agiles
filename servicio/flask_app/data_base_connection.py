from flask_mysqldb import MySQL
import MySQLdb


class DataBaseConnection(MySQL):
    def __init__(self, app):
        self.__mysql = MySQL(app)

    def get_connection(self):
        return self.__mysql

    def get_cursor(self):
        return self.__mysql.connection.cursor(MySQLdb.cursors.DictCursor)

    def get_cursor_no_dict(self):
        return self.__mysql.connection.cursor()
