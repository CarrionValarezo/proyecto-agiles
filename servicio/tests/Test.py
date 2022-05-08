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
        #Instanciacion de los servicios
        ucs = ServicioUsuario(repo_procesos=self.repo_procesos,
                              repo_activos=self.repo_activos,
                              repo_usuarios=self.repo_usuarios)
        cp = CrearProceso(repo_procesos=self.repo_procesos, ucs=ucs)
        
        
        #Creacion de la lista con todos los usarios en la base de datos. 
        usuarios = self.repo_usuarios.listar()
        
        #Instanciacion del nuevo proceso 
        proceso = Proceso(nombre_proceso="Proceso Prueba",
                          fecha_proceso=self.hoy,
                          creador=Administrador(cedula_admin="123"))
        
        #Creacion y almacenamiento en la base de datos mediante el servicio CrearProceso
        nuevo_proceso = cp.crear_proceso(usuarios=usuarios, p=proceso)
        
        #Obtencion de la cantidad de activos en el proceso. 
        cant_activos = len(nuevo_proceso.activos_procesados)

        #Comrpueba que el estado del nuevo proceso sea CREADO. 
        self.assertEqual("CREADO", nuevo_proceso.estado)
        
        #Comprueba la fecha de creacion
        self.assertEqual(self.hoy, nuevo_proceso.fecha)
        
        #Comprueba que existan activos en el proceso creado. 
        self.assertTrue(cant_activos > 0)
        
        #Comprueba que al estar recien creado no tenga observaciones. 
        self.assertEqual(0, nuevo_proceso.cant_observaciones)
        return nuevo_proceso

    def test_crear_proceso_validar_correcto(self):

        #Crea un nuevo proceso con el metodo de arriba xd
        nuevo_proceso = self.test_crear_proceso()

        #Obtiene la cantidad de activos que se encuentran en el proceso
        cant_activos = len(nuevo_proceso.activos_procesados)

        #Instanciacion del servicio de validacion de activos 
        val = ValidarActivo(repo_procesos=self.repo_procesos)

        #Variables para la validacion del activo
        estado = "CORRECTO"
        obs = ""
        revisor = Administrador(cedula_admin="123")

        #Realiza la validacion de todos los activos en el proceso.  
        for a in nuevo_proceso.activos_procesados:
            val.validar(a.id, nuevo_proceso, estado, obs, revisor)

        #Comrpueba que la cantidad de activos que hay en el proceso sea la misma
        #que la cantidad de activos que se encuentran validados, o sea, comrpueba
        #que todos los activos esten validados
        self.assertEqual(cant_activos, nuevo_proceso.cant_activos_validados)

        #Comrpueba que no existan activos validados con observaciones. 
        self.assertEqual(0, nuevo_proceso.cant_observaciones)

        #Comprueba que el estado del proceso sea finalizado pues ya se validaron
        #todos sus activos
        self.assertEqual("FINALIZADO", nuevo_proceso.estado)

    def test_crear_proceso_validar_observacion(self):
        #Lo mismo que el de arriba pero con observaciones
        nuevo_proceso = self.test_crear_proceso()
        cant_activos = len(nuevo_proceso.activos_procesados)

        #Validacion de estado del proceso como CREADO. 
        #Valida la fecha 
        #Valida que existan arvchivos
        #Todo esto innecesario ya se hace en el test de mas arriba
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

        #Crea un nuevo proceso para agrear y eliminar usuarios
        proceso = self.test_crear_proceso()

        #Instancia el servicio de usuarios, el de eliminar usuario de un proceso, 
        #el de agregar usuario a un proceso, y los servicios de procesos
        ucs = ServicioUsuario(repo_procesos=self.repo_procesos,
                              repo_activos=self.repo_activos,
                              repo_usuarios=self.repo_usuarios)
        elm = EliminarUsuarioProcesado(repo_procesos=self.repo_procesos, ucs=ucs)
        upp = UsuariosPorProceso()
        agg = AgregarUsuarioAlProceso(repo_procesos=self.repo_procesos,
                                      ucs=ucs)
        spc = ServicioProceso(repo_procesos=self.repo_procesos,
                              repo_usuarios=self.repo_usuarios)


        #Obtiene la lista de los usuarios que se encuentran en el proceso. 
        usuarios: list[Usuario] = upp.usuarios(proceso)

        #Elimina un usuario del proceso. 
        elm.eliminar(usuarios[0], proceso)

        #Busca el proceso por id, mal hecho esta esto 
        proceso = spc.buscar(proceso.id)
        #Obtiene la lista de los usuario que se encuentran en el proceso despues
        #de haber eliminado a uno. 
        usuarios_despues = upp.usuarios(proceso)

        #Comrpueba que la cantidad usuarios de despues de eliminar a uno sea igual
        #la cantidad de usuarios inicial en el proceso - 1.
        self.assertEqual(len(usuarios)-1, len(usuarios_despues))

        #Agrega de nuevo al usuario previamente eliminado
        agg.agregar(usuarios[0], proceso)

        #Lo mismo de arriba que esta mal hecho
        proceso = spc.buscar(proceso.id)
        usuarios_despues = upp.usuarios(proceso)

        #Al agregar de nuevo al usuario comprueba que la cantidad de usuarios en el proceso 
        #sea igual a la cantidad de usarios antes de que se eliminara uno
        self.assertEqual(len(usuarios), len(usuarios_despues))

