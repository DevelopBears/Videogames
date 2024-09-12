package org.grizzielicious.VideoGames.controllers;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.service.PlataformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/plataformas")
public class PlataformaController {


    @Autowired
    private PlataformaService service;

    @GetMapping("/todas")
    public ResponseEntity<?> todasLasPlataformas() {
        log.info("Comienza el listado de todas las plataformas");
        return ResponseEntity.ok(service.listarTodasLasPlataformas());
    }

    @GetMapping("/porId/{id}")
    public ResponseEntity<?> plataformaPorId(@PathVariable int idPlataforma) {
        log.info("Comienza la obtención de plataformas por ID {}", idPlataforma);
        return ResponseEntity.ok(service.encontrarPlataformaPorId(idPlataforma).orElse(null));
    }

    @GetMapping("/porNombreCompleto")
    public ResponseEntity<?> porNombreCompleto(@RequestParam String plataforma) {
        log.info("Comienza la búsqueda por nombre completo de plataforma {}", plataforma);
        return ResponseEntity.ok(service.encontrarPlataformaPorNombre(plataforma).orElse(null));
    }

    @GetMapping("/porNombreLike")
    public ResponseEntity<?> porNombreLike(@RequestParam String plataforma) {
        log.info("Comienza la búsqueda por plataforma por nombre coincidente con: {}", plataforma);
        return ResponseEntity.ok(service.encontrarPlataformaPorNombreLike(plataforma));
    }

    @GetMapping("/porPlataformaDisponible/{idVideojuego}")
    public ResponseEntity<?> porVideojuego(@RequestParam int idVideojuego) {
        log.info("Comienza el proceso de búsqueda de plataformas que estén disponibles epara el videojuego con id {} ", idVideojuego);
        return ResponseEntity.ok(service.encontrarPlataformaPorVideojuego(idVideojuego));
    }
}
