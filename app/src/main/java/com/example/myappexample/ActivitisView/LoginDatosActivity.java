package com.example.myappexample.ActivitisView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.example.myappexample.JSONParser;
import com.example.myappexample.LoginActivity;
import com.example.myappexample.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginDatosActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginDatosActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutApellido;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextApellido;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    //private InputValidation inputValidation;

    // url to create new product
    private static String url_create_login = "https://maicolespinoza1996.000webhostapp.com/insertDataLogin.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";


    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_datos);

        initViews();
        initListeners();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutNombre);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);


        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextNombre);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        //btnRegistrar.setOnClickListener(this);
        //btnCancelar.setOnClickListener(this);
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                // creating new product in background thread
                new CreateLoginData().execute();
                break;
            case R.id.appCompatTextViewLoginLink:
                Intent intentCancelar = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentCancelar);
                finish();
                break;
        }
    }


    /**
     * Background Async Task to Create new product
     * */
    class CreateLoginData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginDatosActivity.this);
            pDialog.setMessage("Creating data Login....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String nombre = textInputEditTextName.getText().toString();
            String email = textInputEditTextEmail.getText().toString();
            String pass = textInputEditTextPassword.getText().toString();
            String confpass = textInputEditTextConfirmPassword.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("pass", pass));
            params.add(new BasicNameValuePair("codlogin", "1"));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_login,
                    "POST", params);

            if(json == null) {
                // successfully created product
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
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
