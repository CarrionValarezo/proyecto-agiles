from flask import Flask
from flask_cors import CORS
from .config import LocalConfig
from .data_base_connection import DataBaseConnection

# Creacion de la app Flask con la base de datos
app = Flask(__name__)
db = DataBaseConnection(app)
CORS(app)


def create_app():

    # Configuracion de la app
    app.config['CORS_HEADERS'] = 'Content-Type'

    # Devuelve JSON de manera mas legible
    app.config['JSONIFY_PRETTYPRINT_REGULAR'] = True

    # No ordena los JSON
    app.config['JSON_SORT_KEYS'] = False

    # Configuracion de la app mediante un objeto
    app.config.from_object(LocalConfig)

    # Registra blueprint
    from .procesos.url import procesos_blueprint
    app.register_blueprint(procesos_blueprint)

    return app
