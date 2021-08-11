class Usuario:

    def __init__(self, **kwargs):
        self.cedula: str = kwargs.get("cedula_usuario")
        self.nombre: str = kwargs.get("nombre_usuario")
        self.apellido: str = kwargs.get("apellido_usuario")
        self.cant_obs: int = 0
        self.cant_act: int = 0

    def to_dict(self, opcion: str = "FULL") -> dict:
        dic: dict = {
            "SIMPLE": {"cedula_usuario": self.cedula,
                       "nombre_usuario": self.nombre,
                       "apellido_usuario": self.apellido},
            "FULL": {"cedula_usuario": self.cedula,
                     "nombre_usuario": self.nombre,
                     "apellido_usuario": self.apellido,
                     "cantidad_observaciones_usuario": self.cant_obs,
                     "cantidad_activos_usuario": self.cant_act}
        }
        return dic[opcion]
