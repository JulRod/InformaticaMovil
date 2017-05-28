package com.example.julio.baresyrestaurantes.controlador.fragmentos;

/**
 * Created by julio on 20/4/17.
 * Fragmento del mapa
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.julio.baresyrestaurantes.R;
import com.example.julio.baresyrestaurantes.controlador.actividades.MainActivity;
import com.example.julio.baresyrestaurantes.modelo.Bar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapDescriptionFragment extends Fragment {

    /*ATRIBUTOS*/
    MapView mMapView;
    private LatLng marker_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_description, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        //Gestionamos el fragmento que contiene el mapa
        SupportMapFragment mSupportMapFragment;
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        //Recogemos los datos del bar selecionado para poder pintar la chincheta con los datos
                        Bar bar = MainActivity.bares.getDirectorio().get(MainActivity.bares.getSelected());
                        String name = bar.getNombre().getContent();
                        String lat_long = bar.getLat_long().getContent();
                        String[] array_lat_long = lat_long.split(" ");
                        double lat = Double.parseDouble(array_lat_long[0]);
                        double _long = Double.parseDouble(array_lat_long[1]);

                        //Pintamos la chincheta en la ubicacion del bar
                        marker_bar = new LatLng(lat, _long);
                        googleMap.addMarker(new MarkerOptions().position(marker_bar).title(name).snippet(lat_long));

                        //Ajustamos la camara a la chincheta
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker_bar).zoom(15.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);

                    }

                }
            });
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mMapView.onResume();
        } catch (NullPointerException ne) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mMapView.onPause();
        } catch (NullPointerException ne) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMapView.onDestroy();
        } catch (NullPointerException ne) {
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
