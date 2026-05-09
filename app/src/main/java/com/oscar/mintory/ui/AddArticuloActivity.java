package com.oscar.mintory.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.oscar.mintory.R;
import com.oscar.mintory.data.api.RetrofitClient;
import com.oscar.mintory.data.api.RawgRetrofitClient;
import com.oscar.mintory.model.Articulo;
import com.oscar.mintory.model.api.BookItem;
import com.oscar.mintory.model.api.GoogleBooksResponse;
import com.oscar.mintory.model.api.GameItem;
import com.oscar.mintory.model.api.RawgResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad encargada de la búsqueda de artículos (libros y videojuegos)
 * mediante APIs externas y su posterior inserción en la base de datos local.
 */
public class AddArticuloActivity extends AppCompatActivity {

    // Componentes de la interfaz de usuario
    private TextInputEditText etBuscador;
    private TextView txtPreviewTitulo, txtPreviewAutor;
    private ImageView imgPreview;
    private Button btnBuscar, btnGuardar;
    private RadioButton radioLibro;

    // Gestión de datos y persistencia
    private ArticuloViewModel articuloViewModel;
    private Articulo articuloTemporal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_articulo);

        // Inicializamos el ViewModel para gestionar la comunicación con Room
        articuloViewModel = new ViewModelProvider(this).get(ArticuloViewModel.class);

        // Enlazamos los componentes de la vista con nuestras variables de Java
        etBuscador = findViewById(R.id.etBuscador);
        txtPreviewTitulo = findViewById(R.id.txtPreviewTitulo);
        txtPreviewAutor = findViewById(R.id.txtPreviewAutor);
        imgPreview = findViewById(R.id.imgPreview);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnGuardar = findViewById(R.id.btnGuardar);
        radioLibro = findViewById(R.id.radioLibro);

        // Configuramos el listener del botón de búsqueda
        btnBuscar.setOnClickListener(v -> {
            String consulta = etBuscador.getText().toString().trim();
            if (!consulta.isEmpty()) {
                // Determinamos qué servicio de API consultar según la opción seleccionada
                if (radioLibro.isChecked()) {
                    buscarLibro(consulta);
                } else {
                    buscarVideojuego(consulta);
                }
            } else {
                Toast.makeText(this, "Por favor, escribe un título", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuramos el listener para guardar el artículo seleccionado
        btnGuardar.setOnClickListener(v -> {
            if (articuloTemporal != null) {
                // Realizamos la inserción a través del ViewModel
                articuloViewModel.insertar(articuloTemporal);
                Toast.makeText(this, "¡Añadido a tu colección!", Toast.LENGTH_SHORT).show();

                // Finalizamos la actividad para volver al catálogo principal
                finish();
            } else {
                Toast.makeText(this, "Primero debes buscar un artículo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Realiza una petición asíncrona a la API de Google Books para obtener datos de libros.
     * @param query Título o ISBN del libro a buscar.
     */
    private void buscarLibro(String query) {
        RetrofitClient.getApi().searchBooks(query).enqueue(new Callback<GoogleBooksResponse>() {
            @Override
            public void onResponse(Call<GoogleBooksResponse> call, Response<GoogleBooksResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().items != null && !response.body().items.isEmpty()) {

                    // Extraemos la información del primer resultado obtenido
                    BookItem item = response.body().items.get(0);

                    String titulo = item.volumeInfo.title;
                    String autor = (item.volumeInfo.authors != null) ? item.volumeInfo.authors.get(0) : "Autor desconocido";
                    String fecha = (item.volumeInfo.publishedDate != null) ? item.volumeInfo.publishedDate : "N/A";
                    String imagen = (item.volumeInfo.imageLinks != null) ? item.volumeInfo.imageLinks.thumbnail.replace("http:", "https:") : "";

                    // Actualizamos los elementos visuales de la previsualización
                    txtPreviewTitulo.setText(titulo);
                    txtPreviewAutor.setText(autor);

                    if (!imagen.isEmpty()) {
                        Glide.with(AddArticuloActivity.this).load(imagen).into(imgPreview);
                    } else {
                        imgPreview.setImageResource(android.R.drawable.ic_menu_gallery);
                    }

                    // Creamos el objeto de nuestra entidad Articulo con categoría "Libro"
                    articuloTemporal = new Articulo(titulo, autor, fecha, imagen, "Hoy", 0.0f, "Pendiente", "Libro");

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
     * Realiza una petición asíncrona a la API de RAWG para obtener datos de videojuegos.
     * @param query Título del videojuego a buscar.
     */
    private void buscarVideojuego(String query) {
        // Clave de API necesaria para autenticar las peticiones en RAWG
        String apiKey = "c3741dbe6d7d4c66a82df4e756959492";

        RawgRetrofitClient.getApi().searchGames(apiKey, query).enqueue(new Callback<RawgResponse>() {
            @Override
            public void onResponse(Call<RawgResponse> call, Response<RawgResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().results != null && !response.body().results.isEmpty()) {

                    // Extraemos la información del primer videojuego de la lista
                    GameItem item = response.body().results.get(0);

                    String titulo = item.name;
                    String desarrolladora = "Estudio desconocido";
                    String fecha = (item.released != null) ? item.released : "N/A";
                    String imagen = (item.background_image != null) ? item.background_image : "";

                    // Mostramos los datos en la interfaz
                    txtPreviewTitulo.setText(titulo);
                    txtPreviewAutor.setText(desarrolladora);

                    if (!imagen.isEmpty()) {
                        Glide.with(AddArticuloActivity.this).load(imagen).into(imgPreview);
                    } else {
                        imgPreview.setImageResource(android.R.drawable.ic_menu_gallery);
                    }

                    // Creamos el objeto de nuestra entidad Articulo con categoría "Videojuego"
                    articuloTemporal = new Articulo(titulo, desarrolladora, fecha, imagen, "Hoy", 0.0f, "Pendiente", "Videojuego");

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