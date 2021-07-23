from flask import Blueprint, jsonify, request
from . import aplicacion
from . import serializers

procesos_blueprint = Blueprint("usac", __name__)


@procesos_blueprint.route('/usuarios/cantidad-activos')
def get_usuarios_cant_activos():
    respuesta = []
    usuarios_cant_activos = aplicacion.get_usuarios_cant_activos()
    for usuario, cant_activos in usuarios_cant_activos:
        data_usuario = serializers.usuario_dict(usuario)
        data_usuario["cantidad_activos_usuario"] = cant_activos
        respuesta.append(data_usuario)
    return jsonify(respuesta)


@procesos_blueprint.route('/usuarios/<cedula>/activos')
def get_activos_por_usuario(cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    respuesta = []
    activos_usuario = aplicacion.get_activos_por_usuario(usuario)
    for activo in activos_usuario:
        data_activo = serializers.activo_dict(activo)
        data_item = serializers.item_dic(activo.get_item())
        respuesta.append({**data_activo, **data_item})
    return jsonify(respuesta)


@procesos_blueprint.route('/usuarios/<cedula>/procesos')
def get_procesos_por_usuario(cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    procesos = aplicacion.get_procesos_por_usuario(usuario)
    respuesta = []
    for proceso in procesos:
        respuesta.append(serializers.proceso_dic(proceso))
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos')
def get_procesos():
    procesos = aplicacion.get_procesos()
    respuesta = []
    for proceso in procesos:
        respuesta.append(serializers.proceso_dic(proceso))
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos', methods=['POST'])
def crear_proceso():
    data = request.get_json()
    proceso = data.get("proceso")
    cedulas_usuarios = data.get("usuarios_proceso")
    usuarios = [aplicacion.get_usuario_por_cedula(usuario.get("cedula_usuario")) for usuario in cedulas_usuarios]
    nuevo_proceso = aplicacion.crear_proceso(proceso, usuarios)
    return jsonify({"id_proceso": nuevo_proceso.get_id()})


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios')
def get_usuarios_por_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    respuesta = []
    for usuario in usuarios:
        respuesta.append(serializers.usuario_dict(usuario))
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/activos')
def get_activos_por_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    activos = []
    respuesta = []
    for usuario in usuarios:
        activos = activos + aplicacion.get_activos_por_usuario(usuario)
    for activo in activos:
        item = activo.get_item()
        data_activo = serializers.activo_dict(activo)
        data_item = serializers.item_dic(item)
        respuesta.append({**data_activo, **data_item})
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>')
def get_detalle_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    respuesta = {"proceso": {
        **serializers.proceso_dic(proceso),
        "cantidad_usuarios_proceso": len(usuarios),
        "cantidad_activos_proceso": aplicacion.get_cantidad_activos_proceso(proceso),
    },
        "usuarios": [],
        "activos": []
    }
    for usuario in usuarios:
        respuesta["usuarios"].append(serializers.usuario_dict(usuario))
    for activo in aplicacion.get_activos_por_proceso(proceso):
        usuario = aplicacion.get_usuario_por_activo(activo)
        item = activo.get_item()
        respuesta["activos"].append({**serializers.activo_dict(activo),
                                     **serializers.item_dic(item),
                                     **serializers.usuario_dict(usuario),
                                     "revision_activo": activo.get_revision(),
                                     "estado_revision_activo": activo.get_estado(),
                                     "observacion_revision": activo.get_observacion()
                                     })
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['DELETE'])
def eliminar_usuario_de_proceso(id_proceso, cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    aplicacion.eliminar_usuario_de_proceso(usuario, proceso)
    return jsonify({"mensaje": "Usuario eliminado correctamente"})


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['POST'])
def agregar_usuario_a_proceso(id_proceso, cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    aplicacion.agregar_usuario_proceso(usuario, proceso)
    return jsonify({"mensaje": "No implementado"})


@procesos_blueprint.route('/procesos/<id_proceso>/activos/<id_activo>', methods=['PUT'])
def validar_activo(id_proceso, id_activo):
    data = request.get_json()
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    activo = aplicacion.get_activo_por_id(id_activo)
    aplicacion.validar_activo(activo, proceso, data.get("estado_activo"), data.get("observacion_activo"))
    return jsonify({"mensaje": "Activo validado"})


@procesos_blueprint.route('/')
def test():
    return "Si funciona!"
