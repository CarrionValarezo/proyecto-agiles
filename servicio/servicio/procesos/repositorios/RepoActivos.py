from servicio.app import db
from servicio.procesos.entidades.Activo import Activo
from servicio.procesos.entidades.Usuario import Usuario


class RepoActivos:

    def __init__(self):
        self.cur = db.get_cursor()

    def buscar(self, activo_id: str) -> Activo:
        self.cur.execute(f'''select a.id_act as id_activo, 
                        i.nom_ite as nombre_activo, 
                        i.des_ite as descripcion_activo, 
                        u.ced_usu as cedula_usuario, 
                        u.nom_usu as nombre_usuario,
                        u.ape_usu as apellido_usuario
                        from activo a, item i, usuario u
                        where a.id_act = {activo_id}
                        and a.id_ite_act = i.id_ite
                        and a.ced_usu_act = u.ced_usu;''')
        params = self.cur.fetchone()
        return Activo(**params, usuario=Usuario(**params))

    def listar(self) -> list[Activo]:
        self.cur.execute(f'''select a.id_act as id_activo, 
                        i.nom_ite as nombre_activo, 
                        i.des_ite as descripcion_activo, 
                        u.ced_usu as cedula_usuario, 
                        u.nom_usu as nombre_usuario,
                        u.ape_usu as apellido_usuario
                        from activo a, item i, usuario u
                        where a.id_ite_act = i.id_ite
                        and u.ced_usu = a.ced_usu_act''')
        data = self.cur.fetchall()
        return [Activo(**params, usuario=Usuario(**params)) for params in data]
