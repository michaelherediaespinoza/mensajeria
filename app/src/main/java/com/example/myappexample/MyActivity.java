package com.example.myappexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappexample.ActivitisView.GetImagenesActivity;
import com.example.myappexample.ActivitisView.UpdateNombreActivity;
import com.example.myappexample.ActivitisView.UploadImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;

public class MyActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> empresaList;


    // url to get all products list
    private static String url_all_empresas = "https://maicolespinoza1996.000webhostapp.com/getMensajeEvelyn.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "tbl_evelin";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_MESAJE = "mesaje";

    // products JSONArray
    JSONArray products = null;

    ListView lista;

    EditText txtNombre, txtMensaje;
    String name,mensaje, idmensaje;
    ImageButton save;
    SwipeRefreshLayout swipeRefreshLayout;

    private int aux;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

   //     Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSms);
//        setSupportActionBar(toolbar);

        aux = 1;
        //txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        save = (ImageButton) findViewById(R.id.btnGuardar);

        // init SwipeRefreshLayout and TextView
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.silver);
        swipeRefreshLayout.setColorScheme(android.R.color.black,
                android.R.color.holo_purple,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light);

        // Hashmap para el ListView
        empresaList = new ArrayList<HashMap<String, String>>();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.listAllProducts);
/*
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String pid = ((TextView) view.findViewById(R.id.single_post_tv_nombre)).getText()
                        .toString();

                Toast.makeText(getApplicationContext(), "DATOSS.. " + pid, Toast.LENGTH_LONG);
            }
        });
*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // cancle the Visual indication of a refresh
                        swipeRefreshLayout.setRefreshing(false);

                        // Cargar los mensajes en el Background Thread
                        aux = 4;
                        empresaList.clear();
                        new LoadAllProducts().execute();
                    }
                }, 5000);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtMensaje.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Porvafor Ingrese un MENSAJE..!!", Toast.LENGTH_LONG).show();
                }else {
                    regUser();
                    txtMensaje.setText("");
                    empresaList.clear();
                    new LoadAllProducts().execute();

                }
            }
        });


    }//fin onCreate


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyActivity.this);
            switch (aux) {
                case 1:
                    pDialog.setMessage("Cargando mensajes. Por favor espere...!!");
                    dialogoShow();
                    break;
                case 2:
                    pDialog.setMessage("Enviando Mensaje...");
                    dialogoShow();
                    break;
                case 3:
                    pDialog.setMessage("Actulizando Mensajes...");
                    dialogoShow();
                    break;
                case 4:
                    pDialog.cancel();
            }
        }

        /**
         * obteniendo todos los productos
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                Log.d("All json: ", String.valueOf(success));


                if (success == 1) {

                    // successfully updated
                    Intent ix = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, ix);

                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String fecha = c.getString(TAG_FECHA);
                        String nombre = c.getString(TAG_NOMBRE);
                        String mensaje = c.getString(TAG_MESAJE);

                        // creating new HashMap
                        HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                        map.put(TAG_FECHA, fecha);
                        map.put(TAG_NOMBRE, nombre);
                        map.put(TAG_MESAJE, mensaje);

                        empresaList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            MyActivity.this,
                            empresaList,
                            R.layout.single_post,
                            new String[]{
                                    TAG_FECHA,
                                    TAG_NOMBRE,
                                    TAG_MESAJE,
                            },
                            new int[]{
                                    R.id.single_post_tv_fecha,
                                    R.id.single_post_tv_nombre,
                                    R.id.single_post_tv_mensaje

                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);
                    lista.setSelection(empresaList.size() - 1);
//                    adapter.notifyAll();

                }
            });
        }

        public void dialogoShow() {
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


    }

    public void regUser() {

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        name = sh.getString("nombreupdate", "");

        aux = 2;
        //name="My Hermosisima Evelyn";
        mensaje=txtMensaje.getText().toString();
        idmensaje="1";
        //int idmensajee = Integer.parseInt(idmensaje);
        String method="register";

        SimpleDateFormat formatoAnio = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = Calendar.getInstance().getTime();
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        Date horaActual = Calendar.getInstance().getTime();

        String fA = formatoAnio.format(fechaActual);
        String hA = formatoHora.format(horaActual);
        String faha = "Fecha: " + fA + " Hora: " + hA;

        BackgroundTask backgroundTask=new BackgroundTask(this);
        //backgroundTask.execute(method,name,password,contact,country);
        backgroundTask.execute(method,name,faha,mensaje,idmensaje);
        //finish();
        //startActivity(getIntent());
        //finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actu_nombre:
                Intent intentActuNombre = new Intent(getApplicationContext(), UpdateNombreActivity.class);
                startActivity(intentActuNombre);
                return true;
            case R.id.actu_sms:
                aux = 3;
                //Toast.makeText(this, "Actualizandoo SMS.... ", Toast.LENGTH_SHORT).show();
                empresaList.clear();
                new LoadAllProducts().execute();
                return true;
            case R.id.eliminar_sms:
                Toast.makeText(this, "Eliminar SMS.... ", Toast.LENGTH_SHORT).show();
                // addSomething();
                return true;
            case R.id.galeriaIma:
                Intent intentGalery = new Intent(getApplicationContext(), GetImagenesActivity.class);
                startActivity(intentGalery);
                return true;
            case R.id.subirImagen:
                Intent intentSubirImagen = new Intent(getApplicationContext(), UploadImageActivity.class);
                startActivity(intentSubirImagen);
                return true;
            case R.id.actu_login:
                // addSomething();
                Toast.makeText(this, "Actualizar Login.... ", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cerrarSesion:
                //  startSettings();
                Intent intentRegisterr = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentRegisterr);
                finish();
                return true;
            case R.id.salir:
                //finish();
                leerDatos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void leerDatos(){
        // Retrieving the value using its keys
        // the file name must be same in both saving
        // and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

// The value will be default as empty string
// because for the very first time
// when the app is opened,
// there is nothing to show
        String s1 = sh.getString("nombreupdate", "");

        // We can then use the data
        //name.setText(s1);
        Toast.makeText(this, "Datos Recuprados.... " + s1, Toast.LENGTH_SHORT).show();
    }
}
