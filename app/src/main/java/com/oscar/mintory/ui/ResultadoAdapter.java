package com.oscar.mintory.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.oscar.mintory.R;
import com.oscar.mintory.model.Articulo;
import java.util.ArrayList;
import java.util.List;

public class ResultadoAdapter extends RecyclerView.Adapter<ResultadoAdapter.ResultadoViewHolder> {

    private List<Articulo> resultados = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAñadirClick(Articulo articulo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setResultados(List<Articulo> resultados) {
        this.resultados = resultados;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resultado, parent, false);
        return new ResultadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoViewHolder holder, int position) {
        Articulo articulo = resultados.get(position);

        holder.txtTitulo.setText(articulo.getTitulo());
        holder.txtAutor.setText(articulo.getAutorODesarrolladora());

        if (articulo.getCaratulaUrl() != null && !articulo.getCaratulaUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(articulo.getCaratulaUrl())
                    .into(holder.imgCaratula);
        } else {
            holder.imgCaratula.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.btnAñadir.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAñadirClick(articulo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultados.size();
    }

    static class ResultadoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCaratula;
        TextView txtTitulo, txtAutor;
        Button btnAñadir;

        public ResultadoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCaratula = itemView.findViewById(R.id.imgResCaratula);
            txtTitulo = itemView.findViewById(R.id.txtResTitulo);
            txtAutor = itemView.findViewById(R.id.txtResAutor);
            btnAñadir = itemView.findViewById(R.id.btnResAñadir);
        }
    }
}