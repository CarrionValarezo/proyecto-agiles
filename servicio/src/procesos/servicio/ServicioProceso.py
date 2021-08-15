from src.procesos.repositorios import RepoProcesos, RepoUsuarios
from src.procesos.entidades import ActivoProcesado, Proceso, Usuario


class ServicioProceso:

    def __init__(self, repo_procesos: RepoProcesos, repo_usuarios: RepoUsuarios):
        self.repo_procesos: RepoProcesos = repo_procesos
        self.repo_usuarios: RepoUsuarios = repo_usuarios

    def listar(self) -> list[Proceso]:
        return self.repo_procesos.listar()

    def buscar(self, id_proceso: int) -> Proceso:
        return self.repo_procesos.buscar(id_proceso)

    def usuarios_faltantes(self, p: Proceso) -> list[Usuario]:
        usuarios_faltantes: list[Usuario] = []
        usuarios: list[Usuario] = self.repo_usuarios.listar()
        usuarios_proceso: list[Usuario] = self.usuarios(p)
        for u in usuarios:
            coincidencias: int = 0
            for up in usuarios_proceso:
                if up.cedula == u.cedula:
                    coincidencias += 1
            if coincidencias == 0:
                usuarios_faltantes.append(u)
        return usuarios_faltantes

    def usuarios(self, p: Proceso) -> list[Usuario]:
        usuarios: dict = {}
        for activo in p.activos_procesados:
            if activo.usuario.cedula not in usuarios:
                usuarios[activo.usuario.cedula] = activo.usuario
        return [usuario for usuario in usuarios.values()]

    def buscar_activo(self, id_activo: str, p: Proceso) -> ActivoProcesado:
        return self.repo_procesos.buscar_activo(id_activo, p)
