from flask import Flask
from flask_cors import CORS
from .config import LocalConfig
from .data_base_connection import DataBaseConnection

app = Flask(__name__)
db = DataBaseConnection(app)
CORS(app)


def create_app():
    app.config['CORS_HEADERS'] = 'Content-Type'
    app.config['JSONIFY_PRETTYPRINT_REGULAR'] = True
    app.config['JSON_SORT_KEYS'] = False
    app.config.from_object(LocalConfig)

    from .usuarios_activos.url import usac
    app.register_blueprint(usac)

    return app
