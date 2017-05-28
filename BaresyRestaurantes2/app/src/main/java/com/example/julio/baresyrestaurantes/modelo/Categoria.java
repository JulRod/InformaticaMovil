package com.example.julio.baresyrestaurantes.modelo;

/**
 * Created by julio on 19/4/17.
 * Clase Categoria que segun el JSON es un objeto distintos a todos
 * los demas que contiene el nombre y el id de la categoria
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Categoria implements Parcelable {

    /*ATRIBUTOS*/
    long id;

    @SerializedName("content")
    String categoria;

    /**
     * Constructos del objeto CATEGORIA que contiene la informacion de la categoria
     * donde in es de tipo PARCEL
     * @param in
     */
    protected Categoria(Parcel in) {
        id = in.readLong();
        categoria = in.readString();
    }
    /*METODOS PARCEL*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(categoria);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>() {
        @Override
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        @Override
        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };
    /*GETTERS AND SETTERS*/
    public String getCategoria() {
        return categoria;
    }
}
