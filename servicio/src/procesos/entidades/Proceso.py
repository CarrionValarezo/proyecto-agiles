from src.login import Administrador
from src.procesos.entidades import ActivoProcesado

REVISADO = 1


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
        self.cant_activos_revisados: int = kwargs.get("cant_activos_revisados")

    @property
    def activos_procesados(self):
        return self._activos_procesados

    @activos_procesados.setter
    def activos_procesados(self, lista: list[ActivoProcesado]):
        self._activos_procesados = lista
        self.cargar_datos()

    def cargar_datos(self):
        datos = self.__cant_observaciones()
        self.cant_observaciones = datos[0]
        self.cant_activos_revisados = datos[1]
        self.cant_activos_procesados = len(self._activos_procesados)

    def __cant_observaciones(self) -> tuple:
        observaciones: int = 0
        revisiones: int = 0
        for activo in self._activos_procesados:
            if activo.revision == REVISADO:
                revisiones += 1
            if activo.estado == "OBSERVACION":
                observaciones += 1
        return observaciones, revisiones

    def to_dict(self) -> dict:
        return {
            "id_proceso": self.id,
            "nombre_proceso": self.nombre,
            "fecha_creacion_proceso": self.fecha,
            "estado_proceso": self.estado,
            "cantidad_activos_procesados": self.cant_activos_procesados,
            "cantidad_activos_revisados": self.cant_activos_revisados,
            "cantidad_observaciones": self.cant_observaciones,
            "creador": self.creador.to_dict(),
            "activos_procesados": [activo.to_dict() for activo in self.activos_procesados]
        }

    @staticmethod
    def procesos_to_dict(procesos):
        return [proceso.to_dict() for proceso in procesos]
