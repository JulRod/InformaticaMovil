package com.example.julio.baresyrestaurantes.modelo;

/**
 * Created by julio on 20/4/17.
 * Objeto tipo Bar que permitirá cargar el JSON correctamente con Volley.
 * Tiene muchos de los atributos que tienen un bar en el JSON.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Bar implements Parcelable {
    /*ATRIBUTOS*/
    boolean favorite;

    @SerializedName("categorias")
    Categorias categorias;

    @SerializedName("descripcion")
    ObjectWithContent descripcion;

    @SerializedName("foto")
    ObjectWithContent foto;

    @SerializedName("horario")
    Object horario;

    @SerializedName("localizacion")
    ObjectWithContent lat_long;

    @SerializedName("nombre")
    ObjectWithContent nombre;

    @SerializedName("telefono.content")
    int telefono;

    @SerializedName("url")
    String webGijon;

    @SerializedName("correo-electronico")
    String contacto;

    /*GETTERS AND SETTERS*/

    public Categoria[] getCategorias() {
        return categorias.categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public ObjectWithContent getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(ObjectWithContent descripcion) {
        this.descripcion = descripcion;
    }

    public ObjectWithContent getFoto() {
        return foto;
    }

    public void setFoto(ObjectWithContent foto) {
        this.foto = foto;
    }

    public Object getHorario() {
        return horario;
    }

    public void setHorario(Object horario) {
        this.horario = horario;
    }

    public ObjectWithContent getLat_long() {
        return lat_long;
    }

    public void setLat_long(ObjectWithContent lat_long) {
        this.lat_long = lat_long;
    }

    public ObjectWithContent getNombre() {
        return nombre;
    }

    public void setNombre(ObjectWithContent nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getWebGijon() {
        return webGijon;
    }

    public void setWebGijon(String webGijon) {
        this.webGijon = webGijon;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Constructor del tipo BAR que prepara todos los atributos para poder ser Parcelados
     * Donde el parametro es un Parcel
     * @param in
     */
    protected Bar(Parcel in) {
        //categorias.categorias = (Categoria[]) in.readValue(Categorias.class.getClassLoader());
        //categorias.categorias = in.createTypedArray(CATCREATOR);
        categorias = new Categorias();
        descripcion = new ObjectWithContent(in);
        foto = new ObjectWithContent(in);
        horario = new Object();
        lat_long = new ObjectWithContent(in);
        nombre = new ObjectWithContent(in);
        telefono = 0;
        webGijon = "";
        contacto = "";
        favorite = in.readByte() != 0x00;
        if(in != null || in.dataSize() != 0) {
            //categorias = (Categorias) in.readValue(Categorias.class.getClassLoader());
            //categorias = new Categorias();
            categorias.categorias = in.createTypedArray(CATCREATOR);
            favorite = in.readByte() != 0x00;
            descripcion = (ObjectWithContent) in.readValue(ObjectWithContent.class.getClassLoader());
            foto = (ObjectWithContent) in.readValue(ObjectWithContent.class.getClassLoader());
            horario = (Object) in.readValue(Object.class.getClassLoader());
            lat_long = (ObjectWithContent) in.readValue(ObjectWithContent.class.getClassLoader());
            nombre = (ObjectWithContent) in.readValue(ObjectWithContent.class.getClassLoader());
            telefono = in.readInt();
            webGijon = in.readString();
            contacto = in.readString();
        }


    }
    /*METODOS PARCEL*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (favorite ? 0x01 : 0x00));
        dest.writeValue(categorias.categorias);
        dest.writeValue(descripcion);
        dest.writeValue(foto);
        dest.writeValue(horario);
        dest.writeValue(lat_long);
        dest.writeValue(nombre);
        dest.writeInt(telefono);
        dest.writeString(webGijon);
        dest.writeString(contacto);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bar> CREATOR = new Parcelable.Creator<Bar>() {
        @Override
        public Bar createFromParcel(Parcel in) {
            return new Bar(in);
        }

        @Override
        public Bar[] newArray(int size) {
            return new Bar[size];
        }
    };

    public static final Parcelable.Creator<Categoria> CATCREATOR = new Parcelable.Creator<Categoria>() {
        @Override
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        @Override
        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };
}
