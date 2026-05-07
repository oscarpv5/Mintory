# Mintory 📦

**Mintory** es una aplicación móvil nativa para Android diseñada para que los coleccionistas (de libros, videojuegos, vinilos, etc.) puedan centralizar y gestionar su inventario de forma profesional, rápida y eficiente.

Este proyecto constituye el Proyecto de Fin de Ciclo (PFC) para el Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM) - Curso 2024/2025.

## 🚀 Funcionalidades Principales

* **Sincronización con APIs**: Autorrelleno de datos técnicos para evitar la entrada manual mediante la integración con *Google Books API* y *RAWG API* (videojuegos).
* **Persistencia Total**: Almacenamiento local utilizando Room (SQLite) para garantizar que los datos estén siempre disponibles, incluso sin conexión.
* **Filtros Inteligentes**: Clasificación rápida del inventario según el estado del artículo (ej. Leído/Jugado, Pendiente).
* **Interfaz Moderna**: Diseño fluido basado en Material Design 3, utilizando listas optimizadas (RecyclerView) y carga eficiente de imágenes (Glide).

## 🛠️ Tecnologías Utilizadas

* **Frontend**: Java/Kotlin, XML (Android Nativo).
* **Arquitectura**: MVVM (Model-View-ViewModel).
* **Almacenamiento Local**: Room Persistence Library.
* **Networking**: Retrofit / Volley para consumo de APIs REST.
* **Procesamiento de Imágenes**: Glide.

## 📱 Requisitos del Sistema

* **Sistema Operativo**: Android 7.0 (Nougat, API 24) o superior.
* **Memoria**: Mínimo 2 GB de RAM.
* **Almacenamiento**: 50 MB de espacio libre.
* **Conexión**: Requerida únicamente para la búsqueda y añadido inicial de artículos mediante API.

## 👨‍💻 Autor

**Óscar Pillado Villaverde**

*Desarrollado como proyecto de cierre para evidenciar los conocimientos adquiridos durante el ciclo formativo.*