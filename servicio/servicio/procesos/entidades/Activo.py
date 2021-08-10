from servicio.procesos.entidades.Usuario import Usuario


class Activo:

    def __init__(self, **kwargs):
        self.id: str = kwargs.get("id_activo")
        self.nombre: str = kwargs.get("nombre_activo")
        self.descripcion: str = kwargs.get("descripcion_activo")
        self.usuario: Usuario = kwargs.get("usuario")

    def to_dict(self) -> dict:
        return {"id_activo": self.id,
                "nombre_activo": self.nombre,
                "descripcion_activo": self.descripcion,
                "usuario": self.usuario.to_dict()}

