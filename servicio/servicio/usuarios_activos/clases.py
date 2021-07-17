class Usuario:

    def __init__(self, **kwargs):
        self.__cedula = kwargs.get("cedula_usuario")
        self.__nombre = kwargs.get("nombre_usuario")
        self.__apellido = kwargs.get("apellido_usuario")
        self.__activos = kwargs.get("activos_usuario")

    def get_cedula(self):
        return self.__cedula

    def get_cantidad_activos(self):
        return len(self.__activos)

    def get_usuario(self):
        return {
            "cedula_usuario": self.__cedula,
            "nombre_usuario": self.__nombre,
            "apellido_usuario": self.__apellido,
        }

    def get_usuario_activos(self):
        return {
            "cedula_usuario": self.__cedula,
            "nombre_usuario": self.__nombre,
            "apellido_usuario": self.__apellido,
            "activos_usuario": [activo.get_activo() for activo in self.__activos]
        }

    def set_activos(self, activos):
        self.__activos = activos


class Activo:

    def __init__(self, **kwargs):
        self.__id = kwargs.get("id_activo")
        self.__nombre = kwargs.get("nombre_activo")
        self.__descripcion = kwargs.get("descripcion_activo")

    def get_activo(self):
        return {
            "id_activo" : self.__id,
            "nombre_activo": self.__nombre,
            "descripcion_activo": self.__descripcion
        }