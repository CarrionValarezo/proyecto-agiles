from .data import DataUsuario, DataActivo
from .clases import Usuario, Activo
import json


def get_usuarios_activos():
    lista_usuarios = [Usuario(**usuario) for usuario in DataUsuario().get_usuarios()]
    for usuario in lista_usuarios:
        usuario.set_activos([Activo(**activo) for activo in DataActivo().get_activos_by_usuario(usuario)])
    json_list = [usuario.get_usuario_activos() for usuario in lista_usuarios]
    return json_list

def get_usuario_cantidad_activo():
    lista_usuarios = [Usuario(**usuario) for usuario in DataUsuario().get_usuarios()]

    for usuario in lista_usuarios:
        usuario.set_activos([Activo(**activo) for activo in DataActivo().get_activos_by_usuario(usuario)])

    json_list = [usuario.get_usuario() for usuario in lista_usuarios]
    return json_list
