from servicio.procesos.casos_uso.UsuarioCasosUso import UsuarioCasosUso
from servicio.procesos.entidades.Activo import Activo
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.entidades.Usuario import Usuario


class AgregarUsuarioAlProceso:

    def __init__(self, repo_procesos: RepoProcesos, ucs: UsuarioCasosUso):
        self.ucs = ucs
        self.repo_procesos: RepoProcesos = repo_procesos

    def agregar(self, u: Usuario, p: Proceso):
        activos: list[Activo] = self.ucs.activos(u)
        for activo in activos:
            self.repo_procesos.agregar_activo(p, activo)
