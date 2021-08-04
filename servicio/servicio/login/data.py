from servicio.app import db
from servicio.login.entidades import Administrador


class DataAdmin:

    def __init__(self):
        self.cur = db.get_cursor()

    def existe(self, admin: Administrador) -> bool:
        self.cur.execute(f'''select exists(select * from administrador
                            where ced_adm = '{admin.get_cedula()}'
                            and pas_adm = '{admin.get_password()}') as existe''')
        return self.cur.fetchone()['existe']

    def get_rol(self, admin: Administrador) -> str:
        self.cur.execute(f'''select rol_adm as rol_admin from administrador where ced_adm = {admin.get_cedula()}''')
        return self.cur.fetchone()['rol_admin']