from flask import Blueprint, jsonify
from . import aplicacion

usac = Blueprint("usac", __name__)


@usac.route('/usuario-cant-activos')
def get_usuarios_cant_activos():
    respuesta = []
    usuarios_cant_activos = aplicacion.get_usuarios_cant_activos()
    for usuario, cant_activos in usuarios_cant_activos:
        respuesta.append({
            "cedula_usuario": usuario.get_cedula(),
            "nombre_usuario": usuario.get_nombre(),
            "apellido_usuario": usuario.get_apellido(),
            "cantidad_activos_usuario": cant_activos
        })
    return jsonify(respuesta)


@usac.route('/')
def test():
    return "Si funciona!"
