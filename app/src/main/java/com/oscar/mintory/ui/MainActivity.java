package com.oscar.mintory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oscar.mintory.R;
import com.oscar.mintory.model.Articulo;

public class MainActivity extends AppCompatActivity {

    private ArticuloViewModel articuloViewModel;
    private ArticuloAdapter adapter;

    // Variables para recordar el estado de los filtros
    private String textoBusquedaActual = "";
    private String categoriaActual = "Todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CONFIGURACIÓN DEL RECYCLERVIEW Y ADAPTADOR
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ArticuloAdapter();
        recyclerView.setAdapter(adapter);

        // Escuchamos los clics en las tarjetas para ir a Detalles
        adapter.setOnItemClickListener(articuloClicado -> {
            Intent intent = new Intent(MainActivity.this, DetalleArticuloActivity.class);
            intent.putExtra("articulo_extra", articuloClicado);
            startActivity(intent);
        });

        // CONFIGURACIÓN DEL VIEWMODEL Y LA BASE DE DATOS
        articuloViewModel = new ViewModelProvider(this).get(ArticuloViewModel.class);
        articuloViewModel.getTodosLosArticulos().observe(this, articulos -> {
            // Cuando la base de datos cambia, le damos todos los datos al adaptador
            adapter.setArticulos(articulos);
            // Y reaplicamos los filtros actuales por si estábamos buscando algo
            adapter.filtrar(textoBusquedaActual, categoriaActual);
        });

        // CONFIGURACIÓN DE LOS FILTROS
        SearchView searchView = findViewById(R.id.searchView);
        ChipGroup chipGroup = findViewById(R.id.chipGroupFiltros);

        // Escuchador de la barra de texto
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Actualizamos nuestra variable de texto y aplicamos el filtro general
                textoBusquedaActual = newText;
                adapter.filtrar(textoBusquedaActual, categoriaActual);
                return true;
            }
        });

        // Escuchador de los botones de categoría (Chips)
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int idSeleccionado = checkedIds.get(0);

                // Determinamos qué botón se ha pulsado basándonos en su ID
                if (idSeleccionado == R.id.chipTodos) {
                    categoriaActual = "Todos";
                } else if (idSeleccionado == R.id.chipLibros) {
                    categoriaActual = "Libro";
                } else if (idSeleccionado == R.id.chipJuegos) {
                    categoriaActual = "Videojuego";
                }

                // Aplicamos el filtro general con la nueva categoría
                adapter.filtrar(textoBusquedaActual, categoriaActual);
            }
        });

        // FUNCIONES ADICIONALES (BORRADO Y AÑADIR)
        configurarSwipeToDelete(recyclerView);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddArticuloActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Metodo extraído para mantener el código ordenado.
     * Configura el gesto de deslizar para borrar.
     */
    private void configurarSwipeToDelete(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                Articulo articuloABorrar = adapter.getArticuloEn(posicion);
                articuloViewModel.eliminar(articuloABorrar);
                Toast.makeText(MainActivity.this, "Artículo eliminado", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}