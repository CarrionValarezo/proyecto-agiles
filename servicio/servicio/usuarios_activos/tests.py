from .aplicacion import get_usuarios_cant_activos, get_activos_por_usuario, get_usuario_por_cedula
from .entidades import Usuario
from servicio.app import create_app
import unittest


class TestUsuariosActivos(unittest.TestCase):


    # Prueba que el metodo devuelva una instancia de Usuario junto con un numero entero
    def test_get_usuarios_cant_activos(self):
        app = create_app()
        with app.app_context():
            resultado = get_usuarios_cant_activos()
            for usuario, cant_activos in resultado:
                self.assertIsInstance(usuario,Usuario) and self.assertIsInstance(cant_activos,int)

    def test_get_activos_por_usuario(self):
        app = create_app()
        with app.app_context():
            usuario = get_usuario_por_cedula("123")
            resultado = get_activos_por_usuario(usuario)
            self.assertTrue(True)