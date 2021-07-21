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


def get_usuario_por_cedula(cedula):
    repos_usuario = DataUsuario()
    usuario = Usuario(**repos_usuario.get_usuario_por_cedula(cedula))
    return usuario

def get_activos_por_usuario(usuario):
    repo_usuario_activo = DataUsuarioActivo()
    activos_usuario = []
    for data_activo in repo_usuario_activo.get_activos_por_usuario(usuario):
        activos_usuario.append(UsuarioActivo(**data_activo, activo=Activo(**data_activo)))
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

