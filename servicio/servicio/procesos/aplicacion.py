from .data import DataActivo, DataUsuario, DataProceso
from .entidades import Usuario, Item, Activo, Proceso


# Devuelve una lista de tuplas con el usuario y su cantidad de activos
# [(usuario: Usuario, cantidad_de_activos: int),]
def get_usuarios_cant_activos():
    repo_usuario_activo = DataActivo()
    repo_usuario = DataUsuario()
    usuarios_activos = []
    for data_usuario in repo_usuario.get_usuarios():
        usuario = Usuario(**data_usuario)
        usuarios_activos.append((usuario, repo_usuario_activo.get_cant_activos_por_usuario(usuario)))
    return usuarios_activos


def get_usuario_por_cedula(cedula):
    repos_usuario = DataUsuario()
    usuario = Usuario(**repos_usuario.get_usuario_por_cedula(cedula))
    return usuario


def get_activos_por_usuario(usuario):
    repo_usuario_activo = DataActivo()
    activos_usuario = []
    for data_activo in repo_usuario_activo.get_activos_por_usuario(usuario):
        activos_usuario.append(Activo(**data_activo, item=Item(**data_activo)))
    return activos_usuario


def crear_proceso(proceso, usuarios):
    repo_procesos = DataProceso()
    nuevo_proceso = Proceso(**proceso)
    activos = []
    for usuario in usuarios:
        activos = activos + get_activos_por_usuario(usuario)
    nuevo_proceso.set_id(repo_procesos.crear_proceso(nuevo_proceso))
    for activo in activos:
        repo_procesos.agregar_activo(nuevo_proceso, activo)
    return nuevo_proceso


def asignar_estado_proceso(proceso, activos):
    activos_revisados = 0
    if proceso.get_estado() == "CREADO":
        return "CREADO"
    elif proceso.get_estado() == "FINALIZADO":
        return "FINALIZADO"
    elif proceso.get_estado() == "INICIADO":
        for activo in activos:
            if activo.get_revision():
                activos_revisados += 1
        if len(activos) == activos_revisados:
            return "FINALIZADO"
    return "INICIADO"


def get_proceso_por_id(id_proceso):
    repo_procesos = DataProceso()
    proceso = Proceso(**repo_procesos.get_proceso_por_id(id_proceso))
    proceso.set_cant_observaciones(repo_procesos.get_cant_activos_observacion(proceso))
    return proceso


def get_activos_por_proceso(proceso):
    repo_procesos = DataProceso()
    activos = [Activo(**data_activo, item=Item(**data_activo)) for data_activo in
               repo_procesos.get_activos_por_proceso(proceso)]
    estado_actualizado = asignar_estado_proceso(proceso, activos)
    if proceso.get_estado() != estado_actualizado:
        proceso.set_estado(estado_actualizado)
        repo_procesos.actualizar_estado(proceso)
        proceso.set_estado(estado_actualizado)
    return activos


def get_usuario_por_activo(activo):
    repo_usuarios = DataUsuario()
    usuario = Usuario(**repo_usuarios.get_usuario_por_activo(activo))
    return usuario


# Devulve los usuarios que estan registrados en un proceso
def get_usuarios_por_proceso(proceso):
    repo_proceso = DataProceso()
    usuarios = {}
    for usuario in repo_proceso.get_usuarios_por_proceso(proceso):
        if usuario["cedula_usuario"] not in usuarios:
            usuarios[usuario["cedula_usuario"]] = {
                "cedula_usuario": usuario["cedula_usuario"],
                "nombre_usuario": usuario["nombre_usuario"],
                "apellido_usuario": usuario["apellido_usuario"]
            }

    lista_usuarios = [Usuario(**data_usuario) for data_usuario in usuarios.values()]
    return lista_usuarios


def get_cantidad_activos_proceso(proceso):
    return DataProceso().get_cantidad_activos_por_proceso(proceso)


def get_procesos_por_usuario(usuario):
    repo_procesos = DataProceso()
    procesos = [Proceso(**data_procesos) for data_procesos in repo_procesos.get_procesos_por_usuario(usuario)]
    for proceso in procesos:
        proceso.set_cant_observaciones(repo_procesos.get_cant_activos_observacion_usuario(proceso, usuario))
    return procesos


def asignar_color(proceso):
    repo_activos = DataActivo()
    activos = [Activo(**activo) for activo in repo_activos.get_activos_por_proceso(proceso)]


def get_procesos():
    repo_procesos = DataProceso()
    procesos = [Proceso(**data_procesos) for data_procesos in repo_procesos.get_procesos()]
    for proceso in procesos:
        proceso.set_cant_observaciones(repo_procesos.get_cant_activos_observacion(proceso))
    return procesos


def eliminar_usuario_de_proceso(usuario, proceso):
    repo_procesos = DataProceso()
    repo_procesos.eliminar_usuario_de_proceso(usuario, proceso)
    return None


def agregar_usuario_proceso(usuario, proceso):
    return None


def get_activo_por_id(id_activo):
    repo_activos = DataActivo()
    activo = Activo(**repo_activos.get_activo_por_id(id_activo))
    return activo


def validar_activo(activo, proceso, estado, observacion):
    repo_procesos = DataProceso()
    repo_procesos.validar_activo(activo, proceso, estado, observacion)
    if proceso.get_estado() == "CREADO":
        proceso.set_estado("INICIADO")
        DataProceso().actualizar_estado(proceso)


def get_cant_activos_observacion_usuario(usuario, proceso):
    repo_procesos = DataProceso()
    return repo_procesos.get_cant_activos_observacion_usuario(proceso, usuario)

