package com.example.myappexample.utilidades;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myappexample.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.example.myappexample.model.Usuario;
import com.google.gson.reflect.TypeToken;

/**
 * Clase generalizada para consumo asioncrono de WebServices RestFull
 * 
 * @author Michael Espinoza *
 */
@SuppressLint("NewApi")
public class ClienteRest extends AsyncTask<Object, Integer, String> {

	public static final String TAG = "ClienteRest";

	//Escuchador de retorno en finalizacion de consumo
	private OnTaskCompleted listener;

	//Cadena de texto de respuesta producto de consumo de WS
	private String respStr;

	//Identificador de la solicitud enviada, para posterior referencia al momento de finalizacion
	private int idSolicitud;

	/**
	 * Relacion de listener para retorno de respuesta al consumo de WS
	 * @param listener
	 */
	public ClienteRest(OnTaskCompleted listener){
		this.listener=listener;
    }

	/**
	 * Metodo para invocacion/consumo de WS por solicitud GET
	 * @param url				//URL del WS a consumir
	 * @param params			//Conjunto de parametros pa el WS a concatenarse con la URL (ej: ?texto=a&numero=18)
	 * @param idSolicitud		//Indentificador de solicitud, para identiicacion al momento de respuesta al listener
	 * @param dialog			//Estado para indicar si se desea un Cuadro de dialog de espera mientras se ejecuta el WS o se lo haria en background
	 * @throws Exception		//Control de excepcion
	 */
	public void doGet(String url, String params, String paramss, int idSolicitud, boolean dialog) throws Exception {
		if (dialog) {			//Llamada a cuadro de dialogo de Cargando
			showDialog();
		}
		this.idSolicitud = idSolicitud;
		
		if(params == null){
			params = "";
		}

		//Creacion de objeto HTTP para solicitud
		HttpGet httpGet = new HttpGet(url + params + paramss);
		httpGet.setHeader("content-type", "application/json; charset=UTF-8");
		
		try {
			//Llamda a la ejecucion en bacground de solicitud HTTP
			this.execute("GET", httpGet);			
		} catch (Exception ex) {
			Log.e(TAG, "Error en doGet " + url + params + paramss, ex);
			throw ex;
		}
	}

	public void doGet(String url, String params, int idSolicitud, boolean dialog) throws Exception {
		if (dialog) {			//Llamada a cuadro de dialogo de Cargando
			showDialog();
		}
		this.idSolicitud = idSolicitud;

		if(params == null){
			params = "";
		}

		//Creacion de objeto HTTP para solicitud
		HttpGet httpGet = new HttpGet(url + params);
		httpGet.setHeader("content-type", "application/json; charset=UTF-8");

		try {
			//Llamda a la ejecucion en bacground de solicitud HTTP
			this.execute("GET", httpGet);
		} catch (Exception ex) {
			Log.e(TAG, "Error en doGet " + url + params, ex);
			throw ex;
		}
	}

	/**
	 * Motodo para invocacion/consumo de WS por solicitud POST
	 * @param url				//URL del WS a consumir
	 * @param param			//Objeto o entidad a pasarse como parametro a metodo POST (ej: Persona)
	 * @param idSolicitud		//Indentificador de solicitud, para identiicacion al momento de respuesta al listener
	 * @param dialog			//Estado para indicar si se desea un Cuadro de dialog de espera mientras se ejecuta el WS o se lo haria en background
	 * @throws Exception		//Control de excepcion
	 */
	public void doPost(String url, Object param, int idSolicitud, boolean dialog) throws Exception {
		if (dialog) {		//Llamada a cuadro de dialogo de Cargando
			showDialog();
		}

		this.idSolicitud = idSolicitud;

		//Creacion de objeto HTTP para solicitud
		HttpPost post = new HttpPost(url);
		post.setHeader("content-type", "application/json; charset=UTF-8");

		String paramJSON = "";
		try {
			// Construimos el objeto de parametro a formato JSON para ser pasado en la solicitud POST
	        Gson gson = new Gson();
	        paramJSON = gson.toJson(param);
			StringEntity entity = new StringEntity(paramJSON, HTTP.UTF_8);
			post.setEntity(entity);

			//Llamada a la ejecucion en bacground de solicitud HTTP
			this.execute("POST", post);
		} catch (Exception ex) {
			Log.e(TAG, "Error en doPost " + url + " --> " + paramJSON, ex);
			throw ex;
		}
	}

	/**
	 * Procedimiento que convierte texto plano de respuesta (formato JSON) de WS en una entidad o objeto Java
	 * por medio de la libreria GSON de google: https://code.google.com/p/google-gson/
	 * @param respuesta			Class de respuesta esperada, util para saber a que convertir
	 * @param <T>				Tipo Genorico de clase esperada como respuesta
	 * @return					Objeto POJO de respuesta
	 */
	public static <T> T getResult(String result, Class<T> respuesta)throws JsonSyntaxException{

			GsonBuilder gsonBuilder = new GsonBuilder();
			T data = (T) gsonBuilder.create().fromJson(result, respuesta);
			 System.out.println("data.. "+ data.toString());
			return data;
	}

	/**
	 * Procedimiento que convierte texto plano de respuesta (formato JSON) de WS en una coleccion de objetos Java
	 * por medio de la libreria GSON de google: https://code.google.com/p/google-gson/
	 * @param result				Tipo Genorico de clase esperada como respuesta
	 * @return					Colecci??n de Objetos POJO de respuesta
	 */
    public static <T> List<T> getResults(String result, Class<T> clase){
        Gson gson = new Gson();
        Object [] array = (Object[])java.lang.reflect.Array.newInstance(clase, 1);
        array = gson.fromJson(result, array.getClass());
        List<T> list = new ArrayList<T>();
        for (int i=0 ; i<array.length ; i++)
            list.add((T)array[i]);
        return list;
    }

    /*
    * Author: Johnny Espinoza
    * Realizado como modo prueba, FUNCIONANDO.
     */
	public static List<Usuario> getResultUsuario(String result){
		Gson gson = new Gson();
		List<Usuario> listUsu = new ArrayList<Usuario>();
		Type listType = new TypeToken<List<Usuario>>() {}.getType();
		try {
            listUsu = gson.fromJson(result, listType);
			System.out.println("Tama??oo... " + listUsu.size());

        }catch (Exception e){
		    System.out.println("ERORRR... " + e.getMessage());
        }

		return listUsu;
	}

	private ProgressDialog dialog;

	/**
	 * Procedimiento para llamada a Dialogo para notificacion de espera mistras se solicita WS
	 */
	public void showDialog(){
		try{
			dialog = new ProgressDialog((Context) listener);
			dialog.setCanceledOnTouchOutside(false);
	        dialog.setMessage("Cargando!!...");
	        if(!dialog.isShowing()){
	            dialog.show();
	        }
		}catch(Exception e){}
	}

	/**
	 * Ejecucion de solicitud en background
	 * @param params
	 * @return
	 */
	@Override
	protected String doInBackground(Object... params) {
		HttpClient httpClient = new DefaultHttpClient();
		
		String tipo = (String)params[0];
		
		if(tipo.equals("GET")){
			Log.i(TAG, "Motodo doInBackground para solicitud GET");
			HttpResponse resp;
			try {
				HttpGet get = (HttpGet) params[1];
				resp = httpClient.execute(get);

				int codigoRespuesta = resp.getStatusLine().getStatusCode();
				if(codigoRespuesta == 200){
					respStr = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
					System.out.println("****************");
					System.out.println(respStr);
					Log.i(TAG, "Respuesta a solicitud GET " + respStr);
				}else{
					Log.i(TAG, "Codigo de respuesta a solicitud no satisfactorio, codigo retornado " + codigoRespuesta);
					cancel(true);
				}
			} catch (Exception e) {
				Log.i(TAG, "Error en proceso doInBackgroud", e);
				cancel(true);
				//Cerrada de dialogo en caso de estar abierto (control de error null)
				try{dialog.dismiss();}catch(Exception e1){}
			}

		}else if(((String)params[0]).equals("POST")){
			Log.i(TAG, "Motodo doInBackground para solicitud POST");
			HttpResponse resp;
			try {
				HttpPost post = (HttpPost)params[1];
				resp = httpClient.execute(post);
				respStr = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
				Log.i(TAG, "Respuesta en solicitud POST " + respStr);
			} catch (Exception e) {
				Log.i(TAG, "Error en proceso doInBackgroud", e);
				cancel(true);
				try{dialog.dismiss();}catch(Exception e1){}
			}
		}
		return respStr;
	}

	/**
	 * Procedimiento llamado automaticamente una vez finalizada el proceso en background, esto para
	 * notificacion a listener
	 * @param result		Respuesta desde doinBackground
	 */
	@Override
    protected void onPostExecute(String result) {
		//Notificacion a evento listener de proceso finalizado
        listener.onTaskCompleted(idSolicitud, result);
        super.onPostExecute(result);
		//Cerrada de dialogo de espera
		try{dialog.dismiss();}catch(Exception e1){}
    }

	/**
	 * Procedimiento llamado en caso de error o cancelacion de proceso en background, esto para
	 * notificacion a listener
	 * @param result		Respuesta desde doinBackground
	 */
	@Override
	protected void onCancelled(String result) {
		//Notificacion a evento listener de proceso finalizado
		listener.onTaskCompleted(idSolicitud, result);
        super.onCancelled(result);
		//Cerrada de dialogo de espera
        try{dialog.dismiss();}catch(Exception e1){}
    }

	class DateDeserializer implements JsonDeserializer<Date> {

		private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
		@Override
		public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			String date = element.getAsString();

			SimpleDateFormat formatter = null;
			formatter = new SimpleDateFormat(FORMAT);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			try {
				Date fecha = formatter.parse(date);
				return fecha;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}//end try
		}//end deserialize
	}//end DateDeserializer

}
