from flask import Config


class LocalConfig(Config):
    MYSQL_HOST = "localhost"
    MYSQL_USER = "rmcv"
    MYSQL_PASSWORD = ""
    MYSQL_PORT = 3308
    MYSQL_DB = "agiles"
