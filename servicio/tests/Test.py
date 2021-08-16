import unittest
from datetime import date
from src.login import Administrador
from src.procesos.servicio import CrearProceso, ServicioUsuario, ValidarActivo, EliminarUsuarioProcesado, \
    UsuariosPorProceso, ServicioProceso, AgregarUsuarioAlProceso
from src.procesos.entidades import Proceso, Usuario
from src.procesos.repositorios import RepoProcesos, RepoActivos, RepoUsuarios
from tests.repositorios.Conexion import Conexion


class Test(unittest.TestCase):

    def setUp(self) -> None:
        super().setUp()
        cur = Conexion().get_cursor()
        self.repo_procesos = RepoProcesos(dict_cursor=cur)
        self.repo_usuarios = RepoUsuarios(dict_cursor=cur)
        self.repo_activos = RepoActivos(dict_cursor=cur)
        self.hoy = date.today()

    def test_crear_proceso(self):
        ucs = ServicioUsuario(repo_procesos=self.repo_procesos,
                              repo_activos=self.repo_activos,
                              repo_usuarios=self.repo_usuarios)
        cp = CrearProceso(repo_procesos=self.repo_procesos, ucs=ucs)

        usuarios = self.repo_usuarios.listar()
        proceso = Proceso(nombre_proceso="Proceso Prueba",
                          fecha_proceso=self.hoy,
                          creador=Administrador(cedula_admin="123"))
        nuevo_proceso = cp.crear_proceso(usuarios=usuarios, p=proceso)
        cant_activos = len(nuevo_proceso.activos_procesados)

        self.assertEqual("CREADO", nuevo_proceso.estado)
        self.assertEqual(self.hoy, nuevo_proceso.fecha)
        self.assertTrue(cant_activos > 0)
        self.assertEqual(0, nuevo_proceso.cant_observaciones)
        return nuevo_proceso

    def test_crear_proceso_validar_correcto(self):
        nuevo_proceso = self.test_crear_proceso()
        cant_activos = len(nuevo_proceso.activos_procesados)
        val = ValidarActivo(repo_procesos=self.repo_procesos)

        estado = "CORRECTO"
        obs = ""
        revisor = Administrador(cedula_admin="123")

        for a in nuevo_proceso.activos_procesados:
            val.validar(a.id, nuevo_proceso, estado, obs, revisor)

        self.assertEqual(cant_activos, nuevo_proceso.cant_activos_validados)
        self.assertEqual(0, nuevo_proceso.cant_observaciones)
        self.assertEqual("FINALIZADO", nuevo_proceso.estado)

    def test_crear_proceso_validar_observacion(self):
        nuevo_proceso = self.test_crear_proceso()
        cant_activos = len(nuevo_proceso.activos_procesados)

        self.assertEqual("CREADO", nuevo_proceso.estado)
        self.assertEqual(self.hoy, nuevo_proceso.fecha)
        self.assertTrue(cant_activos > 0)

        val = ValidarActivo(repo_procesos=self.repo_procesos)

        estado = "OBSERVACION"
        obs = "Mal"
        revisor = Administrador(cedula_admin="123")

        for a in nuevo_proceso.activos_procesados:
            val.validar(a.id, nuevo_proceso, estado, obs, revisor)

        self.assertEqual(cant_activos, nuevo_proceso.cant_activos_validados)
        self.assertEqual(cant_activos, nuevo_proceso.cant_observaciones)
        self.assertEqual("FINALIZADO", nuevo_proceso.estado)

    def test_eliminar_agregar_usuario(self):
        proceso = self.test_crear_proceso()
        ucs = ServicioUsuario(repo_procesos=self.repo_procesos,
                              repo_activos=self.repo_activos,
                              repo_usuarios=self.repo_usuarios)
        elm = EliminarUsuarioProcesado(repo_procesos=self.repo_procesos, ucs=ucs)
        upp = UsuariosPorProceso()
        agg = AgregarUsuarioAlProceso(repo_procesos=self.repo_procesos,
                                      ucs=ucs)
        spc = ServicioProceso(repo_procesos=self.repo_procesos,
                              repo_usuarios=self.repo_usuarios)

        usuarios: list[Usuario] = upp.usuarios(proceso)
        elm.eliminar(usuarios[0], proceso)

        proceso = spc.buscar(proceso.id)
        usuarios_despues = upp.usuarios(proceso)

        self.assertEqual(len(usuarios)-1, len(usuarios_despues))

        agg.agregar(usuarios[0], proceso)

        proceso = spc.buscar(proceso.id)
        usuarios_despues = upp.usuarios(proceso)

        self.assertEqual(len(usuarios), len(usuarios_despues))

