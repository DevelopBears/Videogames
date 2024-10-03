package org.grizzielicious.VideoGames.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.converters.VideojuegoConverter;
import org.grizzielicious.VideoGames.dtos.VideojuegoPorPlataformaResponseDto;
import org.grizzielicious.VideoGames.entities.Plataforma;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;
import org.grizzielicious.VideoGames.exceptions.PlataformaAlreadyExistsException;
import org.grizzielicious.VideoGames.exceptions.PlataformaNotFoundException;
import org.grizzielicious.VideoGames.service.PlataformaService;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.grizzielicious.VideoGames.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/plataformas")
public class PlataformaController {


    @Autowired
    private PlataformaService service;

    @Autowired
    private VideojuegoService videojuegoService;

    @Autowired
    private VideojuegoConverter converter;

    @GetMapping("/todas")
    public ResponseEntity<?> todasLasPlataformas() {
        log.info("Comienza el listado de todas las plataformas");
        return ResponseEntity.ok(service.listarTodasLasPlataformas());
    }

    @GetMapping("/porId/{id}")
    public ResponseEntity<?> plataformaPorId(@PathVariable int id) {
        log.info("Comienza la obtención de plataformas por ID {}", id);
        return ResponseEntity.ok(service.encontrarPlataformaPorId(id).orElse(null));
    }

    @GetMapping("/porNombreCompleto")
    public ResponseEntity<?> porNombreCompleto(@RequestParam String plataforma) throws InvalidParameterException {
        log.info("Comienza la búsqueda por nombre completo de plataforma {}", plataforma);
        if(plataforma.isBlank()) {
            throw new InvalidParameterException("El parámetro de búsqueda debe de contener por lo menos un caracter");
        }
        return ResponseEntity.ok(service.encontrarPlataformaPorNombre(plataforma).orElse(null));
    }

    @GetMapping("/porNombreLike")
    public ResponseEntity<?> porNombreLike(@RequestParam String plataforma) throws InvalidParameterException {
        log.info("Comienza la búsqueda por plataforma por nombre coincidente con: {}", plataforma);
        if(plataforma.isBlank()) {
            throw new InvalidParameterException("El parámetro de búsqueda debe de contener por lo menos un caracter");
        }
        return ResponseEntity.ok(service.encontrarPlataformaPorNombreLike(plataforma));
    }

    @GetMapping("/porPlataformaDisponible/{idVideojuego}")
    public ResponseEntity<?> porVideojuego(@RequestParam int idVideojuego) {
        log.info("Comienza el proceso de búsqueda de plataformas que estén disponibles epara el videojuego con id {} ",
                idVideojuego);
        return ResponseEntity.ok(service.encontrarPlataformaPorVideojuego(idVideojuego));
    }

    @GetMapping("/videojuegosPorPlataforma/{idPlataforma}")
    public ResponseEntity<?> videojuegosPorPLataforma (@PathVariable int idPlataforma) throws PlataformaNotFoundException {
        log.info("Comienza el proceso de búsqueda de videojuegos por la plataforma con ID: {}", idPlataforma);
        Plataforma p = validarPlataformaExistentePorId(idPlataforma);
        List<Videojuego> result = videojuegoService.encontrarPorIdPlataforma(idPlataforma);
        log.info("Se encontraron los siguientes {} resultados", result.size());
        return ResponseEntity.ok(
                VideojuegoPorPlataformaResponseDto.builder()
                        .idPlataforma(p.getIdPlataforma())
                        .nombrePlataforma(p.getPlataforma())
                        .videojuegos(converter.convertFromEntityList(result))
                        .build()
        );
    }

    @PostMapping("/crearPlataforma")
    public ResponseEntity<?> crearPlataforma(@Valid @RequestBody Plataforma nuevaPlataforma, Errors errores)
            throws InvalidParameterException, PlataformaAlreadyExistsException {
        log.info("Comienza el proceso de crear una nueva plataforma");
        String detail;
        HttpStatus status;
        if(errores.hasErrors()) {
            throw new InvalidParameterException("El género no cumple cono las validaciones: " +
                    ErrorUtils.errorsToStringSet(errores));
        }
        validarInexistenciaDePlataforma(nuevaPlataforma.getPlataforma());
        try {
            int id = service.guardarPlataforma(nuevaPlataforma);
            detail = "La plataforma se creó de manera correcta en la base de datos con el ID: " + id;
            status = HttpStatus.OK;
            log.info(detail);
        } catch (Exception e ) {
            detail = "Ocurrió una excepción al momento de guardar la plataforma en BD: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail,e);
        }
        return new ResponseEntity<> (detail, status);
    }

    @PatchMapping("/actualizarPlataforma/{id}")
    public ResponseEntity<?> actualizarPlataformaPorId (@PathVariable int id, @RequestParam String nombreAActualizar)
            throws PlataformaAlreadyExistsException, PlataformaNotFoundException, InvalidParameterException {
        log.info("Comienza el proceso de actualizar la plataforma con el ID: " + id);
        String detail;
        HttpStatus status;
        Plataforma toUpdate;
        ErrorUtils.validateString25(nombreAActualizar, "nombrePlataforma");
        toUpdate = validarPlataformaExistentePorId(id);
        validarInexistenciaDePlataforma(nombreAActualizar);
        try {
            toUpdate.setPlataforma(nombreAActualizar);
            service.guardarPlataforma(toUpdate);
            detail = "Se actualizó la plataforma de manera correcta con el ID: " + id;
            status = HttpStatus.OK;
        } catch (Exception e){
            detail = "Ocurrió una excepción al actualizar la plataforma: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail, e);
        }
        log.info("Se termina el proceso de borrado de género");
        return new ResponseEntity<>(detail, status);
    }

    @DeleteMapping("/borrarPlataforma/{id}")
    public ResponseEntity<?> eliminaPlataforma(@PathVariable int id) throws PlataformaNotFoundException {
        log.info("Comienza el proceso de eliminar una plataforma");
        String detail;
        HttpStatus status;

        int idBorrado = validarPlataformaExistentePorId(id).getIdPlataforma();
        try {
            service.eliminarPlataforma(idBorrado);
            detail = "La plataforma se borró de manera correcta la plataforma en la base de datos con el ID: " + id;
            status = HttpStatus.OK;
            log.info(detail);
        } catch (Exception e ) {
            detail = "Ocurrió una excepción al momento de borrar la plataforma en BD: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail,e);
        }
        return new ResponseEntity<> (detail, status);
    }

    private void validarInexistenciaDePlataforma(String plataforma) throws PlataformaAlreadyExistsException {
        int id = service.encontrarPlataformaPorNombre(plataforma)
                .orElse(Plataforma.builder().idPlataforma(-1).build())
                .getIdPlataforma();
        if(id > 0) {
            throw new PlataformaAlreadyExistsException("La plataforma <" + plataforma + "> ya existe en la BD");
        }
    }

    private Plataforma validarPlataformaExistentePorId(int idPlataforma) throws PlataformaNotFoundException {
        return service.encontrarPlataformaPorId( idPlataforma)
                .orElseThrow( () -> new PlataformaNotFoundException("La plataforma con ID <" + idPlataforma +
                        "> no existe en la BD"));
    }
}
