package org.grizzielicious.VideoGames.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dto.Genero;
import org.grizzielicious.VideoGames.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/generos")
public class GeneroController {

    @Autowired
    private GeneroService service;

    @Operation(summary = "Endpoint para listar todos los géneros que existen actualmente", description = "Aquí podría ir una descripción más amplia") //todo quitar al final del proyecto
    @ApiResponse(responseCode = "200", description = "Consulta completada exitosamente")
    @ApiResponse(responseCode = "500", description = "Hubo un eeror en la consulta")
    @GetMapping("/todos")
    private ResponseEntity<?> listarTodosLosGeneros(){
        log.info("Se listan todos los géneros");
        List<Genero> lista = service.listarTodosLosGeneros();
        log.info("Se obtuvieron {} registros", lista.size());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Endpoint para listar todos los géneros que existen actualmente", description = "Aquí podría ir una descripción más amplia") //todo quitar al final del proyecto la descripción
    @Parameter(name = "id", description = "Id del género buscado, de tipo int32", required = true)
    @ApiResponse(responseCode = "200", description = "Consulta completada exitosamente")
    @ApiResponse(responseCode = "500", description = "Hubo un eeror en la consulta")
    @GetMapping("/porId/{id}")
    private ResponseEntity<?> obtenerGeneroPorId(@PathVariable int id){
        log.info("Se listan todos los géneros por ID");
        return ResponseEntity.ok(service.encontrarGeneroPorId(id).orElse(null));
    }

    @Operation(summary = "Endpoint para listar todos los génerospor su nombre", description = "Aquí podría ir una descripción más amplia") //todo quitar al final del proyecto la descripción
    @Parameter(name = "nombre", description = "Nombre del género buscado, de tipo int32", required = true)
    @ApiResponse(responseCode = "200", description = "Consulta completada exitosamente")
    @ApiResponse(responseCode = "500", description = "Hubo un error en la consulta")
    @GetMapping("/porNombre")
    private ResponseEntity<?> obtenerGeneroPorNombre(@RequestParam String nombre){
        log.info("Se listan todos los géneros por nombre");
        return ResponseEntity.ok(service.encontrarGeneroPorDescripcion(nombre).orElse(null));
    }

    @PostMapping("/crearGenero")
    private ResponseEntity<?> crearGenero(@RequestBody Genero genero) {
        log.info("Comenzando a crear un género");
        HttpStatus status;
        String detail;
        try {
            validarInexistenciaDeGenero(genero);
            int id = service.guardaGenero(genero);
            detail = "Se creó un nuevo género en la BD con el id = " + id;
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió un error al crear un nuevo género: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail);
        }
        log.info("Finaliza proceso de creación de género");
        return new ResponseEntity<>(detail, status);
    }

    @PutMapping("/actualizarGenero/{id}")
    private ResponseEntity<?> actualizarGenero(@PathVariable int id, @RequestParam String newDescripcion){
        log.info("Iniciando el proceso de actualización de género");
        HttpStatus status;
        Genero aActulizar;
        String detail;
        try {
            aActulizar = validarExistenciaDeGenero(id);
            validarInexistenciaDeGenero(newDescripcion);
            aActulizar.setDescripcion(newDescripcion);
            service.guardaGenero(aActulizar);
            detail = "Se actualizó el género de manera correcta";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió una excepción al actualizar el género: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.info("Termina el proceso de actualización de género");
        return new ResponseEntity<>(detail, status);
    }

    @DeleteMapping("/borrarGenero/{id}")
    private ResponseEntity<?> borrarGenero(@PathVariable int id){
        log.info("Iniciando el proceso de borrado de género");
        HttpStatus status;
        Genero aActulizar;
        String detail;
        try {
            aActulizar = validarExistenciaDeGenero(id);
            aActulizar.setEstaActivo(false);
            service.guardaGenero(aActulizar);
            detail = "Se borró el género de manera correcta";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió una excepción al actualizar el género: "  + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.info("Termina el proceso de borrado de género");
        return new ResponseEntity<>(detail, status);

    }

    public void validarInexistenciaDeGenero(Genero aValidar) throws Exception {
        int id = service.encontrarGeneroPorDescripcion(aValidar.getDescripcion())
                .orElse(Genero.builder().idGenero(-1).build())
                .getIdGenero();
        if(id > 0) {
            throw new Exception("El género <" + aValidar.getDescripcion() + "> ya se encuentra en la base de datos");
        }
    }

    public void validarInexistenciaDeGenero(String descripcion) throws Exception {
        int id = service.encontrarGeneroPorDescripcion(descripcion)
                .orElse(Genero.builder().idGenero(-1).build())
                .getIdGenero();
        if(id > 0) {
            throw new Exception("El género <" + descripcion + "> ya se encuentra en la base de datos");
        }
    }

    public Genero validarExistenciaDeGenero(int idBuscado) throws Exception {
        Genero genero = service.encontrarGeneroPorId(idBuscado)
                .orElse(Genero.builder().idGenero(-1).build());
        if(genero.getIdGenero() < 0) {
            throw new Exception("El género <" + idBuscado + "> no existe en la base de datos");
        }
        return genero;
    }



}
