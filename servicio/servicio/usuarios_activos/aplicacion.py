from .data import DataUsuario, DataActivo
from .entidades import Usuario, Activo


def get_usuarios_activos():
    repositorio_usuarios = DataUsuario()
    repositorio_activos = DataActivo()
    lista_usuarios = [Usuario(**usuario) for usuario in repositorio_usuarios.get_usuarios()]
    for usuario in lista_usuarios:
        usuario.set_activos([Activo(**activo) for activo in repositorio_activos.get_activos_by_usuario(usuario)])
    return lista_usuarios
