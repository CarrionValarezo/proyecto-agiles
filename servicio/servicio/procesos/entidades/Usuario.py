
class Usuario:

    def __init__(self, **kwargs):
        self.cedula: str = kwargs.get("cedula_usuario")
        self.nombre: str = kwargs.get("nombre_usuario")
        self.apellido: str = kwargs.get("apellido_usuario")

    def to_dict(self) -> dict:
        return {"cedula_usuario": self.cedula,
                "nombre_usuario": self.nombre,
                "apellido_usuario": self.apellido}

