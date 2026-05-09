package com.oscar.mintory.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.oscar.mintory.data.ArticuloRepository;
import com.oscar.mintory.model.Articulo;
import java.util.List;

public class ArticuloViewModel extends AndroidViewModel {

    private ArticuloRepository repository;
    private LiveData<List<Articulo>> todosLosArticulos;

    public ArticuloViewModel(Application application) {
        super(application);
        repository = new ArticuloRepository(application);
        todosLosArticulos = repository.getTodosLosArticulos();
    }

    public LiveData<List<Articulo>> getTodosLosArticulos() {
        return todosLosArticulos;
    }

    public void insertar(Articulo articulo) {
        repository.insertar(articulo);
    }

    public void eliminar(Articulo articulo) {
        repository.eliminar(articulo);
    }

    public void actualizar(Articulo articulo) {
        repository.actualizar(articulo);
    }
}