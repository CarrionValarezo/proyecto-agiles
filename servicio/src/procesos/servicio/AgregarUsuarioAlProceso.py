from src.procesos.servicio import ServicioUsuario
from src.procesos.repositorios import RepoProcesos
from src.procesos.entidades import Activo, Proceso, Usuario


class AgregarUsuarioAlProceso:

    def __init__(self, repo_procesos: RepoProcesos, ucs: ServicioUsuario):
        self.ucs = ucs
        self.repo_procesos: RepoProcesos = repo_procesos

    def agregar(self, u: Usuario, p: Proceso):
        activos: list[Activo] = self.ucs.activos(u)
        for activo in activos:
            self.repo_procesos.agregar_activo(p, activo)
