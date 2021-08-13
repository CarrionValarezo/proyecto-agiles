from servicio.app import db
from servicio.procesos.entidades.Usuario import Usuario


class RepoUsuarios:

    def __init__(self, dict_cursor):
        self.cur = dict_cursor

    def buscar(self, cedula: str) -> Usuario:
        self.cur.execute(f'''select ced_usu as cedula_usuario, 
                            nom_usu as nombre_usuario,
                            ape_usu as apellido_usuario 
                            from usuario
                            where ced_usu = {cedula}''')
        return Usuario(**self.cur.fetchone())

    def listar(self) -> list[Usuario]:
        self.cur.execute(f'''select ced_usu as cedula_usuario, 
                            nom_usu as nombre_usuario, 
                            ape_usu as apellido_usuario
                            from usuario;''')
        return [Usuario(**data) for data in self.cur.fetchall()]
