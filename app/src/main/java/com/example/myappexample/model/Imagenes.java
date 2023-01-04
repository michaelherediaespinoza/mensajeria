package com.example.myappexample.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

public class Imagenes  implements Serializable {

    private byte foto[];

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Bitmap ByteArrayToBitmapOrImagen()
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(this.foto);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        // System.out.println("bitmap ..  "+ bitmap);
        return bitmap;
    }
}
