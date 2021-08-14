from flask import Blueprint, jsonify
from flask_app.app import auth
from src.login.Administrador import Administrador
from src.login.RepoAdministrador import RepoAdministrador

login_blueprint = Blueprint("login_blueprint", __name__)


@auth.verify_password
def verify_password(username, password) -> Administrador:
    repo_admins: RepoAdministrador = RepoAdministrador()
    admin: Administrador = Administrador(cedula_admin=username, password_admin=password)
    if repo_admins.existe(admin):
        return repo_admins.buscar(username)


@auth.get_user_roles
def get_user_roles(admin: Administrador):
    repo_admins: RepoAdministrador = RepoAdministrador()
    admin.rol = repo_admins.get_rol(admin)
    return admin.rol


@login_blueprint.route('/admin/login')
@auth.login_required
def login():
    respuesta: dict = {"permiso": True}
    return jsonify(respuesta)