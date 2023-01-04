package com.example.myappexample.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myappexample.model.Usuario;

import java.util.ArrayList;
import com.example.myappexample.R;

/**
 * Created by UPS
 */
public class ListAdapterCategorias extends ArrayAdapter<Usuario> {

    //Contenxto de la aplicacion que relaciona al ListView y el Adpater
    private Context context;

    /**
     * Inicializacion
     *
     * @param context Contexto de la App desde la que se invoca
     * @param items   //Coleccion de objetos a presentar
     */
    public ListAdapterCategorias(Context context, ArrayList<Usuario> items) {
        super(context, R.layout.item_usuario, items);
        this.context = context;
    }

    /**
     * View a presentar en pantalla correspondiente a un item de ListView
     *
     * @param position    //Indice del ListItem
     * @param convertView //Contexto o contenedor de View
     * @param parent      //Contendor padre
     * @return  Objeto View
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_usuario, null);
        }

        //Objeto Categoria a visualizar segun position
        Usuario item = getItem(position);
        if (item != null) {
            // Recupera los elementos de vista y setea en funcion de valores de objeto
            TextView titulo = (TextView) view.findViewById(R.id.txtNombreUsu);
            TextView apellido = (TextView) view.findViewById(R.id.txtApellidoUsu);

            if (titulo != null && apellido != null ) {
                apellido.setText(item.getFecha());
                titulo.setText(item.getNombre());
            }
        }
        return view;
    }
}
