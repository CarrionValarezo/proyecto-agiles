from src.procesos.casos_uso import UsuarioCasosUso
from src.procesos.repositorios import RepoProcesos
from src.procesos.entidades import Activo, Proceso, Usuario


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
        p = self.repo_procesos.buscar(id_proceso)
        return p
