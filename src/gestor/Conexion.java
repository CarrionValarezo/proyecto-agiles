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
	
	public static String [][] getUsuarios() throws Exception{ 
		String url = "http://localhost:5000/usuario-activo"; 
		String [][] usuarios = null; 

		HttpClient client = HttpClient.newHttpClient(); 
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build(); 

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); 
		JSONArray jArray = new JSONArray(response.body()); 
		usuarios = new String[jArray.length()][4];
		for (int i = 0; i < jArray.length(); i++){ 
			usuarios[i][0] = jArray.getJSONObject(i).getString("cedula_usuario");
			usuarios[i][1] = jArray.getJSONObject(i).getString("nombre_usuario");
			usuarios[i][2] = jArray.getJSONObject(i).getString("apellido_usuario");
			usuarios[i][3] = String.valueOf(jArray.getJSONObject(i).getJSONArray("activos_usuario").length());
		}
		System.out.println(jArray.getJSONObject(1).getJSONArray("activos_usuario").length());
		return usuarios; 
	}
}
