package com.example.cliente_android;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ClienteServicio {
    private final String URL = "http://192.168.0.50:5000";
    private AsyncHttpClient cliente;

    public ClienteServicio(){
        this.cliente = new AsyncHttpClient();
    }

    public void getProcesos(JsonHttpResponseHandler handler){
        String url = getUrl("/procesos");
        cliente.get(url, handler);
    }

    public void getProceso(int idProceso, JsonHttpResponseHandler handler){
        String url = getUrl("/procesos/"+idProceso);
        cliente.get(url, handler);
    }

    public String getUrl(String urlRelativa){
        return URL+urlRelativa;
    }
}
