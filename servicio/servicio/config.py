import os
from flask import Config
import urllib.parse as urlparse


class LocalConfig(Config):
    MYSQL_HOST = "localhost"
    MYSQL_USER = "rmcv"
    MYSQL_PASSWORD = ""
    MYSQL_PORT= 3308
    MYSQL_DB = "agiles"
