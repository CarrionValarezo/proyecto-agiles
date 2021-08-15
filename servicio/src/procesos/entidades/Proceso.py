from src.login import Administrador
from src.procesos.entidades import ActivoProcesado


class Proceso:
    INICIADO: str = "INICIADO"
    FINALIZADO: str = "FINALIZADO"
    CREADO: str = "CREADO"

    def __init__(self, **kwargs):
        self.id: int = kwargs.get("id_proceso")
        self.nombre: str = kwargs.get("nombre_proceso")
        self.fecha: str = kwargs.get("fecha_proceso")
        self.estado: str = kwargs.get("estado_proceso")
        self.creador: Administrador = kwargs.get("creador")
        self._activos_procesados: list[ActivoProcesado] = kwargs.get("activos_procesado")
        self.cant_observaciones: int = kwargs.get("cant_observaciones")
        self.cant_activos_procesados: int = kwargs.get("cant_activos_procesados")
        self.cant_activos_validados: int = kwargs.get("cant_activos_revisados")

    @property
    def activos_procesados(self):
        return self._activos_procesados

    @activos_procesados.setter
    def activos_procesados(self, lista: list[ActivoProcesado]):
        self._activos_procesados = lista
        self.cargar_datos()

    def cargar_datos(self):
        datos = self.__cant_obs_y_validaciones()
        self.cant_observaciones = datos[0]
        self.cant_activos_validados = datos[1]
        self.cant_activos_procesados = len(self._activos_procesados)

    def actualizar_estado(self):
        self.cargar_datos()
        cant_activos: int = self.cant_activos_procesados
        cant_revisados: int = self.cant_activos_validados
        if cant_revisados == cant_activos and self.estado == self.INICIADO:
            self.estado = self.FINALIZADO
        elif cant_revisados > 0 and self.estado == self.CREADO:
            self.estado = self.INICIADO
        elif cant_revisados < cant_activos and self.estado == self.FINALIZADO:
            self.estado = self.INICIADO

    def __cant_obs_y_validaciones(self) -> tuple:
        observaciones: int = 0
        revisiones: int = 0
        for activo in self._activos_procesados:
            if activo.esta_revisado():
                revisiones += 1
            if activo.tiene_observacion():
                observaciones += 1
        return observaciones, revisiones

    def to_dict(self) -> dict:
        return {
            "id_proceso": self.id,
            "nombre_proceso": self.nombre,
            "fecha_creacion_proceso": self.fecha,
            "estado_proceso": self.estado,
            "cantidad_activos_procesados": self.cant_activos_procesados,
            "cantidad_activos_revisados": self.cant_activos_validados,
            "cantidad_observaciones": self.cant_observaciones,
            "creador": self.creador.to_dict(),
            "activos_procesados": [activo.to_dict() for activo in self.activos_procesados]
        }

    @staticmethod
    def procesos_to_dict(procesos):
        return [proceso.to_dict() for proceso in procesos]
