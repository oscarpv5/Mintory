package com.oscar.mintory.model;

import java.io.Serializable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "articulos")
public class Articulo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titulo;
    private String autorODesarrolladora;
    private String anioPublicacion;
    private String caratulaUrl;
    private String fechaAdquisicion;
    private float valoracion;
    private String estado;
    private String tipo;

    // Constructor
    public Articulo(String titulo, String autorODesarrolladora, String anioPublicacion, String caratulaUrl, String fechaAdquisicion, float valoracion, String estado, String tipo) {
        this.titulo = titulo;
        this.autorODesarrolladora = autorODesarrolladora;
        this.anioPublicacion = anioPublicacion;
        this.caratulaUrl = caratulaUrl;
        this.fechaAdquisicion = fechaAdquisicion;
        this.valoracion = valoracion;
        this.estado = estado;
        this.tipo = tipo;
    }

    // Getters y Setters (Obligatorios para Room)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutorODesarrolladora() { return autorODesarrolladora; }
    public void setAutorODesarrolladora(String autorODesarrolladora) { this.autorODesarrolladora = autorODesarrolladora; }
    public String getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(String anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public String getCaratulaUrl() { return caratulaUrl; }
    public void setCaratulaUrl(String caratulaUrl) { this.caratulaUrl = caratulaUrl; }
    public String getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(String fechaAdquisicion) { this.fechaAdquisicion = fechaAdquisicion; }
    public float getValoracion() { return valoracion; }
    public void setValoracion(float valoracion) { this.valoracion = valoracion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}