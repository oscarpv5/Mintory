package com.oscar.mintory.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.oscar.mintory.R;
import com.oscar.mintory.model.Articulo;
import java.util.ArrayList;
import java.util.List;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloHolder> {

    private List<Articulo> listaArticulos = new ArrayList<>();

    @NonNull
    @Override
    public ArticuloHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí convertimos en código el diseño XML que creamos en el paso anterior
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_articulo, parent, false);
        return new ArticuloHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloHolder holder, int position) {
        Articulo articuloActual = listaArticulos.get(position);

        // Rellenamos los textos
        holder.txtTitulo.setText(articuloActual.getTitulo());
        holder.txtAutor.setText(articuloActual.getAutorODesarrolladora());
        holder.txtEstado.setText(articuloActual.getEstado());

        // Usamos la librería Glide para descargar y mostrar la carátula de forma eficiente
        Glide.with(holder.itemView.getContext())
                .load(articuloActual.getCaratulaUrl())
                // Si falla o está cargando, mostramos un icono por defecto
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgCaratula);
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    // Método vital para actualizar la lista cuando la Base de Datos cambie
    public void setArticulos(List<Articulo> articulos) {
        this.listaArticulos = articulos;
        notifyDataSetChanged();
    }

    // Clase interna que guarda las referencias a las vistas del XML para no buscarlas constantemente
    class ArticuloHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;
        private TextView txtAutor;
        private TextView txtEstado;
        private ImageView imgCaratula;

        public ArticuloHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtAutor = itemView.findViewById(R.id.txtAutor);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgCaratula = itemView.findViewById(R.id.imgCaratula);
        }
    }
}