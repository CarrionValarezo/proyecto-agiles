class Administrador:

    def __init__(self, **kwargs):
        self.__cedula: str = kwargs.get("cedula_admin")
        self.__nombre: str = kwargs.get("nombre_admin")
        self.__apellido: str = kwargs.get("apellido_admin")
        self.__password: str = kwargs.get("password_admin")
        self.__rol: str = kwargs.get("rol_admin")

    def get_cedula(self) -> str:
        return self.__cedula

    def get_nombre(self) -> str:
        return self.__nombre

    def get_apellido(self) -> str:
        return self.__apellido

    def get_password(self) -> str:
        return self.__password

    def set_rol(self, rol: str):
        self.__rol = rol

    def get_rol(self):
        return self.__rol