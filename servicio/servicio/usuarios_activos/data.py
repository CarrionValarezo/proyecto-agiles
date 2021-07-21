from servicio.app import db


class DataUsuario:

    # Devuelve una lista de diccionarios con los datos de los usuarios
    def get_usuarios(self):
        cur = db.get_cursor()
        cur.execute("SELECT ced_usu as cedula_usuario, nom_usu as nombre_usuario, ape_usu as apellido_usuario "
                    "FROM USUARIO")
        return cur.fetchall()

    def get_usuario_por_cedula(self, cedula):
        cur = db.get_cursor()
        cur.execute(f'''SELECT ced_usu as cedula_usuario, 
                       nom_usu as nombre_usuario,
                       ape_usu as apellido_usuario
                       FROM usuario
                       WHERE ced_usu = {cedula}''')
        return cur.fetchone()


class DataUsuarioActivo:

    # Devuelve la cantidad de activos por usuario
    def get_cant_activos_por_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute(f'''SELECT COUNT(ID_USAC) AS cantidad_activos
                        FROM USUARIO_ACTIVO 
                        WHERE USU_USAC = {usuario.get_cedula()} ''')
        return cur.fetchone()["cantidad_activos"]

    def get_activos_por_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute(f'''SELECT UA.ID_USAC AS id_pertenencia, 
                        A.ID_ACT AS id_activo,
                        A.NOM_ACT AS nombre_activo, 
                        A.DES_ACT AS descripcion_activo
                        FROM USUARIO_ACTIVO UA, ACTIVO A
                        WHERE A.ID_ACT = UA.ACT_USAC
                        AND USU_USAC = {usuario.get_cedula()}; ''')
        return cur.fetchall()


class DataProceso:

    def crear_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''insert into proceso
                        values(null,'{proceso.get_nombre()}', '{proceso.get_fecha()}');''')
        cur.connection.commit()
