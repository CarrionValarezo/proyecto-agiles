from src.login import Administrador
from src.procesos.repositorios import RepoProcesos
from src.procesos.entidades import Proceso, ActivoProcesado


class ValidarActivo:

    def __init__(self, repo_procesos: RepoProcesos):
        self.repo_procesos: RepoProcesos = repo_procesos

    def validar(self, id_activo: str, proceso: Proceso,
                estado: str, obs: str, revisor: Administrador) -> bool:
        activo = self.__buscar_activo(id_activo, proceso)
        if activo is None:
            return False
        activo.validacion = ActivoProcesado.REVISADO
        activo.revisor = revisor
        activo.observacion = obs
        activo.estado_validacion = estado
        self.repo_procesos.validar_activo(activo, proceso)
        proceso.actualizar_estado()
        self.repo_procesos.actualizar(proceso)
        return True

    def __buscar_activo(self, id_activo: str, proceso: Proceso) -> ActivoProcesado:
        for activo in proceso.activos_procesados:
            if activo.id == id_activo:
                return activo
