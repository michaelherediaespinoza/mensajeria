package com.example.myappexample.ActivitisView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myappexample.JSONParser;
import com.example.myappexample.MyActivity;
import com.example.myappexample.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateNombreActivity extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText txtCodigo;
    EditText txtNombreUpdate;
    Button btnUpdateNombre;

    // url to create new product
    private static String url_update_product = "https://maicolespinoza1996.000webhostapp.com/updateSMSEvelyn.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nombre);

        // Edit Text
        txtCodigo = (EditText) findViewById(R.id.txtCodigoUpdate);
        txtNombreUpdate = (EditText) findViewById(R.id.txtNombreUpdate);

        // Create button
        btnUpdateNombre = (Button) findViewById(R.id.btnupdateNombre);

        // button click event
        btnUpdateNombre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new UpdateNombreActivity.CreateNewProduct().execute();

                // Storing data into SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                // Creating an Editor object
                // to edit(write to the file)
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                String nombreUpdate = txtNombreUpdate.getText().toString();

                // Storing the key and its value
                // as the data fetched from edittext
                myEdit.putString("nombreupdate", nombreUpdate);
                // Once the changes have been made,
                // we need to commit to apply those changes made,
                // otherwise, it will throw an error
                myEdit.commit();



            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateNombreActivity.this);
            pDialog.setMessage("Actulizado nombre..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            String codigo = txtCodigo.getText().toString();
            String nombreUpdate = txtNombreUpdate.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idnombre", codigo));
            params.add(new BasicNameValuePair("nombre", nombreUpdate));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);

            System.out.println("Esattus de JSON = " + json);
                 if (json == null || !json.toString().isEmpty()) {

                     // successfully created product
                     Intent i = new Intent(getApplicationContext(), MyActivity.class);
                     startActivity(i);

                     // closing this screen
                     finish();
                 }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}