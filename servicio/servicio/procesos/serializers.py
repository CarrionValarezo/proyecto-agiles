from servicio.procesos.entidades import Usuario, Activo, Proceso, Item


def usuario_dict(usuario: Usuario) -> dict:
    return {
        "cedula_usuario": usuario.get_cedula(),
        "nombre_usuario": usuario.get_nombre(),
        "apellido_usuario": usuario.get_apellido(),
    }


def activo_dict(activo: Activo) -> dict:
    return {
        "id_activo": activo.get_id(),
    }


def proceso_dic(proceso: Proceso) -> dict:
    return {
        "id_proceso": proceso.get_id(),
        "nombre_proceso": proceso.get_nombre(),
        "fecha_creacion_proceso": proceso.get_fecha(),
        "estado_proceso": proceso.get_estado()
    }


def item_dic(item: Item) -> dict:
    return {
        "id_item": item.get_id(),
        "nombre_item": item.get_nombre(),
        "descripcion_item": item.get_descripcion()
    }
