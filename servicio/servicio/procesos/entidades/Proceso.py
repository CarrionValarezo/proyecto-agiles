from servicio.login.Administrador import Administrador
from servicio.procesos.entidades.ActivoProcesado import ActivoProcesado

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
