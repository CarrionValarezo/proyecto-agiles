/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import org.json.JSONArray;

/**
 *
 * @author carri
 */
public class Conexion {

	public static String[][] getUsuarios() throws Exception {
		//Especificar la url a la cual voy a realizar la peticion
		String url = "http://localhost:5000/usuario-activo";
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
			usuarios[i][3] = String.valueOf(jArray.getJSONObject(i).getJSONArray("activos_usuario").length());
		}
		return usuarios;
	}
}
