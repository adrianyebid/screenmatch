package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    // Scanner para leer la entrada del usuario
    private Scanner teclado = new Scanner(System.in);

    // Objeto para consumir API
    private ConsumoAPI consumoApi = new ConsumoAPI();

    // URL base de la API OMDb y clave de API
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f8cfc5ac";

    // Objeto para convertir datos JSON a objetos Java
    private ConvierteDatos conversor = new ConvierteDatos();

    // Lista para almacenar los datos de las series obtenidas
    private List<DatosSerie> datosSeries = new ArrayList<>();

    // Repositorio para almacenar las series en una base de datos
    private SerieRepository repositorio;

    // Lista para almacenar las series recuperadas del repositorio
    private List<Serie> series;

    private Optional<Serie> serieBuscada;

    // Constructor de la clase, inicializa el repositorio
    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    // Método para mostrar el menú principal
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series\s
                    2 - Buscar episodios
                    3 - Mostrar series buscadas                   \s
                    4 - Buscar serie por titulo\s
                    5 - Top 5 mejores series
                    6 - Buscar series por categoria
                    7 - Buscar por numero de temporada y evaluacion
                    8 - Buscar episodios por titulo
                    9 - Top 5 episodios por Serie
                    0 - Salir                  \s
                   \s""";
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine(); // Consume newline
            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarPorTemporadaYPorEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    // Método para obtener los datos de una serie desde la API
    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json); // Muestra el JSON obtenido
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    // Método para buscar episodios de una serie específica
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quiere ver los episodios");
        var nombreSerie = teclado.nextLine();
        List<DatosTemporadas> temporadas = new ArrayList<>();
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }

    // Método para buscar una serie en la web y guardarla en el repositorio
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos); // Comentado porque ya no se usa la lista local
        System.out.println(datos); // Muestra los datos de la serie obtenida
    }

    // Método para mostrar las series buscadas y almacenadas en el repositorio
    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no econtrada");
        }


    }

    private void buscarTop5Series(){
       List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
       topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " Evaluacion: " + s.getEvaluacion()));
    }


    private void buscarSeriesPorCategoria(){
        System.out.println("Escriba el genero/categoria de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);// pasamos el string a tipo ENUM Categoria
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria "+ genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarPorTemporadaYPorEvaluacion(){
        System.out.println("Escriba el numero de temporadas de la serie");
        var numeroTemporadas = teclado.nextInt();
        teclado.nextLine(); // Consume newline
        System.out.println("Escriba la evaluacion de la serie");
        var evaluacionSerie = Double.valueOf(teclado.nextLine());
        List<Serie> seriesPorTemporadaYEvaluacion = repositorio.seriesPorTemporadaYEvaluacion(numeroTemporadas,evaluacionSerie);
        System.out.println("Las series con "+numeroTemporadas+" temporadas y evaluacion de "+evaluacionSerie+" son: ");
        seriesPorTemporadaYEvaluacion.forEach(s -> System.out.println(s.getTitulo()+"- evaluacion: "+ s.getEvaluacion()));


    }

    private void buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar:");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s",
                                    e.getSerie(),e.getTemporada(),e.getNumeroEpisodio(), e.getEvaluacion()));
    }

    private void buscarTop5Episodios(){
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            System.out.println(serie.getTitulo());
            System.out.println();
            topEpisodios.forEach(e -> System.out.println( e.getTitulo()
                    + " Temporada: "+ e.getTemporada()
                    + " Evaluacion: "+e.getEvaluacion()));

        }
    }
}
