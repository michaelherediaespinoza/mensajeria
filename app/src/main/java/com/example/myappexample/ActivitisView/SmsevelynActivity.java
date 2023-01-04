package com.example.myappexample.ActivitisView;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myappexample.ListAdapters.ListAdapterCategorias;
import com.example.myappexample.R;
import com.example.myappexample.model.Usuario;
import com.example.myappexample.utilidades.ClienteRest;
import com.example.myappexample.utilidades.OnTaskCompleted;
import com.example.myappexample.utilidades.Util;

import java.util.ArrayList;
import java.util.List;

public class SmsevelynActivity extends AppCompatActivity implements OnTaskCompleted {


    private static final int SOLICITUD_CATEGORIAS = 1;
    private static final int SOLICITUD_CATEGORIA = 2;

    private ListAdapterCategorias categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsevelyn);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            consultaListadoCategorias();
        } catch (Exception e) {
            System.out.println("Errorrrrrr " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "action bar clicked", Toast.LENGTH_LONG);
            return true;
        }
        if (id == android.R.id.home) {
            Toast.makeText(getApplicationContext(), "action bar clicked", Toast.LENGTH_LONG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Realiza la llamada al WS para consultar el listado de Categorias
     */
    protected void consultaListadoCategorias() {
        try {
            String URL = Util.URL_SRV + "getsms.php";
            System.out.println("Linkk.. " + URL);
            ClienteRest clienteRest = new ClienteRest(this);
            clienteRest.doGet(URL, "", "", SOLICITUD_CATEGORIAS, true);
        } catch (Exception e) {
            Util.showMensaje(this, R.string.msj_error_clienrest);
            e.printStackTrace();
        }
    }

    /**
     * Realiza la llamada al WS para consultar el listado de Categorias
     */
    protected void consultaCategoria(int id) {
        try {
            String URL = Util.URL_SRV + "pedidos/categoriaid";
            ClienteRest clienteRest = new ClienteRest(this);
            // clienteRest.doGet(URL, "?id="+id, SOLICITUD_CATEGORIA, true);
        } catch (Exception e) {
            Util.showMensaje(this, R.string.msj_error_clienrest);
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskCompleted(int idSolicitud, String result) {
        Log.i("MainActivity", "" + result);
        switch (idSolicitud) {
            case SOLICITUD_CATEGORIAS:
                if (result != null) {
                    try {
                        System.out.println("usuarios 1111: " + result.toString());
                        List<Usuario> res = ClienteRest.getResults(result, Usuario.class);
                        //List<Usuario> res =  ClienteRest.getResultUsuario(result);
                        mostrarCategorias(res);
                        System.out.println("usuarios 2222: " + res.toString());
                        Toast.makeText(getApplicationContext(), "DATOSS.. " + res.toString(), Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        Util.showMensaje(this, e.getMessage());
                        Log.i("MainActivity", "Error en carga de categorias", e);
                        Util.showMensaje(this, R.string.msj_error_clienrest_formato);
                    }
                } else
                    Util.showMensaje(this, R.string.msj_error_clienrest);
                break;
            case SOLICITUD_CATEGORIA:
                if (result != null) {
                    try {
                        Usuario res = ClienteRest.getResult(result, Usuario.class);
                        Util.showMensaje(this, res.toString());
                    } catch (Exception e) {
                        Log.i("MainActivity", "Error en carga de categoria por ID", e);
                        Util.showMensaje(this, R.string.msj_error_clienrest_formato);
                    }
                } else
                    Util.showMensaje(this, R.string.msj_error_clienrest);
                break;
            default:
                break;
        }
    }

    public void mostrarCategorias(List<Usuario> list) {
        ListView lista = (ListView) findViewById(R.id.lstCategorias);
        categorias = new ListAdapterCategorias(getApplicationContext(), new ArrayList<Usuario>(list));
        lista.setAdapter(categorias);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // mostrarCategoria(position);
                Usuario cat = categorias.getItem(position);
                Toast.makeText(getApplicationContext(), "DATOSS.. " + cat.getNombre(), Toast.LENGTH_LONG);
            }
        });
    }

    private void mostrarCategoria(int position) {
        Usuario cat = categorias.getItem(position);
        Toast.makeText(getApplicationContext(), "DATOSS.. " + cat.getNombre(), Toast.LENGTH_LONG);
//        consultaCategoria(cat.getCodigo());
    }

}
