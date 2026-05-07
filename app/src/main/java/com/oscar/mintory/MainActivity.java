package com.oscar.mintory;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        // Configurar el botón flotante (por ahora solo mostrará un aviso)
        FloatingActionButton fab = findViewById(R.id.fabAddArticulo);
        fab.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, com.oscar.mintory.ui.AddArticuloActivity.class);
            startActivity(intent);
        });
    }
}