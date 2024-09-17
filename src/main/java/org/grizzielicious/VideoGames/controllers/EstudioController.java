package org.grizzielicious.VideoGames.controllers;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dto.Estudio;
import org.grizzielicious.VideoGames.exceptions.EstudioAlreadyExistsException;
import org.grizzielicious.VideoGames.service.EstudioService;
//import org.hibernate.cache.spi.access.EntityDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/estudios")
public class EstudioController {

    @Autowired
    private EstudioService service;

    @GetMapping("/todos")
    public ResponseEntity<?> mostrarTodosLosEstudios(){
        log.info("Comienza el proceso de obtención de todos los estudios de la BD");
        List<Estudio> lista = service.listarTodosLosEstudios();
        log.info("Se obtuvieron {} estudios en total", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/porId/{id}")
    public ResponseEntity<?> encontrarEstudioPorId(@PathVariable int idEstudio) {
        log.info("Comienza el proceso de búsqueda de estudio con el ID: {}", idEstudio);
        return ResponseEntity.ok(service.encontrarEstudioPorId(idEstudio).orElse(null));
    }

    @GetMapping("/porNombre")
    public ResponseEntity<?> encontrarEstudioPorNombre(@RequestParam String nombreEstudio) {
        log.info("Comienza el proceso de búsqueda de estudio por nombre completo");
        return ResponseEntity.ok(service.encontrarEstudioPorNombre(nombreEstudio).orElse(null));
    }

    @GetMapping("/porNombreLike")
    public ResponseEntity<?> encontrarEstudioPorNombreLike (@RequestParam String nombreEstudio) {
        log.info("Comienza el proceso de búsqueda de estudio por nombre que contenga: {}", nombreEstudio);
        return ResponseEntity.ok(service.encontrarEstudioPorNombreLike(nombreEstudio));
    }

    @GetMapping("/videojuegosPorEstudio")
    public ResponseEntity<?> encontrarVideojuegosPorEstudio (@RequestParam String nombreEstudio) {
        log.info("Comienza el proceso de búsqueda para todos los videojuegos desarrollados por el estudio {}",
                nombreEstudio);
        return ResponseEntity.ok(service.encontrarVideojuegoPorEstudio(nombreEstudio));
    }
    //para crear un nuevo estudio se debe de validar que:
    //-no exista previamente en la base de datos
    //-que no esté vacía la descricpción del estudio
    @PostMapping("/crearEstudio")
    public ResponseEntity<?> crearEstudio (@RequestBody Estudio nuevoEstudio, Errors errors) {
        log.info("Comienza el proceso de crear un nuevo estudio");
        String detail;
        HttpStatus status;
        if (errors.hasErrors()){
            
        }
        try {
            int id = service.guardarEstudio(nuevoEstudio);
            detail = "El estudio se creó de manera correcta en la BD con el ID: " + id;
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió un error al crear el nuevo estudio en la BD: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail, e);
        }
        return new ResponseEntity<>(detail, status);
    }

    private void validarInexistenciaDeEstudio (String estudio) throws EstudioAlreadyExistsException {
        int id = service.encontrarEstudioPorNombre(estudio)
                .orElse(Estudio.builder().idEstudio(-1).build())
                .getIdEstudio();
        if (id > 0){
            throw new EstudioAlreadyExistsException("El estudio <" + estudio + "> ya existe en la BD");
        }
    }



}

//Añadir nuevas estudios
//Consultar todos los estudios OK
//Consultar estudio por nombre OK
//Consultar estudio por nombre like OK
//Consultar todos los videojuegos que ha desarrollado un estudio OK
//Eliminar estudio por id