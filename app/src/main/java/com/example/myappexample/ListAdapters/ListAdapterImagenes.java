package com.example.myappexample.ListAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myappexample.R;

import com.example.myappexample.model.Imagenes;

import java.util.ArrayList;


public class ListAdapterImagenes extends ArrayAdapter<Imagenes> {

    //Contenxto de la aplicacion que relaciona al ListView y el Adpater
    private Context context;

    /**
     * Inicializacion
     *
     * @param context Contexto de la App desde la que se invoca
     * @param items   //Coleccion de objetos a presentar
     */
    public ListAdapterImagenes(Context context, ArrayList<Imagenes> items) {
        super(context, R.layout.item_imagenes, items);
        this.context = context;
    }


    /**
     * View a presentar en pantalla correspondiente a un item de ListView
     *
     * @param position    //Indice del ListItem
     * @param convertView //Contexto o contenedor de View
     * @param parent      //Contendor padre
     * @return Objeto View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_imagenes, null);
        }

        //Objeto Categoria a visualizar segun position
        Imagenes item = getItem(position);
        System.out.println("111111..  " + item.toString());
        if (item != null) {
            System.out.println("222222..  ");
            // Recupera los elementos de vista y setea en funcion de valores de objeto

            ImageView foto = (ImageView) view.findViewById(R.id.imgFoto);

            //JustifyTextView descripcionn = (JustifyTextView) view.findViewById(R.id.txtDescripcionNoticiaa);

            Bitmap bmImg = item.ByteArrayToBitmapOrImagen();

            foto.setImageBitmap(bmImg);

        }
        return view;
    }

}


