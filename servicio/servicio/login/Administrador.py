
class Administrador:

    def __init__(self, **kwargs):
        self.nombre: str = kwargs.get("nombre_admin")
        self.apellido: str = kwargs.get("apellido_admin")
        self.password: str = kwargs.get("password_admin")
        self.rol: str = kwargs.get("rol_admin")
        self.cedula: str = kwargs.get("cedula_admin")

    def to_dict(self) -> dict:
        return {"cedula_admin": self.cedula,
                "nombre_admin": self.nombre,
                "apellido_admin": self.apellido}
