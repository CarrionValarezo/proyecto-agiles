from servicio.procesos.casos_uso.UsuarioCasosUso import UsuarioCasosUso
from servicio.procesos.entidades.Activo import Activo
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.entidades.Usuario import Usuario


class CrearProceso:

    def __init__(self, repo_procesos: RepoProcesos, ucs: UsuarioCasosUso):
        self.ucs = ucs
        self.repo_procesos: RepoProcesos = repo_procesos

    def crear_proceso(self, usuarios: list[Usuario], p: Proceso) -> Proceso:
        id_proceso: int = self.repo_procesos.crear(p)
        p.id = id_proceso
        for u in usuarios:
            activos: list[Activo] = self.ucs.activos(u)
            for a in activos:
                self.repo_procesos.agregar_activo(p, a)
        return p
