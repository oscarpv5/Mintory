package com.oscar.mintory.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.oscar.mintory.R;
import com.oscar.mintory.data.api.RetrofitClient;
import com.oscar.mintory.data.api.RawgRetrofitClient;
import com.oscar.mintory.model.Articulo;
import com.oscar.mintory.model.api.BookItem;
import com.oscar.mintory.model.api.GoogleBooksResponse;
import com.oscar.mintory.model.api.GameItem;
import com.oscar.mintory.model.api.RawgResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad encargada de la búsqueda de artículos (libros y videojuegos)
 * mediante APIs externas y su posterior inserción en la base de datos local.
 * Ahora muestra múltiples resultados en un RecyclerView.
 */
public class AddArticuloActivity extends AppCompatActivity {

    // Componentes de la interfaz de usuario
    private TextInputEditText etBuscador;
    private Button btnBuscar;
    private RadioButton radioLibro;

    // Componentes para la lista de resultados
    private RecyclerView recyclerResultados;
    private ResultadoAdapter resultadoAdapter;

    // Gestión de datos y persistencia
    private ArticuloViewModel articuloViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_articulo);

        // Inicializamos el ViewModel para gestionar la comunicación con Room
        articuloViewModel = new ViewModelProvider(this).get(ArticuloViewModel.class);

        // Enlazamos los componentes de la vista con nuestras variables de Java
        etBuscador = findViewById(R.id.etBuscador);
        btnBuscar = findViewById(R.id.btnBuscar);
        radioLibro = findViewById(R.id.radioLibro);

        // --- CONFIGURACIÓN DEL RECYCLERVIEW ---
        // Enlazamos el RecyclerView del XML y le asignamos un diseño lineal
        recyclerResultados = findViewById(R.id.recyclerResultados);
        recyclerResultados.setLayoutManager(new LinearLayoutManager(this));

        // Inicializamos el adaptador y lo conectamos al RecyclerView
        resultadoAdapter = new ResultadoAdapter();
        recyclerResultados.setAdapter(resultadoAdapter);

        // Configuramos el listener del adaptador para cuando el usuario pulse "+ Añadir" en un resultado
        resultadoAdapter.setOnItemClickListener(articuloSeleccionado -> {
            // Insertamos el artículo seleccionado en la base de datos local
            articuloViewModel.insertar(articuloSeleccionado);
            Toast.makeText(this, "¡" + articuloSeleccionado.getTitulo() + " añadido!", Toast.LENGTH_SHORT).show();
            // Finalizamos la actividad para volver al catálogo principal
            finish();
        });

        // Configuramos el listener del botón de búsqueda
        btnBuscar.setOnClickListener(v -> {
            String consulta = etBuscador.getText().toString().trim();
            if (!consulta.isEmpty()) {
                // Determinamos qué servicio de API consultar según la opción seleccionada
                if (radioLibro.isChecked()) {
                    buscarLibros(consulta);
                } else {
                    buscarVideojuegos(consulta);
                }
            } else {
                Toast.makeText(this, "Por favor, escribe un título", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Realiza una petición asíncrona a la API de Google Books para obtener múltiples libros.
     * @param query Título o ISBN del libro a buscar.
     */
    private void buscarLibros(String query) {
        RetrofitClient.getApi().searchBooks(query).enqueue(new Callback<GoogleBooksResponse>() {
            @Override
            public void onResponse(Call<GoogleBooksResponse> call, Response<GoogleBooksResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().items != null) {

                    List<Articulo> listaResultados = new ArrayList<>();

                    // Recorremos la lista de resultados de la API para transformarlos en objetos Articulo
                    for (BookItem item : response.body().items) {
                        String titulo = item.volumeInfo.title;
                        String autor = (item.volumeInfo.authors != null) ? item.volumeInfo.authors.get(0) : "Autor desconocido";
                        String fecha = (item.volumeInfo.publishedDate != null) ? item.volumeInfo.publishedDate : "N/A";
                        String imagen = (item.volumeInfo.imageLinks != null) ? item.volumeInfo.imageLinks.thumbnail.replace("http:", "https:") : "";

                        // Creamos el objeto con categoría "Libro" y lo añadimos a la lista temporal
                        Articulo nuevoLibro = new Articulo(titulo, autor, fecha, imagen, "Hoy", 0.0f, "Pendiente", "Libro");
                        listaResultados.add(nuevoLibro);
                    }

                    // Enviamos la lista completa al adaptador para que la muestre
                    resultadoAdapter.setResultados(listaResultados);

                } else {
                    Toast.makeText(AddArticuloActivity.this, "No se encontraron libros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoogleBooksResponse> call, Throwable t) {
                Toast.makeText(AddArticuloActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Realiza una petición asíncrona a la API de RAWG para obtener múltiples videojuegos.
     * @param query Título del videojuego a buscar.
     */
    private void buscarVideojuegos(String query) {
        // Clave de API necesaria para autenticar las peticiones en RAWG
        String apiKey = "c3741dbe6d7d4c66a82df4e756959492";

        RawgRetrofitClient.getApi().searchGames(apiKey, query).enqueue(new Callback<RawgResponse>() {
            @Override
            public void onResponse(Call<RawgResponse> call, Response<RawgResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().results != null) {

                    List<Articulo> listaResultados = new ArrayList<>();

                    // Recorremos los resultados obtenidos de la API RAWG
                    for (GameItem item : response.body().results) {
                        String titulo = item.name;
                        String desarrolladora = "Estudio desconocido"; // RAWG requiere otra petición para la desarrolladora, usamos un genérico
                        String fecha = (item.released != null) ? item.released : "N/A";
                        String imagen = (item.background_image != null) ? item.background_image : "";

                        // Creamos el objeto con categoría "Videojuego" y lo añadimos a la lista temporal
                        Articulo nuevoJuego = new Articulo(titulo, desarrolladora, fecha, imagen, "Hoy", 0.0f, "Pendiente", "Videojuego");
                        listaResultados.add(nuevoJuego);
                    }

                    // Actualizamos el adaptador con la lista de videojuegos encontrados
                    resultadoAdapter.setResultados(listaResultados);

                } else {
                    Toast.makeText(AddArticuloActivity.this, "No se encontraron videojuegos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RawgResponse> call, Throwable t) {
                Toast.makeText(AddArticuloActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}