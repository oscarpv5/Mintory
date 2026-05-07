package com.oscar.mintory.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.oscar.mintory.model.Articulo;
import java.util.List;

@Dao
public interface ArticuloDao {

    @Insert
    void insertar(Articulo articulo);

    @Update
    void actualizar(Articulo articulo);

    @Delete
    void eliminar(Articulo articulo);

    // Obtener todo el inventario
    @Query("SELECT * FROM articulos ORDER BY titulo ASC")
    LiveData<List<Articulo>> obtenerTodos();

    // Filtro inteligente por estado (ej: solo los "Pendientes")
    @Query("SELECT * FROM articulos WHERE estado = :estadoFiltro")
    LiveData<List<Articulo>> obtenerPorEstado(String estadoFiltro);
}