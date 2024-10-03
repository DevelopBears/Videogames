package org.grizzielicious.VideoGames.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.bo.MassUploadBO;
import org.grizzielicious.VideoGames.bo.PrecioBO;
import org.grizzielicious.VideoGames.dtos.MassUploadProcessingResponse;
import org.grizzielicious.VideoGames.dtos.MassUploadResponse;
import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.exceptions.*;
import org.grizzielicious.VideoGames.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/precios")
public class PrecioController {

    @Autowired
    private PrecioBO precioBO;

    @Autowired
    private MassUploadBO massUploadBO;

    @GetMapping("/porId/{id}")
    public ResponseEntity<?> encontrarPrecioPorId (@PathVariable int id) {
        log.info("Comienza el proceso de obtención de precio con el ID: {}", id);
        return ResponseEntity.ok(precioBO.getPrecioPorId(id));
    }

    @Parameter(name = "fecha", description = "El formato de la fecha debe de ser en yyyy-MM-dd HH:mm:ss", required = true)
    @GetMapping("/porFecha")
    public ResponseEntity<?> encontrarPorFecha (@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                @RequestParam LocalDateTime fecha) {
        log.info("Comienza el proceso de obtención de precio por la fecha {}", fecha);
        return ResponseEntity.ok(precioBO.getEncontrarPorFecha(fecha));
    }

    @GetMapping("/vigentePorVideojuego")
    public ResponseEntity<?> encontrarPorVideojuegoFecha (@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    @PostMapping("/crearPrecio")
    public ResponseEntity<?> crearPrecio (@Valid @RequestBody PrecioDto precio, Errors errors)
            throws InvalidParameterException {
        log.info("Comienza el proceso de crear un nuevo precio");
        HttpStatus status;
        String detail;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("El precio no cumple con las validaciones estipuladas: "
                    + ErrorUtils.errorsToStringSet(errors));
        }
        try {
            int id = precioBO.crearPrecio(precio);
            detail = "Se creó un nuevo precio de manera exitosa en la BD con el ID: <" + id + ">";
            status = HttpStatus.OK;
            log.info(detail);
        } catch (Exception e) {
            detail = "Ocurrió un error al crear un nuevo precio en la BD" + e.getMessage();
            if( e instanceof VideojuegoNotFoundException || e instanceof PrecioAlreadyExistsException) {
                log.error(e.getMessage());
                status = HttpStatus.BAD_REQUEST;
            } else {
                log.error(e.getMessage(), e);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(detail, status);
    }

    @PatchMapping("/actualizarPrecio/{idPrecio}")
    public ResponseEntity<?> actualizarPrecio (
            @RequestParam(required = false) Boolean setFinVigenciaAsNull,
            @PathVariable int idPrecio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaFinVigencia,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime fechaInicioVigencia,
            @RequestParam(required = false) Float precioUnitario
    ) throws InvalidParameterException {
        log.info("Comienza el proceso de actualizar un precio");
        HttpStatus status;
        String detail;
        if(Objects.isNull(fechaInicioVigencia) && Objects.isNull(fechaFinVigencia) && Objects.isNull(precioUnitario)
            && Objects.isNull(setFinVigenciaAsNull)) {
            throw new InvalidParameterException("Debe tener por lo menos un parámetro válido");
        }
        try {
            precioBO.actualizarPrecio(idPrecio,precioUnitario,fechaInicioVigencia,fechaFinVigencia,setFinVigenciaAsNull);
            detail = "Se actualizó un precio de manera exitosa en la BD con el ID: <" + idPrecio + ">";
            status = HttpStatus.OK;
            log.info(detail);
        } catch (Exception e) {
            detail = "Ocurrió un error al actualizar un precio en la BD" + e.getMessage();
            if(e instanceof PrecioNotFoundException) {
                log.error(e.getMessage());
                status = HttpStatus.BAD_REQUEST;
            } else {
                log.error(e.getMessage(), e);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(detail, status);
    }

    @GetMapping("/clonarPrecio")
    public ResponseEntity<?> clonarPrecio (@RequestParam int idPrecio, @RequestParam int idVideojuego)
            throws InvalidParameterException, PrecioNotFoundException, VideojuegoNotFoundException,
            PrecioAlreadyExistsException {
        log.info("Comienza el proceso de clonar un precio");
        return ResponseEntity.ok(precioBO.clonaPrecio(idPrecio, idVideojuego));
    }

    @PostMapping(path = "/cargaMasivaDePrecios", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cargaMasivaDePrecios(@RequestParam(value = "layout", required = true) MultipartFile layout)
            throws InvalidFileException {
        log.info("Comienza el proceso de carga masiva ded precios");
        MassUploadResponse cargaResponse = massUploadBO.getPreciosFromFile(layout);
        MassUploadProcessingResponse response = precioBO.procesaCargaMasiva(cargaResponse.getPrecios());
        response.setRegistrosRechazados(response.getRegistrosRechazados() + cargaResponse.getRegistrosErroneos());
        log.info("Termina el proceso de craga masiva");
        return ResponseEntity.ok(response);
    }
}
