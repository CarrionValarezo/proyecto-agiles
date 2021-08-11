from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.repositorios.RepoActivos import RepoActivos
from servicio.procesos.repositorios.RepoUsuarios import RepoUsuarios
from servicio.procesos.entidades import Usuario, Activo, Proceso


class UsuarioCasosUso:

    def __init__(self):
        self.repo_procesos: RepoProcesos = RepoProcesos()
        self.repo_activos: RepoActivos = RepoActivos()
        self.repo_usuarios: RepoUsuarios = RepoUsuarios()

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
        procesos: list[Proceso] = self.repo_procesos.listar()
        procesos_usuario: list[Proceso] = []
        for p in procesos:
            coincidencias: int = 0
            for activo in p.activos_procesados:
                if activo.usuario.cedula == u.cedula:
                    coincidencias += 1
            if coincidencias != 0:
                procesos_usuario.append(p)
        return procesos_usuario
