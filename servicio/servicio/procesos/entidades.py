from servicio.login.data import DataAdmin
from servicio.login.entidades import Administrador


class Usuario:

    def __init__(self, **kwargs):
        self.cedula: str = kwargs.get("cedula_usuario")
        self.nombre: str = kwargs.get("nombre_usuario")
        self.apellido: str = kwargs.get("apellido_usuario")

    def to_dict(self) -> dict:
        return {"cedula_usuario": self.cedula,
                "nombre_usuario": self.nombre,
                "apellido_usuario": self.apellido}


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


class ActivoProcesado(Activo):

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.revision: bool = kwargs.get("revision_activo")
        self.estado: str = kwargs.get("estado_revision_activo")
        self.observacion: str = kwargs.get("observacion_revision")
        self.revisor: Administrador = kwargs.get("revisor")

    def to_dict(self) -> dict:
        return {**super().to_dict(),
                "revision_activo": self.revisor,
                "estado_revision_activo": self.estado,
                "observacion_revision": self.observacion,
                "revisor": self.revisor.to_dict()}


class Proceso:

    def __init__(self, **kwargs):
        self.id: int = kwargs.get("id_proceso")
        self.nombre: str = kwargs.get("nombre_proceso")
        self.fecha: str = kwargs.get("fecha_proceso")
        self.estado: str = kwargs.get("estado_proceso")
        self.cant_observaciones: int = kwargs.get("cantidad_observaciones")
        self.creador: Administrador = kwargs.get("creador")
        self.activos_procesados: list[ActivoProcesado] = kwargs.get("activos_procesado")

    def to_dict(self) -> dict:
        return {
            "id_proceso": self.id,
            "nombre_proceso": self.nombre,
            "fecha_creacion_proceso": self.fecha,
            "estado_proceso": self.estado,
            "cantidad_observaciones": self.cant_observaciones,
            "creador": self.creador.to_dict(),
            "activos_procesados": [activo.to_dict() for activo in self.activos_procesados]
        }

    @staticmethod
    def procesos_to_dict(procesos):
        return [proceso.to_dict() for proceso in procesos]
