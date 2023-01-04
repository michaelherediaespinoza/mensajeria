package com.example.myappexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myappexample.Adapters.CityAdapter;
import com.example.myappexample.model.City;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EliminarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        ListView lvCities = (ListView) findViewById(R.id.lv_cities);
        ArrayList<City> citiesAvaiable = new ArrayList<City>();

        try {
            // Llamamos al servicio web para recuperar los datos
            HttpGet httpGet = new HttpGet("https://maicolespinoza1996.000webhostapp.com/cities.php");
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
            InputStream iStream = buffer.getContent();
            String aux = "";
            BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                aux += line;
            }

            // Parseamos la respuesta obtenida del servidor a un objeto JSON
            JSONObject jsonObject = new JSONObject(aux);
            JSONArray cities = jsonObject.getJSONArray("cities");

            Toast.makeText(getApplicationContext(), "Tamaño "+ cities.toString(), Toast.LENGTH_LONG ).show();

            // Recorremos el array con los elementos cities
            for (int i = 0; i < cities.length(); i++) {
                JSONObject city = cities.getJSONObject(i);

                // Creamos el objeto City
                City c = new City(city.getInt("id"),
                                city.getString("name"));

                // Almacenamos el objeto en el array que hemos creado anteriormente
                citiesAvaiable.add(c);
                Toast.makeText(getApplicationContext(), "Tamaño "+ citiesAvaiable.size(), Toast.LENGTH_LONG ).show();
            }
        } catch (Exception e) {
//            Log.e("WebService", e.getMessage());
            Toast.makeText(getApplicationContext(), "WebService "+ e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        // Creamos el objeto CityAdapter y lo asignamos al ListView
        CityAdapter cityAdapter = new CityAdapter(this, citiesAvaiable);
        lvCities.setAdapter(cityAdapter);
    }
}
