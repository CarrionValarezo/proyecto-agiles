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


class Activo:

    def __init__(self, **kwargs):
        self.__id = kwargs.get("id_activo")
        self.__nombre = kwargs.get("nombre_activo")
        self.__descripcion = kwargs.get("descripcion_activo")

    def get_id(self):
        return self.__id

    def get_nombre(self):
        return self.__nombre

    def get_descripcion(self):
        return self.__descripcion


class UsuarioActivo:

    def __init__(self, **kwargs):
        self.__id_pertenencia = kwargs.get("id_pertenencia")
        self.__usuario = kwargs.get("usuario")
        self.__activo = kwargs.get("activo")

    def get_id_pertenencia(self):
        return self.__id_pertenencia

    def get_usuario(self):
        return self.__usuario

    def get_activo(self):
        return self.__activo

class Proceso:

    def __init__(self, **kwargs):
        self.__id = kwargs.get("id_proceso")
        self.__nombre = kwargs.get("nombre_proceso")
        self.__fecha = kwargs.get("fecha_proceso")

    def get_id(self):
        return self.__id

    def get_nombre(self):
        return self.__nombre

    def get_fecha(self):
        return self.__fecha
