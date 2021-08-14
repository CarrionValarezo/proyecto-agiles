from flask import Blueprint, jsonify, request
from flask_app.app import auth, db
from src.login import Administrador
from src.procesos.casos_uso import AgregarUsuarioAlProceso, CrearProceso, EliminarUsuarioProcesado, UsuarioCasosUso, \
    UsuariosPorProceso, ProcesoCasosUso, ValidarActivo
from src.procesos.entidades import Activo, Proceso, Usuario
from src.procesos.repositorios import RepoProcesos, RepoActivos, RepoUsuarios

procesos_blueprint = Blueprint("procesos_blueprint", __name__)


# Usuarios
@procesos_blueprint.route('/usuarios/cantidad-activos')
@auth.login_required
def get_usuarios_cant_activos():
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    usuario_casos_uso = UsuarioCasosUso(repo_procesos=repo_procesos,
                                        repo_activos=repo_activos,
                                        repo_usuarios=repo_usuarios)

    usuarios_cant_activos = usuario_casos_uso.usuarios_cant_activos()

    respuesta = [{**usuario.to_dict(), "cantidad_activos_usuario": cant_activos}
                 for usuario, cant_activos in usuarios_cant_activos]

    return jsonify(respuesta)


@procesos_blueprint.route('/usuarios/<cedula>/procesos')
@auth.login_required
def get_procesos_por_usuario(cedula):
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    csu = UsuarioCasosUso(repo_procesos=repo_procesos,
                          repo_activos=repo_activos,
                          repo_usuarios=repo_usuarios)

    usuario: Usuario = csu.buscar(cedula)
    procesos: list[Proceso] = csu.procesos(usuario)
    respuesta = [proceso.to_dict() for proceso in procesos]
    return jsonify(respuesta)


@procesos_blueprint.route('/usuarios/<cedula>/activos')
@auth.login_required
def get_activos_por_usuario(cedula):
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    csu = UsuarioCasosUso(repo_procesos=repo_procesos,
                          repo_activos=repo_activos,
                          repo_usuarios=repo_usuarios)

    usuario: Usuario = csu.buscar(cedula)
    activos: list[Activo] = csu.activos(usuario)
    respuesta: list[dict] = [activo.to_dict() for activo in activos]
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios-faltantes')
@auth.login_required
def get_usuarios_faltante(id_proceso):
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    ucs = UsuarioCasosUso(repo_procesos=repo_procesos,
                          repo_activos=repo_activos,
                          repo_usuarios=repo_usuarios)

    pcs = ProcesoCasosUso(repo_procesos=repo_procesos,
                          repo_usuarios=repo_usuarios)

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
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    pcs = ProcesoCasosUso(repo_procesos, repo_usuarios)

    respuesta = [p.to_dict() for p in pcs.listar()]
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos/<id_proceso>')
@auth.login_required
def get_detalle_proceso(id_proceso):
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    pcs = ProcesoCasosUso(repo_procesos, repo_usuarios)
    upp = UsuariosPorProceso()

    proceso: Proceso = pcs.buscar(id_proceso)
    usuarios: list[Usuario] = upp.usuarios(proceso)

    respuesta = proceso.to_dict()
    respuesta["usuarios_procesados"] = [u.to_dict("FULL") for u in usuarios]
    respuesta["cantidad_usuarios_procesados"] = len(usuarios)
    return jsonify(respuesta)


@procesos_blueprint.route('/procesos', methods=['POST'])
@auth.login_required(role="superadmin")
def crear_proceso():
    dict_cursor = db.get_cursor()

    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    ucs = UsuarioCasosUso(repo_procesos, repo_usuarios, repo_activos)
    crear = CrearProceso(repo_procesos=repo_procesos,
                         ucs=ucs)

    creador: Administrador = auth.current_user()
    data_proceso = request.get_json().get("proceso")
    usuarios_proceso = request.get_json().get("usuarios_proceso")

    usuarios: list[Usuario] = [ucs.buscar(usuario.get("cedula_usuario")) for usuario in usuarios_proceso]
    proceso: Proceso = Proceso(**data_proceso, creador=creador)

    nuevo_proceso: Proceso = crear.crear_proceso(usuarios, proceso)

    return jsonify({"id_proceso": nuevo_proceso.id})


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['DELETE'])
@auth.login_required(role="superadmin")
def eliminar_usuario_de_proceso(id_proceso, cedula):
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    ucs = UsuarioCasosUso(repo_procesos, repo_usuarios, repo_activos)
    pcs = ProcesoCasosUso(repo_procesos, repo_usuarios)
    elm = EliminarUsuarioProcesado(repo_procesos=repo_procesos,
                                   ucs=ucs)

    proceso: Proceso = pcs.buscar(id_proceso)
    usuario: Usuario = ucs.buscar(cedula)

    elm.eliminar(usuario, proceso)
    return jsonify({"mensaje": "Usuario eliminado correctamente"})


@procesos_blueprint.route('/procesos/<id_proceso>/usuarios/<cedula>', methods=['POST'])
@auth.login_required(role="superadmin")
def agregar_usuario_a_proceso(id_proceso, cedula):
    dict_cursor = db.get_cursor()
    repo_procesos = RepoProcesos(dict_cursor)
    repo_activos = RepoActivos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    ucs = UsuarioCasosUso(repo_procesos, repo_usuarios, repo_activos)
    pcs = ProcesoCasosUso(repo_procesos, repo_usuarios)
    agg = AgregarUsuarioAlProceso(repo_procesos=repo_procesos, ucs=ucs)

    proceso: Proceso = pcs.buscar(id_proceso)
    usuario: Usuario = ucs.buscar(cedula)

    agg.agregar(usuario, proceso)
    return jsonify({"mensaje": "Usuario agregado correctamente"})


@procesos_blueprint.route('/procesos/<id_proceso>/activos/<id_activo>', methods=['PUT'])
@auth.login_required
def validar_activo(id_proceso, id_activo):
    dict_cursor = db.get_cursor()

    repo_procesos = RepoProcesos(dict_cursor)
    repo_usuarios = RepoUsuarios(dict_cursor)

    pcs = ProcesoCasosUso(repo_procesos=repo_procesos,
                          repo_usuarios=repo_usuarios)
    vla = ValidarActivo(repo_procesos=repo_procesos)

    proceso: Proceso = pcs.buscar(id_proceso)

    observacion: str = request.get_json().get("observacion_activo")
    estado: str = request.get_json().get("estado_activo")
    revisor: Administrador = auth.current_user()

    vla.validar(id_activo, proceso, estado, observacion, revisor)
    return jsonify({"mensaje": "Activo validado"})
