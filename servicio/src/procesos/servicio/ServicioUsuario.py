from src.procesos.repositorios import RepoProcesos, RepoUsuarios, RepoActivos
from src.procesos.entidades import Usuario, Activo, Proceso


class ServicioUsuario:

    def __init__(self, repo_procesos: RepoProcesos, repo_usuarios: RepoUsuarios, repo_activos: RepoActivos):
        self.repo_procesos: RepoProcesos = repo_procesos
        self.repo_activos: RepoActivos = repo_activos
        self.repo_usuarios: RepoUsuarios = repo_usuarios

    def buscar(self, cedula: str) -> Usuario:
        return self.repo_usuarios.buscar(cedula)

    def activos(self, u: Usuario) -> list[Activo]:
        activos: list[Activo] = self.repo_activos.listar()
        return [a for a in activos if a.usuario.cedula == u.cedula]

    def usuario_cant_activos(self, u: Usuario) -> tuple:
        return u, len(self.activos(u))

    def usuarios_cant_activos(self) -> list[tuple]:
        usuarios: list[Usuario] = self.repo_usuarios.listar()
        return [self.usuario_cant_activos(u) for u in usuarios]

    def procesos(self, u: Usuario) -> list[Proceso]:
        return self.repo_procesos.listar_por_usuario(u)
