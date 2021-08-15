from src.login import Administrador
from src.procesos.entidades import ActivoProcesado, Proceso, Usuario, Activo


class RepoProcesos:

    def __init__(self, dict_cursor):
        self.cur = dict_cursor

    def buscar(self, id_proceso: int) -> Proceso:
        self.cur.execute(f'''select p.id_pro as id_proceso, 
                           p.nom_pro as nombre_proceso, 
                           p.fec_cre_pro as fecha_proceso,
                           p.est_pro as estado_proceso, 
                           a.ced_adm as cedula_admin, 
                           a.nom_adm as nombre_admin, 
                           a.ape_adm as apellido_admin
                           from proceso p, administrador a
                           where p.id_pro = {id_proceso}
                           and a.ced_adm = p.ced_adm_cre_pro;''')
        params: dict = self.cur.fetchone()
        p: Proceso = Proceso(**params, creador=Administrador(**params))
        p.activos_procesados = self.listar_activos(p)
        return p

    def listar(self) -> list[Proceso]:
        self.cur.execute(f'''select p.id_pro as id_proceso, 
                           p.nom_pro as nombre_proceso, 
                           p.fec_cre_pro as fecha_proceso,
                           p.est_pro as estado_proceso, 
                           a.ced_adm as cedula_admin, 
                           a.nom_adm as nombre_admin, 
                           a.ape_adm as apellido_admin
                           from proceso p, administrador a
                           where a.ced_adm = p.ced_adm_cre_pro;''')
        data = self.cur.fetchall()
        procesos = [Proceso(**params, creador=Administrador(**params)) for params in data]
        for p in procesos:
            p.activos_procesados = self.listar_activos(p)
        return procesos

    def crear(self, proceso: Proceso) -> int:
        self.cur.execute(f'''insert into proceso
                                values(null,'{proceso.nombre}','{proceso.fecha}','CREADO', '{proceso.creador.cedula}');''')
        self.cur.connection.commit()
        self.cur.execute(f"select last_insert_id();")
        return self.cur.fetchone()['last_insert_id()']

    def eliminar_activo(self, proceso: Proceso, activo: Activo) -> bool:
        self.cur.execute(f'''delete from detalle_proceso
                            where id_pro_det = {proceso.id}
                            and id_act_det = {activo.id}
                            ''')
        self.cur.connection.commit()
        return True

    def agregar_activo(self, proceso: Proceso, activo: Activo) -> bool:
        self.cur.execute(f'''insert into detalle_proceso
                                values({proceso.id},'{activo.id}',0,'','', null);''')
        self.cur.connection.commit()
        return True

    def listar_activos(self, proceso: Proceso) -> list[ActivoProcesado]:
        self.cur.execute(f'''select a.id_act as id_activo,
                            i.nom_ite as nombre_activo, 
                            i.des_ite as descripcion_activo,
                            u.ced_usu as cedula_usuario, 
                            u.nom_usu as nombre_usuario, 
                            u.ape_usu as apellido_usuario, 
                            dp.rev_act_det as revision_activo,                    
                            dp.est_act_det as estado_revision_activo, 
                            dp.obs_act_det as observacion_revision,
                            ad.ced_adm as cedula_admin,
                            ad.nom_adm as nombre_admin,
                            ad.ape_adm as apellido_admin
                            from activo a, item i, proceso p, usuario u,
                            detalle_proceso dp left join administrador ad on dp.ced_adm_rev_det = ad.ced_adm
                            where a.id_ite_act = i.id_ite
                            and a.id_act = dp.id_act_det
                            and p.id_pro = dp.id_pro_det
                            and u.ced_usu = a.ced_usu_act
                            and p.id_pro = {proceso.id};''')
        data = self.cur.fetchall()
        return [ActivoProcesado(**params, usuario=Usuario(**params), revisor=Administrador(**params)) for params in
                data]

    def buscar_activo(self, id_activo: str, proceso: Proceso) -> ActivoProcesado:
        self.cur.execute(f'''select a.id_act as id_activo,
                                    i.nom_ite as nombre_activo, 
                                    i.des_ite as descripcion_activo,
                                    u.ced_usu as cedula_usuario, 
                                    u.nom_usu as nombre_usuario, 
                                    u.ape_usu as apellido_usuario, 
                                    dp.rev_act_det as revision_activo,                    
                                    dp.est_act_det as estado_revision_activo, 
                                    dp.obs_act_det as observacion_revision,
                                    dp.ced_adm_rev_det as cedula_admin
                                    from activo a, item i, proceso p, detalle_proceso dp, usuario u
                                    where a.id_ite_act = i.id_ite
                                    and a.id_act = dp.id_act_det
                                    and p.id_pro = dp.id_pro_det
                                    and u.ced_usu = a.ced_usu_act
                                    and a.id_act = {id_activo}
                                    and p.id_pro = {proceso.id};''')
        data = self.cur.fetchone()
        return ActivoProcesado(**data, usuario=Usuario(**data), revisor=Administrador(**data))

    def validar_activo(self, activo: ActivoProcesado, proceso: Proceso) -> None:
        self.cur.execute(f'''update detalle_proceso 
                        set rev_act_det = true, 
                            est_act_det = '{activo.estado_validacion}', 
                            obs_act_det = '{activo.observacion}', 
                            ced_adm_rev_det = '{activo.revisor.cedula}'
                        where id_pro_det = {proceso.id}
                        and id_act_det = '{activo.id}';''')
        self.cur.connection.commit()

    def actualizar(self, proceso: Proceso):
        self.cur.execute(f'''update proceso
                            set est_pro = '{proceso.estado}'
                            where id_pro = {proceso.id};''')
        self.cur.connection.commit()
