from flask import Blueprint, jsonify
from . import app

usac = Blueprint("usac", __name__)

@usac.route('/usuario-activo')
def get_usuarios_activos():
    response = app.get_usuarios_activos()
    return jsonify(response)

@usac.route('/usuario-cantidad-activos')
def get_usuarios_cantidad_activos():
    response = app.get_usuario_cantidad_activo()
    return jsonify(response)

@usac.route('/test')
def test():
    return "Si funciona!"