from servicio.procesos.data import DataActivo, DataUsuario, DataProceso
from servicio.procesos.entidades import Usuario, Item, Activo, Proceso


class Aplicacion:

    def __init__(self):
        self.repo_usuarios = DataUsuario()
        self.repo_activos = DataActivo()
        self.repo_procesos = DataProceso()

    def get_usuarios_cant_activos(self):
        usuarios_activos = []
        for data_usuario in self.repo_usuarios.get_usuarios():
            usuario = Usuario(**data_usuario)
            usuarios_activos.append((usuario, self.repo_activos.get_cant_activos_por_usuario(usuario)))
        return usuarios_activos

    def get_usuario_por_cedula(self, cedula):
        usuario = Usuario(**self.repo_usuarios.get_usuario_por_cedula(cedula))
        return usuario

    def get_activos_por_usuario(self, usuario):
        activos_usuario = []
        for data_activo in self.repo_activos.get_activos_por_usuario(usuario):
            activos_usuario.append(Activo(**data_activo, item=Item(**data_activo)))
        return activos_usuario

    def crear_proceso(self, proceso, usuarios, admin):
        nuevo_proceso = Proceso(**proceso)
        activos = []
        for usuario in usuarios:
            activos = activos + self.get_activos_por_usuario(usuario)
        nuevo_proceso.set_id(self.repo_procesos.crear_proceso(nuevo_proceso, admin))
        for activo in activos:
            self.repo_procesos.agregar_activo(nuevo_proceso, activo)
        return nuevo_proceso

    def asignar_estado_proceso(self, proceso, activos):
        activos_revisados = 0
        if proceso.get_estado() == "CREADO":
            return "CREADO"
        elif proceso.get_estado() == "FINALIZADO":
            return "FINALIZADO"
        elif proceso.get_estado() == "INICIADO":
            for activo in activos:
                if activo.get_revision():
                    activos_revisados += 1
            if len(activos) == activos_revisados:
                return "FINALIZADO"
        return "INICIADO"

    def get_proceso_por_id(self, id_proceso):
        proceso = Proceso(**self.repo_procesos.get_proceso_por_id(id_proceso))
        proceso.set_cant_observaciones(self.repo_procesos.get_cant_activos_observacion(proceso))
        return proceso

    def get_activos_por_proceso(self, proceso):
        activos = [Activo(**data_activo, item=Item(**data_activo)) for data_activo in
                   self.repo_procesos.get_activos_por_proceso(proceso)]
        estado_actualizado = self.asignar_estado_proceso(proceso, activos)
        if proceso.get_estado() != estado_actualizado:
            proceso.set_estado(estado_actualizado)
            self.repo_procesos.actualizar_estado(proceso)
            proceso.set_estado(estado_actualizado)
        return activos

    def get_usuario_por_activo(self, activo):
        usuario = Usuario(**self.repo_usuarios.get_usuario_por_activo(activo))
        return usuario

    # Devulve los usuarios que estan registrados en un proceso
    def get_usuarios_por_proceso(self, proceso):
        repo_proceso = DataProceso()
        usuarios = {}
        for usuario in self.repo_procesos.get_usuarios_por_proceso(proceso):
            if usuario["cedula_usuario"] not in usuarios:
                usuarios[usuario["cedula_usuario"]] = {
                    "cedula_usuario": usuario["cedula_usuario"],
                    "nombre_usuario": usuario["nombre_usuario"],
                    "apellido_usuario": usuario["apellido_usuario"]
                }

        lista_usuarios = [Usuario(**data_usuario) for data_usuario in usuarios.values()]
        return lista_usuarios

    def get_cantidad_activos_proceso(self, proceso):
        return DataProceso().get_cantidad_activos_por_proceso(proceso)

    def get_procesos_por_usuario(self, usuario):
        repo_procesos = DataProceso()
        procesos = [Proceso(**data_procesos) for data_procesos in self.repo_procesos.get_procesos_por_usuario(usuario)]
        for proceso in procesos:
            proceso.set_cant_observaciones(self.repo_procesos.get_cant_activos_observacion_usuario(proceso, usuario))
        return procesos

    def get_procesos(self):
        procesos = [Proceso(**data_procesos) for data_procesos in self.repo_procesos.get_procesos()]
        for proceso in procesos:
            proceso.set_cant_observaciones(self.repo_procesos.get_cant_activos_observacion(proceso))
        return procesos

    def eliminar_usuario_de_proceso(self, usuario, proceso):
        self.repo_procesos.eliminar_usuario_de_proceso(usuario, proceso)
        return None

    def agregar_usuario_proceso(self, usuario, proceso):
        activos = [Activo(**data) for data in self.repo_activos.get_activos_por_usuario(usuario)]
        for activo in activos:
            self.repo_procesos.agregar_activo(proceso, activo)

    def get_activo_por_id(self, id_activo):
        activo = Activo(**self.repo_activos.get_activo_por_id(id_activo))
        return activo

    def validar_activo(self, activo, proceso, estado, observacion, admin):
        self.repo_procesos.validar_activo(activo, proceso, estado, observacion, admin)
        if proceso.get_estado() == "CREADO":
            proceso.set_estado("INICIADO")
            DataProceso().actualizar_estado(proceso)

    def get_cant_activos_observacion_usuario(self, usuario, proceso):
        return self.repo_procesos.get_cant_activos_observacion_usuario(proceso, usuario)

    def get_cant_activos_usuario(self, usuario):
        return self.repo_activos.get_activos_por_usuario(usuario)

    def get_usuarios_faltantes(self, proceso):
        usuarios = []
        for data in self.repo_procesos.get_usuarios_faltante(proceso):
            usuario = Usuario(**data)
            cant_activos = self.repo_activos.get_cant_activos_por_usuario(usuario)
            usuarios.append((usuario, cant_activos))
        return usuarios
