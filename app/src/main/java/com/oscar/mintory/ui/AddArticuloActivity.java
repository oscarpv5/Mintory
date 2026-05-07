package com.oscar.mintory.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.oscar.mintory.R;
import com.oscar.mintory.data.api.RetrofitClient;
import com.oscar.mintory.model.api.BookItem;
import com.oscar.mintory.model.api.GoogleBooksResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArticuloActivity extends AppCompatActivity {

    private TextInputEditText etBuscador;
    private TextView txtPreviewTitulo, txtPreviewAutor;
    private ImageView imgPreview;
    private Button btnBuscar, btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_articulo);

        // Enlazar las vistas del XML con Java
        etBuscador = findViewById(R.id.etBuscador);
        txtPreviewTitulo = findViewById(R.id.txtPreviewTitulo);
        txtPreviewAutor = findViewById(R.id.txtPreviewAutor);
        imgPreview = findViewById(R.id.imgPreview);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Programar el clic del botón Buscar
        btnBuscar.setOnClickListener(v -> {
            String consulta = etBuscador.getText().toString().trim();
            if (!consulta.isEmpty()) {
                buscarLibro(consulta);
            } else {
                Toast.makeText(this, "Por favor, escribe un título", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para conectar con la API
    private void buscarLibro(String query) {
        // Hacemos la llamada a internet en segundo plano
        RetrofitClient.getApi().searchBooks(query).enqueue(new Callback<GoogleBooksResponse>() {
            @Override
            public void onResponse(Call<GoogleBooksResponse> call, Response<GoogleBooksResponse> response) {
                // Si la respuesta es exitosa y Google encontró al menos un libro
                if (response.isSuccessful() && response.body() != null && response.body().items != null && !response.body().items.isEmpty()) {

                    // Cogemos el primer libro de los resultados
                    BookItem primerLibro = response.body().items.get(0);

                    // Extraemos los datos
                    String titulo = primerLibro.volumeInfo.title;
                    String autor = "Autor desconocido";
                    if (primerLibro.volumeInfo.authors != null && !primerLibro.volumeInfo.authors.isEmpty()) {
                        autor = primerLibro.volumeInfo.authors.get(0); // Cogemos el primer autor
                    }

                    // Mostramos los textos en la pantalla
                    txtPreviewTitulo.setText(titulo);
                    txtPreviewAutor.setText(autor);

                    // Cargamos la portada con Glide
                    if (primerLibro.volumeInfo.imageLinks != null && primerLibro.volumeInfo.imageLinks.thumbnail != null) {
                        // Cambiamos "http" a "https" por seguridad
                        String imageUrl = primerLibro.volumeInfo.imageLinks.thumbnail.replace("http:", "https:");
                        Glide.with(AddArticuloActivity.this)
                                .load(imageUrl)
                                .into(imgPreview);
                    } else {
                        imgPreview.setImageResource(android.R.drawable.ic_menu_gallery); // Imagen por defecto
                    }

                } else {
                    Toast.makeText(AddArticuloActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoogleBooksResponse> call, Throwable t) {
                // Si falla el internet o la conexión
                Toast.makeText(AddArticuloActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}