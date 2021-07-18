from flask import Blueprint, jsonify
from . import aplicacion

usac = Blueprint("usac", __name__)


@usac.route('/usuario-activo')
def get_usuarios_activos():
    respuesta = []
    lista_usuarios = aplicacion.get_usuarios_activos()
    for usuario in lista_usuarios:
        respuesta.append({
            "cedula_usuario": usuario.get_cedula(),
            "nombre_usuario": usuario.get_nombre(),
            "apellido_usuario": usuario.get_apellido(),
            "activos_usuario": [{
                "id_activo": activo.get_id(),
                "nombre_activo": activo.get_nombre(),
                "descripcion_activo": activo.get_descripcion()
            } for activo in usuario.get_activos()]
        })
    return jsonify(respuesta)


@usac.route('/')
def test():
    return "Si funciona!"
