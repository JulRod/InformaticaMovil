package com.example.julio.baresyrestaurantes.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by julio on 20/4/17.
 * Clase OBJECTWITHCONTENT que contiene objetos que en el JSON tienen
 * la misma estructura pero contienen informacion diferente
 */

public class ObjectWithContent implements Parcelable {
    /*ATRIBUTOS*/
    @SerializedName("content")
    String content;

    /*GETTERS AND SETTERS*/
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Contructor de OBJECTWITHCONTENT donde in es de tipo Parcel
     * @param in
     */
    protected ObjectWithContent(Parcel in) {
        content = in.readString();
    }

    /*METODOS PARCEL*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ObjectWithContent> CREATOR = new Parcelable.Creator<ObjectWithContent>() {
        @Override
        public ObjectWithContent createFromParcel(Parcel in) {
            return new ObjectWithContent(in);
        }

        @Override
        public ObjectWithContent[] newArray(int size) {
            return new ObjectWithContent[size];
        }
    };
}
