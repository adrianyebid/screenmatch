package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.dto.EpisodioDto;
import com.aluracursos.screenmatch.dto.SerieDto;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//Debemos crear esta interfaz para que nos permita hacer operaciones CURD con jpa facilmente
public interface SerieRepository extends JpaRepository<Serie,Long> {
    // Documentation https://spring.io/projects/spring-data-jpa
    //    Usamos derived queries de JPA para crear los metodos de busqueda
    //    La estructura básica de una consulta derivada en JPA consiste en:
    //    verbo introductorio + palabra clave "By" + criterios de búsqueda
    //Con esto podemos buscar una serie por su nombre
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria categoria);

//    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(Integer totalTemporadas, Double evaluacion);
    // Podemos usar native Queries cuando no se va a cambiar la query o es estatica
    // Con Query podemos hacer scripts SQL directamente, cambiamos el nombre de la tabla por el nombre de la clase Serie
    // Ponemos : para que se distinga los atributos de la clase de los argumentos que recibimos
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(Integer totalTemporadas, Double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios(Serie serie);

    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie> lanzamientosMasRecientes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroTemporada")
    List<Episodio> obtenerTemporadasPorNumero(Long id, Long numeroTemporada);
}
