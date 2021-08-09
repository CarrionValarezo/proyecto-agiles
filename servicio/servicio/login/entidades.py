class Administrador:

    def __init__(self, **kwargs):
        self.cedula: str = kwargs.get("cedula_admin")
        self.nombre: str = kwargs.get("nombre_admin")
        self.apellido: str = kwargs.get("apellido_admin")
        self.password: str = kwargs.get("password_admin")
        self.rol: str = kwargs.get("rol_admin")
