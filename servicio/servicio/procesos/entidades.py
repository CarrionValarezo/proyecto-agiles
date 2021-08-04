class Usuario:

    def __init__(self, **kwargs):
        self.__cedula = kwargs.get("cedula_usuario")
        self.__nombre = kwargs.get("nombre_usuario")
        self.__apellido = kwargs.get("apellido_usuario")
        self.__activos = kwargs.get("activos_usuario")

    def get_cedula(self):
        return self.__cedula

    def get_nombre(self):
        return self.__nombre

    def get_apellido(self):
        return self.__apellido

    def get_activos(self):
        return self.__activos

    def get_cantidad_activos(self):
        return len(self.__activos)

    def set_activos(self, activos):
        self.__activos = activos

    def to_dict(self) -> dict:
        return {
            "cedula_usuario": self.__cedula,
            "nombre_usuario": self.__nombre,
            "apellido_usuario": self.__apellido
        }


class Item:

    def __init__(self, **kwargs):
        self.__id = kwargs.get("id_item")
        self.__nombre = kwargs.get("nombre_item")
        self.__descripcion = kwargs.get("descripcion_item")

    def get_id(self):
        return self.__id

    def get_nombre(self):
        return self.__nombre

    def get_descripcion(self):
        return self.__descripcion

    def to_dict(self) -> dict:
        return {
            "id_item": self.get_id(),
            "nombre_item": self.get_nombre(),
            "descripcion_item": self.get_descripcion()
        }


class Activo:

    def __init__(self, **kwargs):
        self.__id = kwargs.get("id_activo")
        self.__usuario = kwargs.get("usuario")
        self.__activo = kwargs.get("item")
        self.__revision = kwargs.get("revision_activo")
        self.__estado = kwargs.get("estado_revision_activo")
        self.__observacion = kwargs.get("observacion_revision")
        self.__admin_revisor = kwargs.get("admin_revisor")

    def get_id(self):
        return self.__id

    def get_revisor(self):
        return self.__admin_revisor

    def get_usuario(self):
        return self.__usuario

    def get_item(self):
        return self.__activo

    def get_revision(self):
        return self.__revision

    def get_estado(self):
        return self.__estado

    def get_observacion(self):
        return self.__observacion

    def to_dict(self) -> dict:
        return {
            "id_activo": self.get_id(),
        }


class Proceso:

    def __init__(self, **kwargs):
        self.__id = kwargs.get("id_proceso")
        self.__nombre = kwargs.get("nombre_proceso")
        self.__fecha = kwargs.get("fecha_proceso")
        self.__estado = kwargs.get("estado_proceso")
        self.__cant_observaciones = kwargs.get("cantidad_observaciones")
        self.__ced_admin_creador = kwargs.get("ced_admin_creador")

    def get_ced_admin_creador(self):
        return self.__ced_admin_creador

    def get_id(self):
        return self.__id

    def get_nombre(self):
        return self.__nombre

    def get_fecha(self):
        return self.__fecha

    def get_estado(self):
        return self.__estado

    def set_id(self, id):
        self.__id = id

    def set_estado(self, estado):
        self.__estado = estado

    def get_cant_observaciones(self):
        return self.__cant_observaciones

    def set_cant_observaciones(self, cantidad):
        self.__cant_observaciones = cantidad

    def to_dict(self) -> dict:
        return {
            "id_proceso": self.get_id(),
            "nombre_proceso": self.get_nombre(),
            "fecha_creacion_proceso": self.get_fecha(),
            "estado_proceso": self.get_estado(),
            "cantidad_observaciones": self.get_cant_observaciones()
        }

    @staticmethod
    def procesos_to_dict(procesos):
        return [proceso.to_dict() for proceso in procesos]
