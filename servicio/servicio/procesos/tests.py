import json
from .aplicacion import get_usuarios_cant_activos, get_activos_por_usuario, get_usuario_por_cedula, get_activos_por_proceso, get_proceso_por_id
from .entidades import Usuario
from servicio.app import create_app
import unittest


class TestProcesos(unittest.TestCase):

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.app = create_app()
        self.app.config["MYSQL_DB"] = "agiles_pruebas"
        self.tester = self.app.test_client()
        self.usuario = "123"
        self.proceso = "1"
        self.usuario_data = [b"cedula_usuario", b"nombre_usuario", b"apellido_usuario"]
        self.proceso_data = [b"id_proceso", b"nombre_proceso", b"fecha_creacion_proceso", b"estado_proceso"]
        self.activo_data = [b"id_activo"]
        self.item_data = [b"id_item", b"nombre_item", b"descripcion_item"]

    def __endpoint_test(self, ruta):
        status_code = self.tester.get(ruta).status_code
        self.assertEqual(status_code, 200)

    def __content_type_test(self, ruta):
        content_type = self.tester.get(ruta).content_type
        self.assertEqual(content_type, "application/json")

    def __data_test(self, ruta, data):
        data_json = self.tester.get(ruta).data
        for key in data:
            print(key)
            self.assertTrue(key in data_json)

    def test_crear_proceso(self):
        dic = {
            "proceso": {"nombre_proceso": "Proceso prueba",
                        "fecha_proceso": "2021-06-06"},
            "usuarios_proceso": [{"cedula_usuario": "123"},
                                 {"cedula_usuario": "456"},
                                 {"cedula_usuario": "789"}]
        }
        respuesta = self.tester.post('/procesos',
                                     data=json.dumps(dic),
                                     content_type="application/json")
        self.assertEqual(respuesta.status_code, 200)
        self.assertTrue(b"id_proceso" in respuesta.data)

    def test_usuarios_cantidad_activos(self):
        ruta = "/usuarios/cantidad-activos"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.usuario_data)

    def test_activos_por_usuario(self):
        ruta = f"/usuarios/{self.usuario}/activos"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.activo_data + self.item_data)

    def test_procesos_por_usuario(self):
        ruta = f"/usuarios/{self.usuario}/procesos"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.proceso_data)

    def test_procesos(self):
        ruta = "/procesos"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.proceso_data)

    def test_usuarios_por_procesos(self):
        ruta = f"/procesos/{self.proceso}/usuarios"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.usuario_data)

    def test_activos_por_procesos(self):
        ruta = f"/procesos/{self.proceso}/activos"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.activo_data + self.item_data)

    def test_detalle_proceso(self):
        ruta = f"/procesos/{self.proceso}"
        print("\n\n" + ruta)
        self.__endpoint_test(ruta)
        self.__content_type_test(ruta)
        self.__data_test(ruta, self.activo_data + self.item_data + self.proceso_data + self.usuario_data)

    def test_index(self):
        self.__endpoint_test("/")

    def test_pruebas(self):
        with self.app.app_context():
            proceso = get_proceso_por_id("3")
            activos = get_activos_por_proceso(proceso)
            for activo in activos:
                print(f"ID: {activo.get_id()}, estado: {type(activo.get_estado())}")

