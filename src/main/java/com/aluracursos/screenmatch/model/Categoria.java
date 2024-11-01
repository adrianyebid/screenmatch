package com.aluracursos.screenmatch.model;

// Enumeración que representa diferentes categorías de películas
public enum Categoria {

    ACCION("Action", "Acción"),       // Categoría de películas de acción
    ROMANCE("Romance", "Romance"),    // Categoría de películas románticas
    COMEDIA("Comedy", "Comedia"),     // Categoría de películas de comedia
    DRAMA("Drama", "Drama"),          // Categoría de películas de drama
    CRIMEN("Crime", "Crimen"),        // Categoría de películas de crimen
    ANIMACION("Animation", "Animación"); // Categoría de películas de animación

    private String categoriaOmdb;     // Nombre de la categoría en la base de datos OMDB
    private String categoriaEspanol;  // Nombre de la categoría en español

    // Constructor para inicializar los nombres de las categorías
    Categoria(String categoriaOmdb, String categoriaEspanol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }

    // Método para obtener la categoría a partir de su nombre en OMDB
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría encontrada : " + text);
    }

    // Método para obtener la categoría a partir de su nombre en español
    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría encontrada : " + text);
    }
}
