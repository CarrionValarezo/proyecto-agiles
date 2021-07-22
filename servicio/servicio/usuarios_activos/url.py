from flask import Blueprint, jsonify, request
from . import aplicacion

usac = Blueprint("usac", __name__)


@usac.route('/usuarios/cantidad-activos')
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


@usac.route('/usuarios/<cedula>/activos')
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


@usac.route('/usuarios/<cedula>/procesos')
def get_procesos_por_usuario(cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    procesos = aplicacion.get_procesos_por_usuario(usuario)
    respuesta = []
    for proceso in procesos:
        respuesta.append({
            "id_proceso": proceso.get_id(),
            "nombre_proceso": proceso.get_nombre(),
            "fecha_creacion_proceso": proceso.get_fecha(),
            "estado_proceso": proceso.get_estado()
        })
    return jsonify(respuesta)


@usac.route('/procesos')
def get_procesos():
    procesos = aplicacion.get_procesos()
    respuesta = []
    for proceso in procesos:
        respuesta.append({
            "id_proceso": proceso.get_id(),
            "nombre_proceso": proceso.get_nombre(),
            "fecha_creacion_proceso": proceso.get_fecha(),
            "estado_proceso": proceso.get_estado()
        })
    return jsonify(respuesta)


@usac.route('/procesos', methods=['POST'])
def crear_proceso():
    data = request.get_json()
    proceso = data.get("proceso")
    cedulas_usuarios = data.get("usuarios_proceso")
    usuarios = [aplicacion.get_usuario_por_cedula(usuario.get("cedula_usuario")) for usuario in cedulas_usuarios]
    aplicacion.crear_proceso(proceso, usuarios)
    return jsonify({"message": "Proceso Creado"})


@usac.route('/procesos/<id_proceso>/usuarios')
def get_usuarios_por_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    respuesta = []
    for usuario in usuarios:
        respuesta.append({
            "cedula_usuario": usuario.get_cedula(),
            "nombre_usuario": usuario.get_nombre(),
            "apellido_usuario": usuario.get_apellido()
        })
    return jsonify(respuesta)


@usac.route('/procesos/<id_proceso>/activos')
def get_activos_por_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    activos = []
    respuesta = []
    for usuario in usuarios:
        activos = activos + aplicacion.get_activos_por_usuario(usuario)
    for activo in activos:
        detalle_activo = activo.get_activo()
        respuesta.append({
            "id_pertenencia": activo.get_id_pertenencia(),
            "id_activo": detalle_activo.get_id(),
            "nombre_activo": detalle_activo.get_nombre(),
            "descripcion_activo": detalle_activo.get_descripcion()
        })
    return jsonify(respuesta)


@usac.route('/procesos/<id_proceso>')
def get_detalle_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    respuesta = {"proceso": {
        "id_proceso": proceso.get_id(),
        "nombre_proceso": proceso.get_nombre(),
        "fecha_creacion_proceso": proceso.get_fecha(),
        "estado_proceso": proceso.get_estado(),
        "cantidad_usuarios_proceso": len(usuarios),
        "cantidad_activos_proceso": aplicacion.get_cantidad_activos_proceso(proceso),
    },
        "usuarios": [],
        "activos": []
    }
    for usuario in usuarios:
        respuesta["usuarios"].append({
            "cedula_usuario": usuario.get_cedula(),
            "nombre_usuario": usuario.get_nombre(),
            "apellido_usuario": usuario.get_apellido()
        })
    for activo in aplicacion.get_activos_por_proceso(proceso):
        usuario = aplicacion.get_usuario_por_activo(activo)
        respuesta["activos"].append({
            "id_pertenencia": activo.get_id_pertenencia(),
            "cedula_usuario": usuario.get_cedula(),
            "nombre_usuario": usuario.get_nombre(),
            "apellido_usuario": usuario.get_apellido(),
            "id_activo": activo.get_activo().get_id(),
            "nombre_activo": activo.get_activo().get_nombre(),
            "descripcion_activo": activo.get_activo().get_descripcion(),
            "revision_activo": activo.get_revision(),
            "estado_revision_activo": activo.get_estado(),
            "observacion_revision": activo.get_observacion()
        })
    return jsonify(respuesta)


@usac.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['DELETE'])
def eliminar_usuario_de_proceso(id_proceso, cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    aplicacion.eliminar_usuario_de_proceso(usuario, proceso)
    return jsonify({"mensaje": "Usuario eliminado correctamente"})


@usac.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['POST'])
def agregar_usuario_a_proceso(id_proceso, cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    aplicacion.agregar_usuario_proceso(usuario, proceso)
    return jsonify({"mensaje": "No implementado"})


@usac.route('/')
def test():
    return "Si funciona!"
