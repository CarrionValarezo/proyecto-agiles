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
                        from activo a, usuario u
                        where u.ced_usu = a.ced_usu_act
                        and a.id_act = {activo.get_id()};
                        ''')
        return cur.fetchone()


class DataActivo:

    # Devuelve la cantidad de activos por usuario
    def get_cant_activos_por_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute(f'''SELECT COUNT(id_act) AS cantidad_activos
                        FROM activo 
                        WHERE ced_USU_act = {usuario.get_cedula()} ''')
        return cur.fetchone()["cantidad_activos"]

    def get_activos_por_usuario(self, usuario):
        cur = db.get_cursor()
        cur.execute(f'''SELECT a.id_act AS id_activo, 
                        i.ID_ite AS id_item,
                        i.NOM_ite AS nombre_item, 
                        i.DES_ite AS descripcion_item
                        FROM activo A, item i
                        WHERE i.ID_ite = A.id_ite_act
                        AND ced_USU_act = {usuario.get_cedula()}; ''')
        return cur.fetchall()

    def get_activo_por_id(self, id_activo):
        cur = db.get_cursor()
        cur.execute(f'''SELECT a.id_act AS id_activo, 
                        i.ID_ite AS id_item,
                        i.NOM_ite AS nombre_item, 
                        i.DES_ite AS descripcion_item
                        FROM activo A, item i
                        WHERE a.ID_act = {id_activo}''')
        return cur.fetchone()


class DataProceso:

    def get_proceso_por_id(self, id_proceso):
        cur = db.get_cursor()
        cur.execute(f'''select id_pro as id_proceso, 
                        nom_pro as nombre_proceso, 
                        fec_cre_pro as fecha_proceso, 
                        est_pro as estado_proceso
                        from proceso 
                        where id_pro = {id_proceso}''')
        return cur.fetchone()

    def crear_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''insert into proceso
                        values(null,'{proceso.get_nombre()}','{proceso.get_fecha()}','CREADO');''')
        cur.connection.commit()
        cur.execute(f"select last_insert_id();")
        return cur.fetchone()['last_insert_id()']

    def agregar_activo(self, proceso, activo):
        cur = db.get_cursor()
        cur.execute(f'''insert into detalle_proceso
                        values({proceso.get_id()},'{activo.get_id()}',0,'','');''')
        cur.connection.commit()

    def get_activos_por_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''SELECT a.ID_act AS id_activo,
                        dp.rev_act_det as revision_activo,                    
                        dp.est_act_det as estado_revision_activo, 
                        dp.obs_act_det as observacion_revision,
                        i.ID_ite AS id_item, 
                        i.NOM_ite AS nombre_item, 
                        i.DES_ite AS descripcion_item
                        FROM activo A, item i, PROCESO P, DETALLE_PROCESO DP
                        WHERE A.id_ite_act = i.ID_ite
                        and a.id_act = dp.id_act_det
                        and p.id_pro = dp.id_pro_det
                        AND P.ID_PRO = {proceso.get_id()};''')
        return cur.fetchall()

    def get_usuarios_por_proceso(self, proceso):
        cur = db.get_cursor()
        cur.execute(f'''select u.ced_usu as cedula_usuario, 
                        u.nom_usu as nombre_usuario, 
                        u.ape_usu as apellido_usuario
                        from usuario u, activo a, proceso p, detalle_proceso dp
                        where u.ced_usu = a.ced_usu_act
                        and a.id_act = dp.id_act_det
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
                            where id_act_det in ( select id_act
                                from activo 
                                where ced_usu_act = "{usuario.get_cedula()}")); ''')
        return cur.fetchall()

    def get_procesos(self):
        cur = db.get_cursor()
        cur.execute(f'''select id_pro as id_proceso, 
                        nom_pro as nombre_proceso, 
                        fec_cre_pro as fecha_proceso, 
                        est_pro as estado_proceso
                        from proceso; 
                    ''')
        return cur.fetchall()

    def eliminar_usuario_de_proceso(self, usuario, proceso):
        cur = db.get_cursor()
        cur.execute(f''' delete from detalle_proceso
                        where id_pro_det = {proceso.get_id()} 
                        and id_act_det in ( select id_act 
                            from activo
                            where ced_usu_act = '{usuario.get_cedula()}');''')
        cur.connection.commit()

    def validar_activo(self, activo, proceso, estado, observacion):
        cur = db.get_cursor()
        cur.execute(f'''update detalle_proceso 
                        set rev_act_det = true, 
                            est_act_det = '{estado}', 
                            obs_act_det = '{observacion}'
                        where id_pro_det = {proceso.get_id()}
                        and id_act_det = '{activo.get_id()}';''')
        cur.connection.commit()
