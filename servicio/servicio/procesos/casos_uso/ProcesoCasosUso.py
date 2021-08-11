from servicio.procesos.entidades.ActivoProcesado import ActivoProcesado
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.repositorios.RepoUsuarios import RepoUsuarios
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.entidades.Usuario import Usuario


class ProcesoCasosUso:

    def __init__(self):
        self.repo_procesos: RepoProcesos = RepoProcesos()
        self.repo_usuarios: RepoUsuarios = RepoUsuarios()

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
