package com.aluracursos.screenmatch.dto;

import com.aluracursos.screenmatch.model.Categoria;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

// SerieDto es un Objeto de Transferencia de Datos (DTO) que se usa para encapsular
// los datos esenciales de la entidad Serie. Este DTO se usa en el SerieController
// para devolver solo la información necesaria al cliente, ayudando a mantener oculta
// la estructura interna y proporcionando una manera limpia y eficiente de transferir datos.

public record SerieDto(
        Long id,
         String titulo,
         Integer totalTemporadas,
         Double evaluacion,
         String poster,
        // Mapea el enumerado de categoría como un STRING en la base de datos
         Categoria genero,
         String actores,
         String sinopsis
) {
}
