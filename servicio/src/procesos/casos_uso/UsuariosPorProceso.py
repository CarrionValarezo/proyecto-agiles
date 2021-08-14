from src.procesos.entidades import Proceso, Usuario


class UsuariosPorProceso:

    def __init__(self):
        self.proceso: Proceso = None

    def usuarios(self, proceso: Proceso) -> list[Usuario]:
        self.proceso = proceso
        usuarios: dict = {}
        for activo in self.proceso.activos_procesados:
            if activo.usuario.cedula not in usuarios:
                if activo.estado == "OBSERVACION":
                    activo.usuario.cant_obs += 1
                activo.usuario.cant_act += 1
                usuarios[activo.usuario.cedula] = activo.usuario
            else:
                u: Usuario = usuarios[activo.usuario.cedula]
                u.cant_act += 1
                if activo.estado == "OBSERVACION":
                    u.cant_obs += 1
        return [usuario for usuario in usuarios.values()]

