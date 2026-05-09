package com.oscar.mintory;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oscar.mintory.model.Articulo;
import com.oscar.mintory.ui.ArticuloAdapter;
import com.oscar.mintory.ui.ArticuloViewModel;

public class MainActivity extends AppCompatActivity {

    private ArticuloViewModel articuloViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCatalogo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Crear y asignar el Adaptador
        final ArticuloAdapter adapter = new ArticuloAdapter();
        recyclerView.setAdapter(adapter);

        // Inicializar el ViewModel y observar los datos de la Base de Datos
        articuloViewModel = new ViewModelProvider(this).get(ArticuloViewModel.class);
        articuloViewModel.getTodosLosArticulos().observe(this, articulos -> {
            // Cada vez que cambie algo en Room, este código se ejecuta y actualiza la lista
            adapter.setArticulos(articulos);
        });

        // Configuramos la funcionalidad de deslizar para borrar (Swipe to delete)
        new androidx.recyclerview.widget.ItemTouchHelper(new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0,
                androidx.recyclerview.widget.ItemTouchHelper.LEFT | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@androidx.annotation.NonNull RecyclerView recyclerView, @androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, @androidx.annotation.NonNull RecyclerView.ViewHolder target) {
                // No vamos a implementar arrastrar y soltar para reordenar, así que devolvemos false
                return false;
            }

            @Override
            public void onSwiped(@androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtenemos la posición del elemento que el usuario acaba de deslizar
                int posicion = viewHolder.getAdapterPosition();

                // Pedimos al adaptador que nos dé el artículo exacto que estaba en esa posición
                Articulo articuloABorrar = adapter.getArticuloEn(posicion);

                // Le decimos a nuestro ViewModel que lo borre definitivamente de la base de datos
                articuloViewModel.eliminar(articuloABorrar);

                // Avisamos al usuario con un mensaje breve
                Toast.makeText(MainActivity.this, "Artículo eliminado", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView); // Finalmente, acoplamos esta lógica a nuestro catálogo

        // Configurar el botón flotante (por ahora solo mostrará un aviso)
        FloatingActionButton fab = findViewById(R.id.fabAddArticulo);
        fab.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, com.oscar.mintory.ui.AddArticuloActivity.class);
            startActivity(intent);
        });
    }
}