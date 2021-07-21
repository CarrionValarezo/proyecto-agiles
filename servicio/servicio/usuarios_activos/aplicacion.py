from .data import DataUsuarioActivo, DataUsuario, DataProceso
from .entidades import Usuario, Activo, UsuarioActivo, Proceso


# Devuelve una lista de tuplas con el usuario y su cantidad de activos
# [(usuario: Usuario, cantidad_de_activos: int),]
def get_usuarios_cant_activos():
    repo_usuario_activo = DataUsuarioActivo()
    repo_usuario = DataUsuario()
    usuarios_activos = []
    for data_usuario in repo_usuario.get_usuarios():
        usuario = Usuario(**data_usuario)
        usuarios_activos.append((usuario, repo_usuario_activo.get_cant_activos_por_usuario(usuario)))
    return usuarios_activos


# Devulve un usuario en base a la cedula
def get_usuario_por_cedula(cedula):
    repos_usuario = DataUsuario()
    usuario = Usuario(**repos_usuario.get_usuario_por_cedula(cedula))
    return usuario


# Devuelve una lista con los activos que le pertenecen al usuario
def get_activos_por_usuario(usuario):
    repo_usuario_activo = DataUsuarioActivo()
    activos_usuario = []
    for data_activo in repo_usuario_activo.get_activos_por_usuario(usuario):
        activos_usuario.append(UsuarioActivo(**data_activo, activo=Activo(**data_activo)))
    return activos_usuario


# Crea un proceso, lo inicializa en base a los activos que poseen cada usuario enviado y retorno los datos del proceso
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


# Devuelve un proceso en base a su id
def get_proceso_por_id(id_proceso):
    repo_procesos = DataProceso()
    proceso = Proceso(**repo_procesos.get_proceso_por_id(id_proceso))
    return proceso


# Devuelve una lista de activos en base a un proceso
def get_activos_por_proceso(proceso):
    repo_procesos = DataProceso()
    activos = [UsuarioActivo(**data_activo, activo=Activo(**data_activo)) for data_activo in
               repo_procesos.get_activos_por_proceso(proceso)]
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
