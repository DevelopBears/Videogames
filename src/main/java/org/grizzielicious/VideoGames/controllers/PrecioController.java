package org.grizzielicious.VideoGames.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.bo.PrecioBO;
import org.grizzielicious.VideoGames.service.PrecioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/precios")
public class PrecioController {

    @Autowired
    private PrecioBO precioBO;

    @GetMapping("/porId/{id}")
    public ResponseEntity<?> encontrarPrecioPorId (@PathVariable int id) {
        log.info("Comienza el proceso de obtención de precio con el ID: {}", id);
        return ResponseEntity.ok(precioBO.getPrecioPorId(id));
    }

    @Parameter(name = "fecha", description = "El formato de la fecha debe de ser en dd-MMM-yyyy HH-mm-ss", required = true)
    @GetMapping("/porFecha")
    public ResponseEntity<?> encontrarPorFecha (@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                @RequestParam LocalDateTime fecha) {
        log.info("Comienza el proceso de obtención de precio por la fecha {}", fecha);
        return ResponseEntity.ok(precioBO.getEncontrarPorFecha(fecha));
    }

    @GetMapping("/vigentePorVideojuego")
    public ResponseEntity<?> encontrarPorVideojuegoFecha (@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
                                                          @RequestParam LocalDateTime fecha,
                                                          @RequestParam int idVideojuego) {
        log.info("Comienza el proceso de obtención de precio por videojuego vigente con ID {}", idVideojuego);
        return ResponseEntity.ok(precioBO.getEncontrarVideojuegoPorFecha(fecha, idVideojuego));
    }

    @GetMapping("/activos")
    public ResponseEntity<?> encontrarPreciosActivos () {
        log.info("Comienza proceso de obtención de precios activos");
        return ResponseEntity.ok(precioBO.getEncontrarPreciosActivos());
    }

    @GetMapping("/activoPorVideojuego")
    public ResponseEntity<?> encontrarPreciosActivosPorVideojuego (int idVideojuego) {
        log.info("Se realiza la búsqueda por precio vigente de videojuego con el ID {}", idVideojuego);
        return ResponseEntity.ok(precioBO.getPreciosPorVideojuego(idVideojuego));
    }


}
