package com.example.julio.baresyrestaurantes.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by julio on 20/4/17.
 * Clase BARES que contiene un arraylist de bares y cual es la posicion del que
 * ha sido seleccionado en el ReciclerView para poder ser usados desde otras actividades
 * sin crear dependencias entre ellas.
 */

public class Bares implements Parcelable {
    /*ATRIBUTOS*/
     ArrayList<Bar> directorio;

    static int selected;

    /*GETTERS AND SETTERS*/
    public ArrayList<Bar> getDirectorio() {
        return directorio;
    }

    public void setDirectorio(ArrayList<Bar> directorio) {
        this.directorio = directorio;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    /**
     * Constructor de BARES donde in es de tipo Parcel (Para permitir que los atributos sean parcelados)
     * @param in
     */
    protected Bares(Parcel in) {
        if (in.readByte() == 0x01) {
            directorio = new ArrayList<Bar>();
            in.readList(directorio, Bar.class.getClassLoader());
        } else {
            directorio = null;
        }
        selected = in.readInt();
    }

    /*METODOS PARCEL*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (directorio == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(directorio);
        }
        dest.writeInt(selected);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bares> CREATOR = new Parcelable.Creator<Bares>() {
        @Override
        public Bares createFromParcel(Parcel in) {
            return new Bares(in);
        }

        @Override
        public Bares[] newArray(int size) {
            return new Bares[size];
        }
    };
}
