package com.example.julio.baresyrestaurantes.controlador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.julio.baresyrestaurantes.R;
import com.example.julio.baresyrestaurantes.modelo.Bar;

import java.util.ArrayList;

/**
 * Created by julio on 20/4/17.
 * RecyclerViewAdapter que gestiona la adaptacion de los datos a los distintos items de la lista
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {

    public LayoutInflater inflater = null;
    ArrayList<Bar> bares;

    public ArrayList<Bar> getBares() {
        return bares;
    }

    public void setBares(ArrayList<Bar> bares) {
        this.bares = bares;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView direccion;

        public ViewHolder(View itemView) {
            super(itemView);

            Name= (TextView) itemView.findViewById(R.id.nameTextView);
            direccion= (TextView) itemView.findViewById(R.id.direccionTextView);
        }
    }

    public RecyclerViewAdapter(Context context, ArrayList<Bar> bares) {

        if (context == null || bares == null ) {
            throw new IllegalArgumentException();
        }

        this.bares = bares;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = inflater.inflate(R.layout.list_item_bar, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.Name.setText(bares.get(position).getNombre().getContent());
        String categorias = "";
        if(bares.get(position).getCategorias() != null) {
            //Gestionamos todas las categorias para poder ponerlas en el mismo string en caso de que existiensen
            for (int i = 0; i < bares.get(position).getCategorias().length; i++) {
                categorias += bares.get(position).getCategorias()[i].getCategoria() + " ";
            }
        }
        holder.direccion.setText(categorias);
    }

    @Override
    public int getItemCount() {
        return bares.size();
    }


}
