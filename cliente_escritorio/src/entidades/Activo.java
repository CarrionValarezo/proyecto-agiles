/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author carri
 */
public class Activo {

	protected String id, idItem, nomItem, desItem;
	protected Usuario usuario;

	public Activo(String idPertinencia, String idItem, String nomItem, String desItem) {
		this.id = idPertinencia;
		this.idItem = idItem;
		this.nomItem = nomItem;
		this.desItem = desItem;
	}

	public static Activo fromJson(JSONObject json) {
		Activo a = new Activo();
		try {
			a.id = json.getString("id_activo");
			a.idItem = json.getString("id_item");
			a.nomItem = json.getString("nombre_item");
			a.desItem = json.getString("descripcion_item");
		} catch (JSONException e) {
		}
		return a;
	}

	public static ArrayList<Activo> fromJson(JSONArray jsonArray) {
		ArrayList<Activo> activos = new ArrayList<Activo>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject activoJson = null;
			try {
				activoJson = jsonArray.getJSONObject(i);
				Activo activo = Activo.fromJson(activoJson);
				if (activo != null) {
					activos.add(activo);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return activos;
	}

	public static String[][] matriz(ArrayList<Activo> activos) {
		int contador = 0;
		if (activos != null) {
			String[][] datosActivos = new String[activos.size()][];
			for (Activo activo : activos) {
				datosActivos[contador] = new String[]{activo.id, activo.idItem, activo.nomItem, activo.desItem};
				contador++;
			}
			return datosActivos;
		}
		return null; 
	}

	public Activo() {
	}

	public String getId() {
		return id;
	}

	public String idItem() {
		return idItem;
	}

	public String nombreItem() {
		return nomItem;
	}

	public String descripcionItem() {
		return desItem;
	}

	public String id() {
		return id;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	@Override
	public String toString() {
		return this.id + " " + this.idItem + " " + this.nomItem;
	}
}
