from servicio.procesos.entidades.ActivoProcesado import ActivoProcesado
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.entidades.Proceso import Proceso


class ValidarActivo:

    def __init__(self):
        self.repo_procesos: RepoProcesos = RepoProcesos()

    def validar(self, activo: ActivoProcesado, proceso: Proceso) -> None:
        self.repo_procesos.validar_activo(activo, proceso)
