package com.example.myappexample;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myappexample.ActivitisView.GetImagenesActivity;
import com.example.myappexample.ActivitisView.ImagenesActivity;
import com.example.myappexample.ActivitisView.LoginDatosActivity;
import com.example.myappexample.ActivitisView.NewProductActivity;
import com.example.myappexample.ActivitisView.SmsevelynActivity;
import com.example.myappexample.ActivitisView.UpdateNombreActivity;
import com.example.myappexample.ActivitisView.UploadImageActivity;
import com.example.myappexample.ActivitisView.ViewImage;
import com.example.myappexample.utilidades.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {
    Button btnLog, btnNewCuenta;
    EditText txtEmail, txtPass;

    // TextView tx1;
    int counter = 3;

    private static String url_login = "https://maicolespinoza1996.000webhostapp.com/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLog = (Button) findViewById(R.id.btnLoginn);
        btnNewCuenta = (Button) findViewById(R.id.btnCrearNewCuenta);
        txtEmail = (EditText) findViewById(R.id.txtUsuarioo);
        txtPass = (EditText) findViewById(R.id.txtPasswordd);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario();
            }
        });

        btnNewCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intentRegisterr = new Intent(getApplicationContext(), LoginDatosActivity.class);
                Intent intentRegisterr = new Intent(getApplicationContext(), MainActivity.class);
                //Intent intentRegisterr = new Intent(getApplicationContext(), GetImagenesActivity.class);
                startActivity(intentRegisterr);
            }
        });

    }

    //https://www.youtube.com/watch?v=3dmszck6D6w
    private void validarUsuario() {
        if (txtEmail.getText().toString().equals("") || txtPass.getText().toString().equals("")) {
            Toast.makeText(this, "Falta Ingresar Información", Toast.LENGTH_LONG).show();
            //Intent intentRegisterr = new Intent(getApplicationContext(), UploadImageActivity.class);
            //startActivity(intentRegisterr);
        } else {
            String URL = Util.URL_SRV + "login.php?email=" + txtEmail.getText().toString().trim() + "&pass=" + txtPass.getText().toString().trim();
            System.out.println("urlll..!!  " + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Ssss " + response.toString());
                    System.out.println("Ssss " + response.length());
                    if (!(response.length() == 67)) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentRegisterr = new Intent(getApplicationContext(), MyActivity.class);
                        startActivity(intentRegisterr);
                    } else {
                        Toast.makeText(LoginActivity.this, "Verifique su CORREO O PASSWORD", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(LoginActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                    ;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("email", txtEmail.getText().toString());
                    parameters.put("pass", txtPass.getText().toString());
                    return parameters;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
            rQueue.add(request);

        }
    }
}


