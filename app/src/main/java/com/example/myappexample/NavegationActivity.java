package com.example.myappexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.myappexample.ListAdapters.ListAdapterCategorias;
import com.example.myappexample.model.Usuario;
import com.example.myappexample.utilidades.ClienteRest;
import com.example.myappexample.utilidades.OnTaskCompleted;
import com.example.myappexample.utilidades.Util;

import java.util.ArrayList;
import java.util.List;

public class NavegationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnTaskCompleted {


    private static final int SOLICITUD_CATEGORIAS = 1;
    private static final int SOLICITUD_CATEGORIA = 2;

    private ListAdapterCategorias categorias;

    private AlertDialog.Builder rater;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(), "valoracion " + rating, Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegation, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "action bar clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == android.R.id.home) {
            Toast.makeText(getApplicationContext(), "action bar clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.camnombre) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.cambiopassword) {
            Toast.makeText(getApplicationContext(), "action bar clicked", Toast.LENGTH_LONG).show();

        } else if (id == R.id.salir) {
            Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentRegister);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // .......-------------------------------------------------------------------------------------..........................


    @Override
    public void onResume() {
        super.onResume();
        consultaListadoCategorias();
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
                Toast.makeText(getApplicationContext(), "DATOSS.. " + cat.getNombre(), Toast.LENGTH_LONG).show();
                showRatingDialog();
            }
        });
    }

    private void mostrarCategoria(int position) {
        Usuario cat = categorias.getItem(position);
        Toast.makeText(getApplicationContext(), "DATOSS.. " + cat.getNombre(), Toast.LENGTH_LONG).show();
//        consultaCategoria(cat.getCodigo());
    }

    public void showRatingDialog()
    {
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        // get values and then displayed in a toast
        String totalStars = "Total Stars:: " + ratingBar.getNumStars();
        String rating = "Rating :: " + ratingBar.getRating();
        Toast.makeText(getApplicationContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();

    }
}
