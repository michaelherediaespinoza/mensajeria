package com.example.myappexample.ActivitisView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.myappexample.ListAdapters.ListAdapterImagenes;
import com.example.myappexample.R;
import com.example.myappexample.model.Imagenes;
import com.example.myappexample.utilidades.ClienteRest;
import com.example.myappexample.utilidades.OnTaskCompleted;
import com.example.myappexample.utilidades.Util;

import java.util.ArrayList;
import java.util.List;

public class ImagenesActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final int SOLICITUD_NOTICIAS = 1;
//    private static final int SOLICITUD_NOTICIA = 2;

    private ListAdapterImagenes noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes);


    }

    @Override
    public void onResume() {
        super.onResume();
        consultaListadoNoticias();
    }

    /**
     * Realiza la llamada al WS para consultar el listado de Categorias
     */
    protected void consultaListadoNoticias() {
        try {
            String URL = Util.URL_SRV + "getImage.php";
            System.out.println("URL..  " + URL);
            ClienteRest clienteRest = new ClienteRest(this);
            clienteRest.doGet(URL, "?id=1", "", SOLICITUD_NOTICIAS, true);
        } catch (Exception e) {
            Util.showMensaje(this, R.string.msj_error_clienrest);
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskCompleted(int idSolicitud, String result) {
        Log.i("NoticiasActivity", "" + result);
        switch (idSolicitud) {
            case SOLICITUD_NOTICIAS:
                if (result != null) {
                    try {
                        List<Imagenes> res = ClienteRest.getResults(result, Imagenes.class);
                        mostrarNoticias(res);
                    } catch (Exception e) {
                        Log.i("NoticiasActivity", "Error en carga de noticias", e);
                        Util.showMensaje(this, R.string.msj_error_clienrest_formato);
                    }
                } else
                    Util.showMensaje(this, R.string.msj_error_clienrest);
                break;
            default:
                break;
        }
    }

    public void mostrarNoticias(List<Imagenes> list) {
        ListView lista = (ListView) findViewById(R.id.lstCategorias);
        noticias = new ListAdapterImagenes(getApplicationContext(), new ArrayList<Imagenes>(list));
        lista.setAdapter(noticias);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //mostrarCategoria(position);
            }
        });

    }


}

