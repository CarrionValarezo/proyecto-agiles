/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import java.io.IOException;
import java.net.URI;
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

	public static String[][] getUsuarios() throws Exception {
		//Especificar la url a la cual voy a realizar la peticion
		String url = "http://localhost:5000/usuario-cant-activos";
		//Declaracion de la matriz usuarios a devolver
		String[][] usuarios = null;
		//Instanciar cliente Http
		HttpClient client = HttpClient.newHttpClient();
		//Construccion de la request(peticion) que realizara el cliente
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();
		//Respuesta que obtiene el cliente al realizar la peticion especificada
		//anteriormente
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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

	public static String[][] getActivosUsuarios(String cedula) throws Exception {
		//Especificar la url a la cual voy a realizar la peticion
		String url = "http://localhost:5000/activos-usuario/" + cedula;
		//Declaracion de la matriz usuarios a devolver
		String[][] activos = null;
		//Instanciar cliente Http
		HttpClient client = HttpClient.newHttpClient();
		//Construccion de la request(peticion) que realizara el cliente
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build();
		//Respuesta que obtiene el cliente al realizar la peticion especificada
		//anteriormente
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		//Cuerpo de la peticion a un array de tipo JSON.
		JSONArray jArray = new JSONArray(response.body());
		//Declaracion de la matriz usuarios
		activos = new String[jArray.length()][4];
		//Llenar la matriz segun los datos del JSON obtenido
		for (int i = 0; i < jArray.length(); i++) {
			activos[i][0] = String.valueOf(jArray.getJSONObject(i).getInt("id_pertenencia"));
			activos[i][1] = jArray.getJSONObject(i).getString("id_activo");
			activos[i][2] = jArray.getJSONObject(i).getString("nombre_activo");
			activos[i][3] = jArray.getJSONObject(i).getString("descripcion_activo");
		}
		return activos;
	}

	public void crearProceso(String nombre, String fecha, String[] cedulas) throws Exception {
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
		String url = "http://localhost:5000/proceso";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jEnviar.toString()))
				.build();

		System.out.println(jEnviar.toString());
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		JSONObject jsonResponse = new JSONObject(response.body());
		System.out.println(response.body());

	}

}
