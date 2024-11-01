package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.principal.Principal;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//CommandLineRunner es una interfaz en Spring Boot que permite ejecutar un bloque de código justo después de que la aplicación haya arrancado.
public class ScreenmatchApplication{


	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

}
