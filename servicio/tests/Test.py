import unittest
from datetime import date
from src.login import Administrador
from src.procesos.casos_uso import CrearProceso, UsuarioCasosUso, ValidarActivo
from src.procesos.entidades import Proceso
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

    def test_crear_proceso_validar_correcto(self):
        ucs = UsuarioCasosUso(repo_procesos=self.repo_procesos,
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

        val = ValidarActivo(repo_procesos=self.repo_procesos)

        estado = "CORRECTO"
        obs = ""
        revisor = Administrador(cedula_admin="123")

        for a in nuevo_proceso.activos_procesados:
            val.validar(a.id, nuevo_proceso, estado, obs, revisor)

        self.assertEqual(cant_activos, nuevo_proceso.cant_activos_revisados)
        self.assertEqual(0, nuevo_proceso.cant_observaciones)
        self.assertEqual("FINALIZADO", nuevo_proceso.estado)

    def test_crear_proceso_validar_observacion(self):
        ucs = UsuarioCasosUso(repo_procesos=self.repo_procesos,
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

        val = ValidarActivo(repo_procesos=self.repo_procesos)

        estado = "OBSERVACION"
        obs = "Mal"
        revisor = Administrador(cedula_admin="123")

        for a in nuevo_proceso.activos_procesados:
            val.validar(a.id, nuevo_proceso, estado, obs, revisor)

        self.assertEqual(cant_activos, nuevo_proceso.cant_activos_revisados)
        self.assertEqual(cant_activos, nuevo_proceso.cant_observaciones)
        self.assertEqual("FINALIZADO", nuevo_proceso.estado)
