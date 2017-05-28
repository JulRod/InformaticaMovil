package com.example.julio.baresyrestaurantes.controlador.actividades;

/**
 * Created by julio on 20/4/17.
 * Actividad principal que será llamada al ejecutar.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.julio.baresyrestaurantes.R;
import com.example.julio.baresyrestaurantes.controlador.RecyclerItemClickListener;
import com.example.julio.baresyrestaurantes.controlador.RecyclerViewAdapter;
import com.example.julio.baresyrestaurantes.modelo.Bar;
import com.example.julio.baresyrestaurantes.modelo.Bares;
import com.example.julio.baresyrestaurantes.modelo.Categoria;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Claves necesarias para las preferencias y los parcels
    public final String URL = "http://datos.gijon.es/doc/informacion/bares-cafes.json";
    private static final String BUNDLE_RECYCLER_LAYOUT = "com.example.julio.baresyrestaurantes.recycler.layout";
    private static final String BUNDLE_RECYCLER_ARRAY = "com.example.julio.baresyrestaurantes.recycler.array";
    private static final String SAVED_PREFERENCES = "com.example.julio.baresyrestaurantes.preferences";

    /*ATRIBUTOS*/
    public static ArrayList<Bar> arrayBares = null;
    public static ArrayList<Bar> arrayCopia = null;
    public static Bares bares;

    private RecyclerView recyclerView = null;
    private RecyclerViewAdapter adapter = null;
    //booleano que será true en caso de estár en la lista de favoritos
    private boolean pulsado = false;

    private RecyclerView.LayoutManager manager = null;
    Parcelable state;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Si estamos online (disponemos de conexion) entonces descargamos el JSON,
        // lo parceamos y lo incluimos en el recyclerview
        // teniendo siempre en cuenta si se habia guardado o no el estado anterior
        if (isOnline()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject datos = response.getJSONObject("directorios");
                        Gson gson = new Gson();

                        bares = gson.fromJson(datos.toString(), Bares.class);

                        //Si arraybares es distinto de null es porque estoy restaurando estado anterior del array de bares
                        if (arrayBares == null) {
                           arrayBares = bares.getDirectorio();
                        }

                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_courses);

                        manager = new LinearLayoutManager(MainActivity.this);

                        //Si state es distinto de null es porque estamos restaurando estado anterior del array del adaptador
                        if(state != null) {
                            manager.onRestoreInstanceState(state);
                            arrayCopia = savedInstanceState.getParcelableArrayList(BUNDLE_RECYCLER_ARRAY);
                            adapter = new RecyclerViewAdapter(MainActivity.this, arrayCopia);
                        }
                        else{
                            adapter = new RecyclerViewAdapter(MainActivity.this, arrayBares);
                        }

                        recyclerView.setLayoutManager(manager);

                        recyclerView.setAdapter(adapter);

                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener
                                .OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent miIntent = new Intent(recyclerView.getContext(), BarDescription.class);
                                startActivity(miIntent);
                                bares.setDirectorio(adapter.getBares());
                                bares.setSelected(position);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                /////////////////////////// long Click ///////////////////////////
                            }
                        }));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_red),Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Si no hay red no se habrá guardado ningun estado controlamos el error
        try {
            state = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }catch(NullPointerException ne){}
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Si no hay red no se habrá guardado ningun array ni ningun layout controlamos el error
        try {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
            outState.putParcelableArrayList(BUNDLE_RECYCLER_ARRAY, adapter.getBares());
        }catch(NullPointerException ne){}

    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        //Creamos la accion del searchView
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Si se escribe algo se filtra el contenido y se cambia el adaptador del recyclerView
                //Teniendo en cuenta que estamos en favoritos o en bares.
                ArrayList<Bar> filteredModelList;
                if(!pulsado) {
                    filteredModelList = filter(arrayBares, newText);
                }else{
                    arrayCopia = new ArrayList<>();
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SAVED_PREFERENCES,Context.MODE_PRIVATE);
                    for (Bar bar : arrayBares) {
                        if (sharedPref.getString(SAVED_PREFERENCES + bar.getNombre().getContent(), null) != null) {
                            arrayCopia.add(bar);
                        }
                    }
                    filteredModelList = filter(arrayCopia, newText);
                }
                adapter.setBares(filteredModelList);
                adapter.notifyDataSetChanged();
                return true;
            }

            private ArrayList<Bar> filter(ArrayList<Bar> bares, String query) {
                query = query.toLowerCase();

                final ArrayList<Bar> filteredModelList = new ArrayList<>();
                //Filtro que filtra por nombre y por categoria
                // (lo que permite una busqueda por nombre y por categoria)
                for (Bar b : bares) {
                    final String nombre = b.getNombre().getContent().toLowerCase();
                    final Categoria[] categorias = b.getCategorias();
                    String cat = "";
                    for(Categoria c : categorias){
                        cat = cat + " " + c.getCategoria();
                    }
                    cat = cat.toLowerCase();
                    if (nombre.contains(query) || cat.contains(query)) {
                        filteredModelList.add(b);
                    }
                }
                return filteredModelList;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPref = this.getSharedPreferences(SAVED_PREFERENCES,Context.MODE_PRIVATE);
        //Controlamos preferencias del usuario para filtrar los favoritos o todos los bares
        switch (item.getItemId()) {
            case R.id.view_favorite:
                //Si el icono favorito a sido pulsado pintamos o los favoritos o los bares dependiendo
                // de si ha sido pulsado o repulsado
                if (!pulsado) {
                    arrayCopia = new ArrayList<Bar>();
                    for (Bar bar : arrayBares) {
                        if (sharedPref.getString(SAVED_PREFERENCES + bar.getNombre().getContent(), null) != null) {
                            arrayCopia.add(bar);
                        }
                    }
                    adapter.setBares(arrayCopia);
                    adapter.notifyDataSetChanged();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24px));
                    setTitle(R.string.favorites);
                    pulsado = true;
                }else{
                    adapter.setBares(arrayBares);
                    adapter.notifyDataSetChanged();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24px));
                    pulsado = false;
                    setTitle(R.string.app_name);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
