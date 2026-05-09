package com.oscar.mintory.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.oscar.mintory.R;
import com.oscar.mintory.data.api.RetrofitClient;
import com.oscar.mintory.model.Articulo;
import com.oscar.mintory.model.api.BookItem;
import com.oscar.mintory.model.api.GoogleBooksResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad encargada de buscar nuevos artículos a través de la API
 * y guardarlos en nuestra base de datos local Room.
 */
public class AddArticuloActivity extends AppCompatActivity {

    // Variables para los elementos de la interfaz visual
    private TextInputEditText etBuscador;
    private TextView txtPreviewTitulo, txtPreviewAutor;
    private ImageView imgPreview;
    private Button btnBuscar, btnGuardar;

    // ViewModel para comunicarnos con la base de datos siguiendo la arquitectura MVVM
    private ArticuloViewModel articuloViewModel;

    // Objeto temporal donde guardaremos los datos del libro extraído de la API antes de confirmar
    private Articulo articuloTemporal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_articulo);

        // Inicializamos el ViewModel que gestionará el guardado en la base de datos
        articuloViewModel = new ViewModelProvider(this).get(ArticuloViewModel.class);

        // Enlazamos nuestras variables de Java con los IDs correspondientes en el XML
        etBuscador = findViewById(R.id.etBuscador);
        txtPreviewTitulo = findViewById(R.id.txtPreviewTitulo);
        txtPreviewAutor = findViewById(R.id.txtPreviewAutor);
        imgPreview = findViewById(R.id.imgPreview);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Configuramos el evento de clic para el botón de "Buscar"
        btnBuscar.setOnClickListener(v -> {
            String consulta = etBuscador.getText().toString().trim();
            if (!consulta.isEmpty()) {
                // Si el campo no está vacío, ejecutamos la búsqueda en internet
                buscarLibro(consulta);
            } else {
                Toast.makeText(this, "Por favor, escribe un título", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuramos el evento de clic para el botón de "Guardar en colección"
        btnGuardar.setOnClickListener(v -> {
            // Verificamos que previamente hayamos encontrado un libro con éxito
            if (articuloTemporal != null) {
                // Insertamos el artículo en Room a través de nuestro ViewModel
                articuloViewModel.insertar(articuloTemporal);
                Toast.makeText(this, "¡Añadido a tu colección!", Toast.LENGTH_SHORT).show();

                // Cerramos esta pantalla para que el usuario vuelva automáticamente al catálogo
                finish();
            } else {
                Toast.makeText(this, "Primero debes buscar un libro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo que realiza la petición HTTP a la API de Google Books.
     * @param query El texto que hemos introducido en el buscador.
     */
    private void buscarLibro(String query) {
        // Hacemos la llamada a internet en segundo plano de forma asíncrona usando Retrofit
        RetrofitClient.getApi().searchBooks(query).enqueue(new Callback<GoogleBooksResponse>() {

            @Override
            public void onResponse(Call<GoogleBooksResponse> call, Response<GoogleBooksResponse> response) {
                // Comprobamos si la respuesta es exitosa y si Google encontró al menos un resultado
                if (response.isSuccessful() && response.body() != null && response.body().items != null && !response.body().items.isEmpty()) {

                    // Extraemos el primer libro de la lista de resultados devuelta por la API
                    BookItem item = response.body().items.get(0);

                    // Obtenemos los datos con operadores ternarios para evitar errores si algún campo viene nulo
                    String titulo = item.volumeInfo.title;
                    String autor = (item.volumeInfo.authors != null) ? item.volumeInfo.authors.get(0) : "Autor desconocido";
                    String fecha = (item.volumeInfo.publishedDate != null) ? item.volumeInfo.publishedDate : "N/A";
                    String imagen = (item.volumeInfo.imageLinks != null) ? item.volumeInfo.imageLinks.thumbnail.replace("http:", "https:") : "";

                    // Mostramos los textos extraídos en la tarjeta de nuestra interfaz visual
                    txtPreviewTitulo.setText(titulo);
                    txtPreviewAutor.setText(autor);

                    // Descargamos y cargamos la portada en el ImageView utilizando la librería Glide
                    if (!imagen.isEmpty()) {
                        Glide.with(AddArticuloActivity.this).load(imagen).into(imgPreview);
                    } else {
                        imgPreview.setImageResource(android.R.drawable.ic_menu_gallery); // Asignamos imagen por defecto si no hay portada
                    }

                    // Construimos nuestro objeto Entidad con los datos de internet y lo guardamos temporalmente.
                    // Asignamos valores por defecto para los atributos que no provee la API (valoración a 0 y estado Pendiente).
                    articuloTemporal = new Articulo(titulo, autor, fecha, imagen, "Hoy", 0.0f, "Pendiente", "Libro");

                } else {
                    Toast.makeText(AddArticuloActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoogleBooksResponse> call, Throwable t) {
                // Gestionamos el caso en el que falle la conexión a internet y avisamos al usuario
                Toast.makeText(AddArticuloActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}