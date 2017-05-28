package com.example.julio.baresyrestaurantes.controlador.fragmentos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julio.baresyrestaurantes.R;
import com.example.julio.baresyrestaurantes.controlador.actividades.MainActivity;
import com.example.julio.baresyrestaurantes.modelo.Bar;
import com.example.julio.baresyrestaurantes.modelo.ObjectWithContent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class BarDescriptionFragment extends Fragment {

    /*ATRIBUTOS*/
    TextView tv_descripcion;
    String UrlImagen;
    Bar bar;
    ObjectWithContent descripcion;
    ObjectWithContent image;

    Parcelable state;

    //Claves necesarias para el parcel
    private static final String BUNDLE_DESCRIPTION = "com.example.julio.baresyrestaurantes.view.description";
    private static final String BUNDLE_IMAGE = "com.example.julio.baresyrestaurantes.view.image";

    public BarDescriptionFragment newInstance() {
        return new BarDescriptionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bar_description, container, false);

        tv_descripcion = (TextView) view.findViewById(R.id.pub_descripcion);

        //Si state es distinto de null es porque estamos restaurando estado anterior del array del adaptador
        if( state == null ) {
            //como no estamos restaurando estado anterior buscamos el bar seleccionado
            bar = MainActivity.bares.getDirectorio().get(MainActivity.bares.getSelected());

            //Pintamos contenido de descripcion o string de no disponible segun haya o no informacion
            if (bar.getDescripcion().getContent() != null) {
                tv_descripcion.setText(Html.fromHtml(bar.getDescripcion().getContent()));
                descripcion = bar.getDescripcion();
            } else {
                tv_descripcion.setText(R.string.NoDescripcion);
            }
            //Descargamos la imagen o usamos una propia de imagen_not_found segun el JSON tenga imagen o no
            if (bar.getFoto() != null) {
                image = bar.getFoto();
                UrlImagen = bar.getFoto().getContent();
                Download down = new Download(getActivity());
                down.execute(UrlImagen);
            }
            else{
                ImageView IVfoto;
                IVfoto = (ImageView) view.findViewById(R.id.bar_foto);
                IVfoto.setImageResource(R.mipmap.ic_not_found);
            }
        }
        else{
            //Si estamos restaurando estado anterior ya tenemos todos los datos disponibles asique los usamos.
            if (descripcion.getContent() != null) {
                tv_descripcion.setText(Html.fromHtml(descripcion.getContent()));
            } else {
                tv_descripcion.setText(R.string.NoDescripcion);
            }

            if (image != null) {
                UrlImagen = image.getContent();
                Download down = new Download(getActivity());
                down.execute(UrlImagen);
            }
            else{
                ImageView IVfoto;
                IVfoto = (ImageView) view.findViewById(R.id.bar_foto);
                IVfoto.setImageResource(R.mipmap.ic_not_found);
            }

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Restablecemos datos al rearranque
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_DESCRIPTION, descripcion);
        bundle.putParcelable(BUNDLE_IMAGE, image);
        onCreate(bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Seguimos reestableciendo datos al rearranque controlando que no sea la primera vez que arrancamos
        try {
            descripcion = savedInstanceState.getParcelable(BUNDLE_DESCRIPTION);
            image = savedInstanceState.getParcelable(BUNDLE_IMAGE);

            state = descripcion;
        }catch(NullPointerException ne){
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardamos los datos
        if(bar == null) {
            outState.putParcelable(BUNDLE_DESCRIPTION, descripcion);
            outState.putParcelable(BUNDLE_IMAGE, image);
        }else{
            outState.putParcelable(BUNDLE_DESCRIPTION, bar.getDescripcion());
            outState.putParcelable(BUNDLE_IMAGE, bar.getFoto());
        }
    }
}

/**
 * Clase que gestiona la descarga de la imagen con asyncTask
 */
class Download extends AsyncTask<String, Void, Bitmap> {

    private static Bitmap loadedImage;
    private static ImageView IVfoto;
    FragmentActivity activity;

    Download(FragmentActivity Act){ this.activity = Act;}

    @Override
    protected Bitmap doInBackground(String... params) {
        // urls vienen de la llamada a execute(): urls[0] es la url
        return downloadFile(params[0]);
    }

    public static Bitmap downloadFile(String urlimagen) {
        URL imageUrl;
        try {
            imageUrl = new URL(urlimagen);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
            return loadedImage;
        } catch (IOException e) {
            e.printStackTrace();
           return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        IVfoto = (ImageView) activity.findViewById(R.id.bar_foto);
        IVfoto.setImageBitmap(loadedImage);
    }

}
