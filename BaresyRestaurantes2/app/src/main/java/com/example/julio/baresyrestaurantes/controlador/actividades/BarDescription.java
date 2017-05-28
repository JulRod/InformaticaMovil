package com.example.julio.baresyrestaurantes.controlador.actividades;

/**
 * Created by julio on 20/4/17.
 * Actividad segundaria que es llamada desde el MainActivity al clickar sobre un item del ReciclerView
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.julio.baresyrestaurantes.controlador.fragmentos.MapDescriptionFragment;
import com.example.julio.baresyrestaurantes.R;
import com.example.julio.baresyrestaurantes.modelo.Bar;

public class BarDescription extends AppCompatActivity {
    /*Claves necesarias para parcelar los distintos contenidos para mantener la coherencia*/
    private static final String BUNDLE_FRAG = "com.example.julio.baresyrestaurantes.frag";
    private static final String SAVED_PREFERENCES = "com.example.julio.baresyrestaurantes.preferences";

    /*ATRIBUTOS*/
    //if frag==true entonces enseño descripcion si frag==false entonces enseño el mapa
    boolean frag;
    Bar bar;
    android.support.v4.app.Fragment fragmentDOS;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cambiamos la ActionBar para que sea la personalizada en "activity_bar_description"
        setContentView(R.layout.activity_bar_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Conseguimos el Bar seleccionado
        bar = MainActivity.bares.getDirectorio().get(MainActivity.bares.getSelected());

        //Recuperamos información sobre las preferencias del usuario
        //para saber si es o no uno de sus bares favoritos
        SharedPreferences sharedPref = this.getSharedPreferences(SAVED_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPref.getString(SAVED_PREFERENCES + bar.getNombre().getContent(), null) != null) {
            bar.setFavorite(true);
        }

        //Cambiamos titulo de la barra para que contenga el nombre del bar seleccionado
        setTitle(bar.getNombre().getContent());

        //Comprobamos si estamos en la descripcion o en el mapa
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //Si estamos en la descripcion creamos el fragmento del mapa y cambiamos el icono del fab
        if (!frag) {
            fragmentDOS = new MapDescriptionFragment();
            fab.setImageResource(R.drawable.ic_place_white_24px);
        } else { //Sino solo cambiamos el icono del fab
            fab.setImageResource(R.drawable.ic_description_white_24px);
        }

        //ClickListener del fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si el bar no se puede ubicar por falta de datos del JSON Imprimimos un TOAST indicandolo
                if (bar.getLat_long().getContent() != null) {
                    //Si tiene la ubicacion del bar se comprueba si estamos en mapa o en la descripcion
                    if (frag) {
                        //Si estamos en mapas enseñamos descripcion
                        //borrando el fragmento del mapa y cambiamos icono del fab
                        getSupportFragmentManager().beginTransaction().remove(fragmentDOS).commit();
                        fab.setImageResource(R.drawable.ic_place_white_24px);
                        //Cambiamos frag para mantener la coherencia
                        frag = false;
                    } else {//Si estamos en descripcion añadimos el fragmento mapa al manager y cambiamos icono del fab
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentDOS).commit();
                        fab.setImageResource(R.drawable.ic_description_white_24px);
                        //Cambiamos frag para mantener la coherencia
                        frag = true;
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.not_marker, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_FRAG, frag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Al restaurar comprobamos en que vista estamos
        //si estamos en mapa creamos un fragmento de mapa nuevo
        //lo traemos al frente
        frag = savedInstanceState.getBoolean(BUNDLE_FRAG);
        if (frag) {
            fragmentDOS = new MapDescriptionFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentDOS).commit();
            findViewById(R.id.fragment_container).bringToFront();
        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //Creamos el menu personalizado y comprobamos si el bar es favorito o no,
        //en caso de que si se cambia icono de favorito
        getMenuInflater().inflate(R.menu.menu_bar_description, menu);
        if (bar.isFavorite()) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24px));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Cargamos preferencias del usuario
        SharedPreferences sharedPref = this.getSharedPreferences(SAVED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //Comprobamos que item se selecciono y se trabaja con el
        switch (item.getItemId()) {
            //Si el item es el de favoritos:
            case R.id.favorite:
                //Y el bar ya era favorito se le quita de los favoritos y
                // se aplica el cambio en el archivo de preferencias (cambiamos icono)
                if (bar.isFavorite()) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24px));
                    bar.setFavorite(false);
                    editor.remove(SAVED_PREFERENCES + bar.getNombre().getContent());
                    editor.apply();
                //Sino se le marca como favorito y seguarda como preferencia del usuario (cambiamos icono)
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24px));
                    bar.setFavorite(true);
                    editor.putString(SAVED_PREFERENCES + bar.getNombre().getContent(), bar.getNombre().getContent());
                    editor.apply();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


