from servicio.procesos.casos_uso.UsuarioCasosUso import UsuarioCasosUso
from servicio.procesos.entidades.Activo import Activo
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.entidades.Usuario import Usuario


class AgregarUsuarioProcesado:

    def __init__(self):
        self.repo_procesos: RepoProcesos = RepoProcesos()

    def agregar(self, u: Usuario, p: Proceso):
        ucs = UsuarioCasosUso()
        activos: list[Activo] = ucs.activos(u)
        for activo in activos:
            self.repo_procesos.agregar_activo(p, activo)
