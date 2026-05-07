package com.oscar.mintory.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.oscar.mintory.model.Articulo;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticuloRepository {

    private ArticuloDao articuloDao;
    private LiveData<List<Articulo>> todosLosArticulos;

    // Usamos un ExecutorService para correr las operaciones de base de datos en segundo plano
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Constructor
    public ArticuloRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        articuloDao = db.articuloDao();
        todosLosArticulos = articuloDao.obtenerTodos();
    }

    // Métodos para leer datos (LiveData ya se ejecuta en segundo plano por defecto)
    public LiveData<List<Articulo>> getTodosLosArticulos() {
        return todosLosArticulos;
    }

    public LiveData<List<Articulo>> getArticulosPorEstado(String estado) {
        return articuloDao.obtenerPorEstado(estado);
    }

    // Métodos para escribir datos (Deben ir en el ExecutorService)
    public void insertar(Articulo articulo) {
        databaseWriteExecutor.execute(() -> {
            articuloDao.insertar(articulo);
        });
    }

    public void actualizar(Articulo articulo) {
        databaseWriteExecutor.execute(() -> {
            articuloDao.actualizar(articulo);
        });
    }

    public void eliminar(Articulo articulo) {
        databaseWriteExecutor.execute(() -> {
            articuloDao.eliminar(articulo);
        });
    }
}