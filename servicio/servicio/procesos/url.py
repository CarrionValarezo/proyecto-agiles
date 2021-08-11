from flask import Blueprint, jsonify, request

from servicio.login.Administrador import Administrador
from servicio.procesos.aplicacion import Aplicacion
from servicio.procesos.casos_uso.CrearProceso import CrearProceso
from servicio.procesos.casos_uso.UsuarioCasosUso import UsuarioCasosUso
from servicio.app import auth
from servicio.procesos.entidades.Activo import Activo
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.entidades.Usuario import Usuario
from servicio.procesos.casos_uso.ProcesoCasosUso import ProcesoCasosUso

procesos_blueprint = Blueprint("usac", __name__)
aplicacion = Aplicacion()


# Usuarios
@procesos_blueprint.route('/usuarios/cantidad-activos')
@auth.login_required
def get_usuarios_cant_activos():
    usuario_casos_uso = UsuarioCasosUso()
    usuarios_cant_activos = usuario_casos_uso.usuarios_cant_activos()
    respuesta = [{**usuario.to_dict(), "cantidad_activos_usuario": cant_activos}
                 for usuario, cant_activos in usuarios_cant_activos]
    return jsonify(respuesta)


@procesos_blueprint.route('/usuarios/<cedula>/procesos')
@auth.login_required
def get_procesos_por_usuario(cedula):
    csu = UsuarioCasosUso()
    usuario: Usuario = csu.buscar(cedula)
    procesos: list[Proceso] = csu.procesos(usuario)
    respuesta = [proceso.to_dict() for proceso in procesos]
    return jsonify(respuesta)


@procesos_blueprint.route('/usuarios/<cedula>/activos')
@auth.login_required
def get_activos_por_usuario(cedula):
    csu = UsuarioCasosUso()
    usuario: Usuario = csu.buscar(cedula)
    activos: list[Activo] = csu.activos(usuario)
    respuesta: list[dict] = [activo.to_dict() for activo in activos]
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios-faltantes')
@auth.login_required
def get_usuarios_faltante(id_proceso):
    pcs = ProcesoCasosUso()
    ucs = UsuarioCasosUso()
    proceso: Proceso = pcs.buscar(id_proceso)
    usuarios_faltantes: list[Usuario] = pcs.usuarios_faltantes(proceso)
    usuarios_cant_activos: list[tuple] = [ucs.usuario_cant_activos(u) for u in usuarios_faltantes]
    respuesta = [{**usuario.to_dict(), "cantidad_activos_usuario": cant_activos}
                 for usuario, cant_activos in usuarios_cant_activos]
    return jsonify(respuesta)


# Procesos
@procesos_blueprint.route('/procesos')
@auth.login_required
def get_procesos():
    pcs = ProcesoCasosUso()
    respuesta = [p.to_dict() for p in pcs.listar()]
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>')
@auth.login_required
def get_detalle_proceso(id_proceso):
    pcs = ProcesoCasosUso()
    proceso: Proceso = pcs.buscar(id_proceso)
    return jsonify(proceso.to_dict())


@procesos_blueprint.route('/procesos', methods=['POST'])
@auth.login_required(role="superadmin")
def crear_proceso():
    ucs = UsuarioCasosUso()
    crear = CrearProceso()

    creador: Administrador = auth.current_user()
    data_proceso = request.get_json().get("proceso")
    usuarios_proceso = request.get_json().get("usuarios_proceso")

    usuarios: list[Usuario] = [ucs.buscar(usuario.get("cedula_usuario")) for usuario in usuarios_proceso]
    proceso: Proceso = Proceso(**data_proceso, creador=creador)

    nuevo_proceso: Proceso = crear.crear_proceso(usuarios, proceso)

    return jsonify({"id_proceso": nuevo_proceso.id})

'''






@procesos_blueprint.route('/procesos/<id_proceso>/usuarios')
@auth.login_required
def get_usuarios_por_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    respuesta = [usuario.to_dict() for usuario in usuarios]
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/activos')
@auth.login_required
def get_activos_por_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    activos = []
    for usuario in usuarios:
        activos = activos + aplicacion.get_activos_por_usuario(usuario)
    respuesta = [{**activo.to_dict(), **activo.item.to_dict()} for activo in activos]
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['POST'])
@auth.login_required(role="superadmin")
def agregar_usuario_a_proceso(id_proceso, cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    aplicacion.agregar_usuario_proceso(usuario, proceso)
    return jsonify({"mensaje": "Usuario agregado correctamente"})




@procesos_blueprint.route('/procesos/<id_proceso>')
@auth.login_required
def get_detalle_proceso(id_proceso):
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    usuarios = aplicacion.get_usuarios_por_proceso(proceso)
    respuesta = {"proceso": {**proceso.to_dict(),
                             "cantidad_usuarios_proceso": len(usuarios),
                             "cantidad_activos_proceso": aplicacion.get_cantidad_activos_proceso(proceso)},
                 "usuarios": [],
                 "activos": []
                 }
    for usuario in usuarios:
        cant_act = len(aplicacion.get_cant_activos_usuario(usuario))
        cant_obs = aplicacion.get_cant_activos_observacion_usuario(usuario, proceso)
        respuesta["usuarios"].append({**usuario.to_dict(),
                                      "cantidad_observaciones_usuario": cant_obs,
                                      "cantidad_activos_usuario": cant_act})
    for activo in aplicacion.get_activos_por_proceso(proceso):
        respuesta["proceso"]["estado_proceso"] = proceso.estado
        usuario = aplicacion.get_usuario_por_activo(activo)
        respuesta["activos"].append({**activo.to_dict(),
                                     **activo.item.to_dict(),
                                     **usuario.to_dict(),
                                     "revision_activo": activo.revision,
                                     "estado_revision_activo": activo.estado,
                                     "observacion_revision": activo.observacion,
                                     "admin_revisor": activo.cedula_revisor,
                                     "nombre_revisor": activo.nombre_revisor,
                                     "apellido_revisor": activo.apellido_revisor})
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['DELETE'])
@auth.login_required(role="superadmin")
def eliminar_usuario_de_proceso(id_proceso, cedula):
    usuario = aplicacion.get_usuario_por_cedula(cedula)
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    aplicacion.eliminar_usuario_de_proceso(usuario, proceso)
    return jsonify({"mensaje": "Usuario eliminado correctamente"})


@procesos_blueprint.route('/procesos/<id_proceso>/activos/<id_activo>', methods=['PUT'])
@auth.login_required
def validar_activo(id_proceso, id_activo):
    data = request.get_json()
    proceso = aplicacion.get_proceso_por_id(id_proceso)
    activo = aplicacion.get_activo_por_id(id_activo)
    admin = auth.current_user()
    aplicacion.validar_activo(activo, proceso, data.get("estado_activo"), data.get("observacion_activo"), admin)
    return jsonify({"mensaje": "Activo validado"})


@procesos_blueprint.route('/')
def test():
'''
