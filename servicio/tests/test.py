import unittest
from servicio.procesos.entidades.Proceso import Proceso
from servicio.procesos.repositorios.RepoProcesos import RepoProcesos
from tests.repositorios.Conexion import Conexion


class Test(unittest.TestCase):

    def setUp(self) -> None:
        super().setUp()
        cur = Conexion().get_cursor()
        self.repo_procesos = RepoProcesos(dict_cursor=cur)

    def test_procesos(self):
        procesos: list[Proceso] = self.repo_procesos.listar()
        for p in procesos:
            print(p.to_dict())
        self.assertTrue(True)
