  
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import entidades.Activo;
import entidades.ActivoProcesado;
import entidades.Admin;
import entidades.Proceso;
import entidades.Usuario;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;
import java.util.Base64;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author carri
 */
public class Conexion {

	//Errores

	private HttpClient cliente;

	public Conexion() {
		this.cliente = HttpClient.newHttpClient();
	}


	public ArrayList<Usuario> getUsuarios() throws Exception {
		String url = "http://localhost:5000/usuarios/cantidad-activos";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		JSONArray jsonArray = new JSONArray(response.body());

		ArrayList<Usuario> usuarios = Usuario.fromJson(jsonArray); 
		return usuarios;
	}

	public ArrayList<Activo> getActivosUsuarios(String cedula) throws Exception {
		String url = "http://localhost:5000/usuarios/" + cedula + "/activos";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		////System.out.println(response.body());
		JSONArray jsonArray = new JSONArray(response.body());

		ArrayList<Activo> activos = Activo.fromJson(jsonArray);
		return activos; 	
	}

	public Proceso getProceso(int idProceso) throws Exception {
		String url = "http://localhost:5000/procesos/" + idProceso;

		HttpClient clienteProceso = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.header("Authorization", Admin.getAuth())
				.build();

		HttpResponse<String> response = clienteProceso.send(request, HttpResponse.BodyHandlers.ofString());

		JSONObject jsonObject = new JSONObject(response.body());

		JSONObject jsonProceso = jsonObject.getJSONObject("proceso");
		JSONArray jsonUsuarios = jsonObject.getJSONArray("usuarios");
		JSONArray jsonActivos = jsonObject.getJSONArray("activos");

		ArrayList<Usuario> usuarios = Usuario.fromJson(jsonUsuarios); 
		ArrayList<Activo> activos = ActivoProcesado.fromJson(jsonActivos); 

		Proceso p = Proceso.fromJson(jsonProceso); 
		p.setUsuarios(usuarios);
		p.setActivos(activos);

		return p;
	}

	public ArrayList<Proceso> getProcesosUsuarios(String cedula) throws Exception{
		String url = "http://localhost:5000/usuarios/" + cedula + "/procesos";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(response.body());
		JSONArray jsonArray = new JSONArray(response.body());
		
		ArrayList<Proceso> procesos = Proceso.fromJson(jsonArray);
		return procesos;
	}

	public ArrayList<Proceso> getProcesos() throws Exception{
		String url = "http://localhost:5000/procesos";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(response.body());
		JSONArray jsonArray = new JSONArray(response.body());
		
		ArrayList<Proceso> procesos = Proceso.fromJson(jsonArray);
		return procesos;
	}

	private void control(HttpResponse<String> response){
		if(response.statusCode() == 403){ 
			JOptionPane.showMessageDialog(null, "¡No tiene permisos para realizar esta acción!");
		}
	}
	

	public void eliminarUsuarioDeProceso(int id_proceso, String cedula) throws Exception{
		String url = "http://localhost:5000/procesos/"+id_proceso+"/usuarios/"+cedula;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.DELETE()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		control(response); 	
	}

	public void validarActivo(String idActivo, int idProceso, 
			String estadoActivo, String observacionActivo) throws Exception{
		String url = "http://localhost:5000/procesos/"+idProceso+"/activos/"+idActivo;
		
		JSONObject jEnviar = new JSONObject(); 

		jEnviar.put("estado_activo", estadoActivo);
		jEnviar.put("observacion_activo", observacionActivo); 

		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Content-Type", "application/json")
				.header("Authorization", Admin.getAuth())
				.PUT(BodyPublishers.ofString(jEnviar.toString()))
				.build(); 
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		////System.out.println(response.body());
	}

	public int crearProceso(JSONObject json) throws Exception {
		String url = "http://localhost:5000/procesos";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Content-Type", "application/json")
				.header("Authorization", Admin.getAuth())
				.POST(BodyPublishers.ofString(json.toString()))
				.build();

		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() == 403){
			JOptionPane.showMessageDialog(null, "¡No tiene permisos para realizar esta acción!");
			return -1;
		}else{
			JSONObject jsonResponse = new JSONObject(response.body());
			return jsonResponse.getInt("id_proceso"); 
		}
	}

	public Admin login() throws Exception{
		String url = "http://localhost:5000/admin/login";
		JSONObject jEnviar = new JSONObject(); 
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.build(); 

		System.out.println(Admin.getAuth());
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.toString());
		if(response.statusCode() == 200){ 
			return Admin._getAdmin();
		}
		return null;
	}

	public ArrayList<Usuario> getUsuariosFaltantes(int idProceso) throws Exception{
		String url = "http://localhost:5000/procesos/"+idProceso+"/usuarios-faltantes";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		JSONArray jsonArray = new JSONArray(response.body());

		ArrayList<Usuario> usuarios = Usuario.fromJson(jsonArray); 
		return usuarios;
	}

	public boolean agregarUsuario(int idProceso, String cedula) throws Exception{
		String url = "http://localhost:5000/procesos/"+idProceso+"/usuarios/"+cedula;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", Admin.getAuth())
				.POST(BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		if(response.statusCode() == 403){ 
			return false; 
		}
		return true; 
	}

}	