from servicio.procesos.base_de_datos import ProcesoBD, ActivoBD, UsuarioBD
from servicio.procesos.entidades import Usuario, Activo


class UsuarioCasosUso:

    def __init__(self):
        self.repo_procesos: ProcesoBD = ProcesoBD()
        self.repo_activos: ActivoBD = ActivoBD()
        self.repo_usuarios: UsuarioBD = UsuarioBD()

    def buscar(self, cedula: str) -> Usuario:
        return self.repo_usuarios.buscar(cedula)

    def activos(self, u: Usuario) -> list[Activo]:
        activos: list[Activo] = self.repo_activos.listar()
        activos = [activo for activo in activos if activo.usuario.cedula == u.cedula]
        return activos

    def usuario_cant_activos(self, u: Usuario) -> tuple:
        return u, len(self.activos(u))

    def usuarios_cant_activos(self) -> list[tuple]:
        usuarios = self.repo_usuarios.listar()
        return [self.usuario_cant_activos(u) for u in usuarios]

    def procesos(self, u: Usuario):
        pass
