class Usuario:

    def __init__(self, **kwargs):
        self.cedula = kwargs.get("cedula_usuario")
        self.nombre = kwargs.get("nombre_usuario")
        self.apellido = kwargs.get("apellido_usuario")
        self.activos = kwargs.get("activos_usuario")

    def to_dict(self) -> dict:
        return {
            "cedula_usuario": self.cedula,
            "nombre_usuario": self.nombre,
            "apellido_usuario": self.apellido
        }


class Item:

    def __init__(self, **kwargs):
        self.id = kwargs.get("id_item")
        self.nombre = kwargs.get("nombre_item")
        self.descripcion = kwargs.get("descripcion_item")

    def to_dict(self) -> dict:
        return {
            "id_item": self.id,
            "nombre_item": self.nombre,
            "descripcion_item": self.descripcion
        }


class Activo:

    def __init__(self, **kwargs):
        self.id = kwargs.get("id_activo")
        self.usuario = kwargs.get("usuario")
        self.item = kwargs.get("item")
        self.revision = kwargs.get("revision_activo")
        self.estado = kwargs.get("estado_revision_activo")
        self.observacion = kwargs.get("observacion_revision")
        self.admin_revisor = kwargs.get("admin_revisor")


    def to_dict(self) -> dict:
        return {
            "id_activo": self.id,
        }


class Proceso:

    def __init__(self, **kwargs):
        self.id = kwargs.get("id_proceso")
        self.nombre = kwargs.get("nombre_proceso")
        self.fecha = kwargs.get("fecha_proceso")
        self.estado = kwargs.get("estado_proceso")
        self.cant_observaciones = kwargs.get("cantidad_observaciones")
        self.ced_admin_creador = kwargs.get("cedula_admin_creador")

    def to_dict(self) -> dict:
        return {
            "id_proceso": self.id,
            "nombre_proceso": self.nombre,
            "fecha_creacion_proceso": self.fecha,
            "estado_proceso": self.estado,
            "cantidad_observaciones": self.cant_observaciones,
            "cedula_admin_creador": self.ced_admin_creador
        }

    @staticmethod
    def procesos_to_dict(procesos):
        return [proceso.to_dict() for proceso in procesos]
