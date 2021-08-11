from servicio.login.Administrador import Administrador
from servicio.procesos.entidades.ActivoProcesado import ActivoProcesado
from servicio.procesos.entidades.Usuario import Usuario


class Proceso:

    def __init__(self, **kwargs):
        self.id: int = kwargs.get("id_proceso")
        self.nombre: str = kwargs.get("nombre_proceso")
        self.fecha: str = kwargs.get("fecha_proceso")
        self.estado: str = kwargs.get("estado_proceso")
        self.creador: Administrador = kwargs.get("creador")
        self._activos_procesados: list[ActivoProcesado] = kwargs.get("activos_procesado")
        self.cant_observaciones: int = kwargs.get("cant_observaciones")
        self.cant_activos_procesados: int = kwargs.get("cant_activos_procesados")

    @property
    def activos_procesados(self):
        return self._activos_procesados

    @activos_procesados.setter
    def activos_procesados(self, lista: list[ActivoProcesado]):
        self._activos_procesados = lista
        self.cant_observaciones = self.__cant_observaciones()
        self.cant_activos_procesados = len(lista)

    def __cant_observaciones(self) -> int:
        observaciones: int = 0
        for activo in self._activos_procesados:
            if activo.estado == "OBSERVACION":
                observaciones += 1
        return observaciones

    def to_dict(self) -> dict:
        return {
            "id_proceso": self.id,
            "nombre_proceso": self.nombre,
            "fecha_creacion_proceso": self.fecha,
            "estado_proceso": self.estado,
            "cantidad_observaciones": self.cant_observaciones,
            "cantidad_activos_procesados": self.cant_activos_procesados,
            "creador": self.creador.to_dict(),
            "activos_procesados": [activo.to_dict() for activo in self.activos_procesados]
        }

    @staticmethod
    def procesos_to_dict(procesos):
        return [proceso.to_dict() for proceso in procesos]
