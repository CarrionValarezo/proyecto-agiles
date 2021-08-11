from servicio.login.Administrador import Administrador
from servicio.procesos.entidades.ActivoProcesado import ActivoProcesado
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.entidades.Proceso import Proceso


class ValidarActivo:

    def __init__(self):
        self.repo_procesos: RepoProcesos = RepoProcesos()
        self.proceso: Proceso = None
        self.activo: ActivoProcesado = None

    def validar(self, id_activo: str, proceso: Proceso,
                estado: str, obs: str, revisor: Administrador) -> bool:
        self.proceso = proceso
        self.__buscar_activo(id_activo)
        if self.activo is not None:
            self.activo.revision = 1
            self.activo.revisor = revisor
            self.activo.observacion = obs
            self.activo.estado = estado
            self.repo_procesos.validar_activo(self.activo, self.proceso)
            self.__actualizar_proceso()
            return True
        return False

    def __buscar_activo(self, id_activo: str) -> None:
        for activo in self.proceso.activos_procesados:
            if activo.id == id_activo:
                self.activo = activo

    def __actualizar_proceso(self):
        self.proceso.cargar_datos()
        if (self.proceso.cant_activos_revisados == self.proceso.cant_activos_procesados
                and self.proceso.estado == "INICIADO"):
            self.proceso.estado = "FINALIZADO"
            self.repo_procesos.actualizar(self.proceso)
        elif self.proceso.cant_activos_revisados > 0 and self.proceso.estado == "CREADO":
            self.proceso.estado = "INICIADO"
            self.repo_procesos.actualizar(self.proceso)
