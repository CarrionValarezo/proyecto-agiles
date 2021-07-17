from servicio.app import db


class DataUsuario:

    def get_usuarios(self):
        cur = db.get_cursor()
        cur.execute("SELECT ced_usu as cedula_usuario, nom_usu as nombre_usuario, ape_usu as apellido_usuario FROM USUARIO")
        return cur.fetchall()

class DataActivo:

    def get_activos_by_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute( "select a.id_act as id_activo, a.nom_act as nombre_activo, a.des_act as descripcion_activo"
                     " FROM USUARIO_ACTIVO ua, ACTIVO a"
                     f" WHERE ua.usu_usac = {usuario.get_cedula()} AND ua.act_usac = a.id_act")
        return cur.fetchall()

