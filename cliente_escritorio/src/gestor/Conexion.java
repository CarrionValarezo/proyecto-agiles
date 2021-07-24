/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import entidades.Activo;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author carri
 */
public class Conexion {

	private HttpClient cliente;

	public Conexion() {
		this.cliente = HttpClient.newHttpClient();
	}

	public String[][] getUsuarios() throws Exception {
		//Especificar la url a la cual voy a realizar la peticion
		String url = "http://localhost:5000/usuarios/cantidad-activos";
		//Declaracion de la matriz usuarios a devolver
		String[][] usuarios = null;
		//Instanciar cliente Http
		//Construccion de la request(peticion) que realizara el cliente
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();
		//Respuesta que obtiene el cliente al realizar la peticion especificada
		//anteriormente
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		//Cuerpo de la peticion a un array de tipo JSON.
		JSONArray jArray = new JSONArray(response.body());
		//Declaracion de la matriz usuarios
		usuarios = new String[jArray.length()][4];
		//Llenar la matriz segun los datos del JSON obtenido
		for (int i = 0; i < jArray.length(); i++) {
			usuarios[i][0] = jArray.getJSONObject(i).getString("cedula_usuario");
			usuarios[i][1] = jArray.getJSONObject(i).getString("nombre_usuario");
			usuarios[i][2] = jArray.getJSONObject(i).getString("apellido_usuario");
			usuarios[i][3] = String.valueOf(jArray.getJSONObject(i).getInt("cantidad_activos_usuario"));
		}
		return usuarios;
	}

	public String[][] getActivosUsuarios(String cedula) throws Exception {
		String url = "http://localhost:5000/usuarios/" + cedula + "/activos";
		String[][] activos = null;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		JSONArray jArray = new JSONArray(response.body());
		activos = new String[jArray.length()][4];
		for (int i = 0; i < jArray.length(); i++) {
			activos[i][0] = String.valueOf(jArray.getJSONObject(i).getInt("id_activo"));
			activos[i][1] = jArray.getJSONObject(i).getString("id_item");
			activos[i][2] = jArray.getJSONObject(i).getString("nombre_item");
			activos[i][3] = jArray.getJSONObject(i).getString("descripcion_item");
		}
		return activos;
	}

	public String crearProceso(String nombre, String fecha, String[] cedulas) throws Exception {
		JSONObject jEnviar = new JSONObject();
		JSONObject jProceso = new JSONObject();
		jProceso.put("nombre_proceso", nombre);
		jProceso.put("fecha_proceso", fecha);
		jEnviar.put("proceso", jProceso);
		JSONArray jArray = new JSONArray();
		for (String cedula : cedulas) {
			System.out.println(cedula);
			JSONObject jCedula = new JSONObject();
			jCedula.put("cedula_usuario", cedula);
			jArray.put(jCedula);
		}
		jEnviar.put("usuarios_proceso", jArray);

		String url = "http://localhost:5000/procesos";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jEnviar.toString()))
				.build();

		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		JSONObject jsonResponse = new JSONObject(response.body());
		return String.valueOf(jsonResponse.getInt("id_proceso")); 
	}

	
	public Proceso getProceso(String idProceso) throws Exception {
		Usuario[] usuarios = null;
		Activo[] activos = null;
		Proceso p = null;
		String url = "http://localhost:5000/procesos/" + idProceso;

		HttpClient clienteProceso = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();

		HttpResponse<String> response = clienteProceso.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());

		JSONObject respuesta = new JSONObject(response.body());

		JSONObject jsonProceso = respuesta.getJSONObject("proceso");
		JSONArray jsonUsuarios = respuesta.getJSONArray("usuarios");
		JSONArray jsonActivos = respuesta.getJSONArray("activos");

		usuarios = new Usuario[jsonProceso.getInt("cantidad_usuarios_proceso")];
		activos = new Activo[jsonProceso.getInt("cantidad_activos_proceso")];

		for (int i = 0; i < usuarios.length; i++) {
			String cedula = jsonUsuarios.getJSONObject(i).getString("cedula_usuario");
			String nombre = jsonUsuarios.getJSONObject(i).getString("nombre_usuario");
			String apellido = jsonUsuarios.getJSONObject(i).getString("apellido_usuario");
			usuarios[i] = new Usuario(cedula, nombre, apellido);
		}

		for (int i = 0; i < activos.length; i++) {
			String idPertenencia = String.valueOf(jsonActivos.getJSONObject(i).getInt("id_activo"));
			String idActivo = jsonActivos.getJSONObject(i).getString("id_item");
			String nombreActivo = jsonActivos.getJSONObject(i).getString("nombre_item");
			String descripcionActivo = jsonActivos.getJSONObject(i).getString("descripcion_item");

			int revisionActivo = jsonActivos.getJSONObject(i).getInt("revision_activo");
			String estadoRevisionActivo = jsonActivos.getJSONObject(i).getString("estado_revision_activo");
			String observacionRevision = jsonActivos.getJSONObject(i).getString("observacion_revision");

			String cedula = jsonActivos.getJSONObject(i).getString("cedula_usuario");
			String nombre = jsonActivos.getJSONObject(i).getString("nombre_usuario");
			String apellido = jsonActivos.getJSONObject(i).getString("apellido_usuario");

			activos[i] = new Activo(idPertenencia, idActivo, nombreActivo, descripcionActivo, revisionActivo,
					estadoRevisionActivo, observacionRevision);
			activos[i].setUsuario(new Usuario(cedula, nombre, apellido));
		}

		String nombreProceso = jsonProceso.getString("nombre_proceso");
		String fechaProceso = jsonProceso.getString("fecha_creacion_proceso");
		String estadoProceso = jsonProceso.getString("estado_proceso"); 

		p = new Proceso(idProceso, nombreProceso, fechaProceso, estadoProceso, usuarios, activos);
		return p;
	}

	public String[][] getProcesosUsuarios(String cedula) throws Exception{
		String url = "http://localhost:5000/usuarios/" + cedula + "/procesos";
		String[][] procesos = null;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		JSONArray jArray = new JSONArray(response.body());
		procesos = new String[jArray.length()][3];
		for (int i = 0; i < jArray.length(); i++) {
			procesos[i][0] = String.valueOf(jArray.getJSONObject(i).getInt("id_proceso"));
			procesos[i][1] = jArray.getJSONObject(i).getString("nombre_proceso");
			procesos[i][2] = jArray.getJSONObject(i).getString("fecha_creacion_proceso");
		}
		return procesos;
	}
	public String[][] getProcesos() throws Exception{
		String url = "http://localhost:5000/procesos";
		String[][] procesos = null;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		JSONArray jArray = new JSONArray(response.body());
		procesos = new String[jArray.length()][4];
		for (int i = 0; i < jArray.length(); i++) {
			procesos[i][0] = String.valueOf(jArray.getJSONObject(i).getInt("id_proceso"));
			procesos[i][1] = jArray.getJSONObject(i).getString("nombre_proceso");
			procesos[i][2] = jArray.getJSONObject(i).getString("fecha_creacion_proceso");
			procesos[i][3] = jArray.getJSONObject(i).getString("estado_proceso");
		}
		return procesos;
	}

	public void eliminarUsuarioDeProceso(String id_proceso, String cedula) throws Exception{
		String url = "http://localhost:5000/procesos/"+id_proceso+"/usuarios/"+cedula;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.DELETE()
				.build();
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
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
				.PUT(BodyPublishers.ofString(jEnviar.toString()))
				.build(); 
		HttpResponse<String> response = this.cliente.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	}

}
