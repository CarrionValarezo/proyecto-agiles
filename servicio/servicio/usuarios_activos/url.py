from flask import Blueprint, jsonify, request
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

@usac.route('/activos-usuario/<cedula>')
def get_activos_por_usuario(cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    respuesta = []
    activos_usuario = aplicacion.get_activos_por_usuario(usuario)
    for activo in activos_usuario:
        detalle_activo = activo.get_activo()
        respuesta.append({
            "id_pertenencia": activo.get_id_pertenencia(),
            "id_activo": detalle_activo.get_id(),
            "nombre_activo": detalle_activo.get_nombre(),
            "descripcion_activo": detalle_activo.get_descripcion()
        })
    return jsonify(respuesta)

@usac.route('/proceso', methods=['POST'])
def crear_proceso():
    data = request.get_json()
    proceso = data.get("proceso")
    cedulas_usuarios = data.get("usuarios_proceso")
    usuarios = [aplicacion.get_usuario_por_cedula(usuario.get("cedula_usuario")) for usuario in cedulas_usuarios]
    aplicacion.crear_proceso(proceso, usuarios)
    return jsonify({"message":"Proceso Creado"})

@usac.route('/')
def test():
    return "Si funciona!"
