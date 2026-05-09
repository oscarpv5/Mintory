package com.oscar.mintory.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.oscar.mintory.R;
import com.oscar.mintory.model.Articulo;

public class DetalleArticuloActivity extends AppCompatActivity {

    private ImageView imgCaratula;
    private TextView txtTitulo, txtAutor;
    private RatingBar ratingBar;
    private Spinner spinnerEstado;
    private Button btnGuardar;

    private ArticuloViewModel articuloViewModel;
    private Articulo articuloActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulo);

        // Inicializamos el ViewModel para poder realizar la actualización
        articuloViewModel = new ViewModelProvider(this).get(ArticuloViewModel.class);

        // Enlazamos los componentes de la interfaz
        imgCaratula = findViewById(R.id.imgDetalleCaratula);
        txtTitulo = findViewById(R.id.txtDetalleTitulo);
        txtAutor = findViewById(R.id.txtDetalleAutor);
        ratingBar = findViewById(R.id.ratingBarValoracion);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnGuardar = findViewById(R.id.btnGuardarCambios);

        // Configuramos el Spinner con las opciones de estado que definimos en strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.estados_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // RECUPERAMOS EL ARTÍCULO: Extraemos el objeto que enviamos desde MainActivity
        articuloActual = (Articulo) getIntent().getSerializableExtra("articulo_extra");

        if (articuloActual != null) {
            rellenarDatos();
        }

        // Programamos el guardado de cambios
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    /**
     * Rellenamos los campos de la pantalla con la información actual del artículo.
     */
    private void rellenarDatos() {
        txtTitulo.setText(articuloActual.getTitulo());
        txtAutor.setText(articuloActual.getAutorODesarrolladora());
        ratingBar.setRating(articuloActual.getValoracion());

        // Cargamos la imagen con Glide
        Glide.with(this)
                .load(articuloActual.getCaratulaUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imgCaratula);

        // Seleccionamos en el Spinner el estado que ya tiene el artículo
        String estadoActual = articuloActual.getEstado();
        ArrayAdapter adapter = (ArrayAdapter) spinnerEstado.getAdapter();
        int position = adapter.getPosition(estadoActual);
        spinnerEstado.setSelection(position);
    }

    /**
     * Recogemos los nuevos datos introducidos por el usuario y actualizamos la base de datos.
     */
    private void guardarCambios() {
        // Actualizamos el objeto con los nuevos valores de la interfaz
        articuloActual.setValoracion(ratingBar.getRating());
        articuloActual.setEstado(spinnerEstado.getSelectedItem().toString());

        // Llamamos al ViewModel para que Room haga el UPDATE en la base de datos
        articuloViewModel.actualizar(articuloActual);

        Toast.makeText(this, "¡Cambios guardados correctamente!", Toast.LENGTH_SHORT).show();

        // Cerramos la actividad y volvemos al catálogo
        finish();
    }
}