package com.example.myappexample;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by USUARIO on 11/5/2018.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context ctx;
    BackgroundTask(Context ctx)
    {
        this.ctx=ctx;
    }

    protected String doInBackground(String... params) {

        String reg_url="https://maicolespinoza1996.000webhostapp.com/insertSMSEvelyn.php";
        String method=params[0];
        if(method.equals("register"))
        {
            String name=params[1];
            String fecha=params[2];
            String mensaje=params[3];
            String idnombre=params[4];

            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("nombre","UTF-8")+"="+ URLEncoder.encode(name,"UTF-8") +
                        "&"+ URLEncoder.encode("fecha","UTF-8")+"="+ URLEncoder.encode(fecha,"UTF-8") +
                        "&"+ URLEncoder.encode("mesaje","UTF-8")+"="+ URLEncoder.encode(mensaje,"UTF-8") +
                        "&"+ URLEncoder.encode("idnombre","UTF-8")+"="+ URLEncoder.encode(idnombre,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS=httpURLConnection.getInputStream();
                IS.close();
                return "Enviado.";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx,result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}