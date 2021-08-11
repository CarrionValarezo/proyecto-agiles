from servicio.procesos.casos_uso.UsuarioCasosUso import UsuarioCasosUso
from servicio.procesos.entidades.Activo import Activo
from servicio.procesos.repositorios.RepoActivos import RepoActivos
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.repositorios.RepoUsuarios import RepoUsuarios
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.entidades.Usuario import Usuario


class EliminarUsuarioProcesado:

    def __init__(self):
        self.repo_procesos: RepoProcesos = RepoProcesos()
        self.repo_activos: RepoActivos = RepoActivos()
        self.repo_usuarios: RepoUsuarios = RepoUsuarios()

    def eliminar(self, u: Usuario, p: Proceso):
        ucs = UsuarioCasosUso()
        activos: list[Activo] = ucs.activos(u)
        for activo in activos:
            self.repo_procesos.eliminar_activo(p, activo)
