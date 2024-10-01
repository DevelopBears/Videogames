package org.grizzielicious.VideoGames.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.entities.Estudio;
import org.grizzielicious.VideoGames.exceptions.*;
import org.grizzielicious.VideoGames.service.EstudioService;
import org.grizzielicious.VideoGames.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/estudios")
public class EstudioController {

    @Autowired
    private EstudioService service;

    @GetMapping("/todos")
    public ResponseEntity<?> listarTodosLosEstudios() {
        log.info("Comienza el proceso de obtención de todos los estudios");
        return ResponseEntity.ok(service.listarTodosLosEstudios());
    }

    @GetMapping("/porNombre")
    public ResponseEntity<?> obtenerEstudiosPorNombre(@RequestParam String nombreEstudio)
            throws InvalidParameterException {
        log.info("Comienza el proceso de búsqueda de estudios con el nombre {}", nombreEstudio);
        if(nombreEstudio.isBlank()) {
            throw new InvalidParameterException("El parámetro de búsqueda debe de contener por lo menos un caracter");
        }
        return ResponseEntity.ok(service.encontrarEstudioPorNombre(nombreEstudio).orElse(null));
    }

    @GetMapping("/porNombreLike")
    public ResponseEntity<?> obtenerEstudiosPorNombreLike(@RequestParam String nombreEstudio)
            throws InvalidParameterException {
        log.info("Comienza el proceso de búsqueda de todos los estudios que coincidan con {}", nombreEstudio);
        if(nombreEstudio.isBlank()) {
            throw new InvalidParameterException("El parámetro de búsqueda debe de contener por lo menos un caracter");
        }
        return ResponseEntity.ok(service.encontrarPorNombreLike(nombreEstudio));
    }

    @GetMapping("/videojuegosPorEstudio/{idEstudio}")
    public ResponseEntity<?> obtenerVideojuegosPorEstudio(@PathVariable int idEstudio) throws EstudioNotFoundException {
        log.info("Comienza el proceso de obtención de videojuegos desarrollados por estudio por el ID: {}", idEstudio);
        return ResponseEntity.ok(service.encontrarVideojuegosPorEstudio(idEstudio));
    }

    @GetMapping("/porId/{idEstudio}")
    public ResponseEntity<?> obtenerEstudioPorId(@PathVariable int idEstudio) {
        log.info("Comienza el proceso de obtención de estudio con el ID: {}", idEstudio);
        return ResponseEntity.ok(service.encontrarPorId(idEstudio).orElse(null));
    }

    @PostMapping("/crearEstudio")
    public ResponseEntity<?> crearEstudio(@Valid @RequestBody Estudio estudio, Errors errores) throws InvalidParameterException, EstudioAlreadyExistsException {
        log.info("Comienza el proceso de creación de un nuevo estudio");
        HttpStatus status;
        String detail;
        if(errores.hasErrors()) {
            throw new InvalidParameterException("El estudio no cumple con las validaciones: " + ErrorUtils.errorsToStringSet(errores));
        }
        validarInexistenciaDeEstudio(estudio.getEstudio());
        try {
            int id = service.guardarEstudio(estudio);
            detail = "Se creó un nuevo estudio en la BD con el ID: " + id;
            status = HttpStatus.OK;
        } catch(Exception e) {
            detail = "Ocurrió una excepción al crear un nuevo estudio";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail,e);
        }
        log.info("Finaliza el proceso de creación de un nuevo estudio");
        return new ResponseEntity<>(detail,status);

    }

    @PutMapping("/actualizarEstuio/{id}")
    private ResponseEntity<?> actualizarEstudio(@PathVariable int id, @RequestParam String estudioMod)
            throws InvalidParameterException, EstudioAlreadyExistsException, EstudioNotFoundException {
        log.info("Comienza el proceso de actualización de estudio");
        HttpStatus status;
        Estudio aModificar;
        String detail;
        ErrorUtils.validateString25(estudioMod, "nombreEstudio");
        aModificar = validarExistenciaEstudio(id);
        validarInexistenciaDeEstudio(estudioMod);
        try {
            aModificar.setEstudio(estudioMod);
            service.guardarEstudio(aModificar);
            detail = "Se actualizó el estudio de manera correcta";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió una excepción al actualizar el estudio: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.info("Termina el proceso de actualización de estudios");
        return new ResponseEntity<>(detail, status);
    }

    @DeleteMapping("/borrarEstudio/{id}")
    private ResponseEntity<?> borrarEstudio(@PathVariable int id) throws EstudioNotFoundException {
        log.info("Iniciando el proceso de borrado de estudio");
        HttpStatus status;
        Estudio porBorrar;
        String detail;
        porBorrar = validarExistenciaEstudio(id);
        try {
            service.eliminarEstudio(porBorrar.getIdEstudio());
            detail = "Se borró el estudio de manera correcta";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió una excepción al actualizar el estudio: "  + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.info("Termina el proceso de borrado de estudio");
        return new ResponseEntity<>(detail, status);

    }

    public void validarInexistenciaDeEstudio (String nombreEstudio) throws EstudioAlreadyExistsException {
        int id = service.encontrarEstudioPorNombre(nombreEstudio)
                .orElse(Estudio.builder().idEstudio(-1).build()).getIdEstudio();
        if(id > 0) {
            throw new EstudioAlreadyExistsException("El estudio <" + nombreEstudio + "> ya se encuentra en la base de datos.");
        }
    }

    public Estudio validarExistenciaEstudio (int idEstudio) throws EstudioNotFoundException {
        return service.encontrarPorId(idEstudio)
                .orElseThrow(() -> new EstudioNotFoundException("El estudio con ID " + idEstudio + " no se encuentra en la BD"));
    }

}