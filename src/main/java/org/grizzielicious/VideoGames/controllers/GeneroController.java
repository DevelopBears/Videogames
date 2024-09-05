package org.grizzielicious.VideoGames.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dto.Genero;
import org.grizzielicious.VideoGames.service.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
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
    @ApiResponse(responseCode = "500", description = "Hubo un eeror en la consulta")
    @GetMapping("/porNombre")
    private ResponseEntity<?> obtenerGeneroPorNombre(@RequestParam String nombre){
        log.info("Se listan todos los géneros por nombre");
        return ResponseEntity.ok(service.encontrarGeneroPorDescripcion(nombre).orElse(null));
    }
}
