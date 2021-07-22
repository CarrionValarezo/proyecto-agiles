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

    def get_usuario_por_activo(self, activo):
        cur = db.get_cursor()
        cur.execute(f'''select u.ced_usu as cedula_usuario, 
                        u.nom_usu as nombre_usuario, 
                        u.ape_usu as apellido_usuario
                        from usuario_activo ua, usuario u
                        where u.ced_usu = ua.usu_usac
                        and ua.id_usac = {activo.get_id_pertenencia()};
                        ''')
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

    def get_proceso_por_id(self, id_proceso):
        cur = db.get_cursor()
        cur.execute(f'''select id_pro as id_proceso, 
                        nom_pro as nombre_proceso, 
                        fec_cre_pro as fecha_proceso
                        from proceso 
                        where id_pro = {id_proceso}''')
        return cur.fetchone()

    def crear_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''insert into proceso
                        values(null,'{proceso.get_nombre()}','{proceso.get_fecha()}');''')
        cur.connection.commit()
        cur.execute(f"select last_insert_id();")
        return cur.fetchone()['last_insert_id()']

    def agregar_activo(self, proceso, activo):
        cur = db.get_cursor()
        cur.execute(f'''insert into detalle_proceso
                        values({proceso.get_id()},'{activo.get_id_pertenencia()}',0,null);''')
        cur.connection.commit()

    def get_activos_por_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''SELECT UA.ID_USAC AS id_pertenencia,
                        A.ID_ACT AS id_activo, 
                        A.NOM_ACT AS nombre_activo, 
                        A.DES_ACT AS descripcion_activo
                        FROM USUARIO_ACTIVO UA, ACTIVO A, PROCESO P, DETALLE_PROCESO DP
                        WHERE UA.ACT_USAC = A.ID_ACT
                        and ua.id_usac = dp.id_per_det
                        and p.id_pro = dp.id_pro_det
                        AND P.ID_PRO = {proceso.get_id()};''')
        return cur.fetchall()

    def get_usuarios_por_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''select u.ced_usu as cedula_usuario, 
                        u.nom_usu as nombre_usuario, 
                        u.ape_usu as apellido_usuario
                        from usuario u, usuario_activo ua, proceso p, detalle_proceso dp
                        where u.ced_usu = ua.usu_usac
                        and ua.id_usac = dp.id_per_det
                        and p.id_pro = dp.id_pro_det
                        and p.id_pro = {proceso.get_id()};''')
        return cur.fetchall()

    def get_cantidad_activos_por_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''select count(id_pro_det) as cantidad_activos
                        from detalle_proceso 
                        where id_pro_det = {proceso.get_id()}; ''')
        return cur.fetchone()["cantidad_activos"]

    def get_procesos_por_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute(f'''select id_pro as id_proceso, 
                        nom_pro as nombre_proceso, 
                        fec_cre_pro as fecha_proceso 
                        from proceso 
                        where id_pro in (
                            select id_pro_det 
                            from detalle_proceso 
                            where id_per_det in ( select id_usac
                                from usuario_activo 
                                where usu_usac = "{usuario.get_cedula()}")); ''')
        return cur.fetchall()
