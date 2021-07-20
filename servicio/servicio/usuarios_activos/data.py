from servicio.app import db


class DataUsuario:

    # Devuelve una lista de diccionarios con los datos de los usuarios
    def get_usuarios(self):
        cur = db.get_cursor()
        cur.execute("SELECT ced_usu as cedula_usuario, nom_usu as nombre_usuario, ape_usu as apellido_usuario "
                    "FROM USUARIO")
        return cur.fetchall()


class DataUsuarioActivo:

    # Devuelve la cantidad de activos por usuario
    def get_cant_activos_por_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute(f'''SELECT COUNT(ID_USAC) AS cantidad_activos
                        FROM USUARIO_ACTIVO 
                        WHERE USU_USAC = {usuario.get_cedula()} ''')
        return cur.fetchone()["cantidad_activos"]
