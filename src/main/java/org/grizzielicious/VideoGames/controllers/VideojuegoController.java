package org.grizzielicious.VideoGames.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.converters.VideojuegoConverter;
import org.grizzielicious.VideoGames.entities.Estudio;
import org.grizzielicious.VideoGames.entities.Genero;
import org.grizzielicious.VideoGames.entities.Plataforma;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.*;
import org.grizzielicious.VideoGames.service.EstudioService;
import org.grizzielicious.VideoGames.service.GeneroService;
import org.grizzielicious.VideoGames.service.PlataformaService;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.grizzielicious.VideoGames.utils.ErrorUtils;
import org.grizzielicious.VideoGames.utils.VideojuegoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/videojuegos")
public class VideojuegoController {

    @Autowired
    private VideojuegoService service;

    @Autowired
    private VideojuegoConverter converter;

    @Autowired
    private PlataformaService plataformaService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private EstudioService estudioService;

    @Autowired
    private VideojuegoUtils videojuegoUtils;

    @GetMapping("/todos")
    public ResponseEntity<?> listarTodosLosVideojuegos (@RequestParam(required = false, defaultValue = "false")
                                                        boolean presentDTO) {
        log.info("Se comienza el proceso de búsqueda de todos los videojuegos en la BD");
        List<Videojuego> queryResult = service.encontrarTodos();
        log.info("Se encontraron {} resultados", queryResult.size());
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porID/{idVideojuego}")
    public ResponseEntity<?> encontrarPorId (@PathVariable int idVideojuego, @RequestParam(required = false,
                                                defaultValue = "false") boolean presentDTO) {
        log.info("Comienza el proceso de búsqueda de videojuego por el ID: {}", idVideojuego);
        Videojuego queryResult = service.encontrarPorId(idVideojuego).orElse(null);
        if (Objects.isNull(queryResult)) {
            return ResponseEntity.ok(null);
        }
        Object result = presentDTO ? converter.convertFromEntity(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/esMultijugador")
    public ResponseEntity<?> encontrarPorMultijugadorDisponible (@RequestParam boolean esMultijugador,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                       boolean presentDTO) {
        log.info("Comienza el proceso de búsqueda de videojuegos compatibles con multijugador");
        List<Videojuego> queryResult = service.encontrarPorMultijugador(esMultijugador);
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porNombreParcial")
    public ResponseEntity<?> encontrarPorNombreParcial(@RequestParam String nombreVideojuego,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean presentDTO) throws InvalidParameterException {
        log.info("Comienza el proceso de búsqueda por nombre coincidente con: {}", nombreVideojuego);
        if(nombreVideojuego.isBlank()) {
            throw new InvalidParameterException("La búsqueda debe de contener por lo menos un caracter válido.");
        }
        List<Videojuego> queryResult = service.encontrarPorNombreLike(nombreVideojuego);
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porNombreCompleto")
    public ResponseEntity<?> encontrarPorNombreCompleto(@RequestParam String nombreVideojuego,
                                                        @RequestParam (required = false, defaultValue = "false")
                                                        boolean presentDTO) throws InvalidParameterException {
        log.info("Comienza el proceso de búsqueda por el nombre {}", nombreVideojuego);
        if(nombreVideojuego.isBlank()) {
            throw new InvalidParameterException("La búsqueda debe de contener por lo menos un caracter válido.");
        }
        Videojuego queryResult = service.encontrarVideojuegoPorNombre(nombreVideojuego).orElse(null);
        if(Objects.isNull(queryResult)) {
            return ResponseEntity.ok(null);
        }
        Object result = presentDTO ? converter.convertFromEntity(queryResult) : queryResult;
        return ResponseEntity.ok(result);

    }

    @GetMapping("/porIdPlataforma/{idPlataforma}")
    public ResponseEntity<?> encontrarPorIdPlataforma (@PathVariable int idPlataforma,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean presentDTO) throws PlataformaNotFoundException {
        log.info("Comienza el proceso de búsqueda de videojuegos por el ID de plataforma: {}", idPlataforma);
        Plataforma p = plataformaService.encontrarPlataformaPorId(idPlataforma).orElseThrow(() -> new
                PlataformaNotFoundException("No existe ninguna plataforma con el id: " + idPlataforma));
        List<Videojuego> queryResult = service.encontrarPorIdPlataforma(idPlataforma);
        log.info("Se encontraron {} resultados.", queryResult.size());
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porNombrePlataforma")
    public ResponseEntity<?> encontrarPorNombrePlataforma (@RequestParam String nombrePlataforma,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean presentDTO) throws InvalidParameterException,
                                                           PlataformaNotFoundException {
        log.info("Comienza el proceso de búsqueda por nombre de plataforma coincidente con {}", nombrePlataforma);
        Plataforma p = plataformaService.encontrarPlataformaPorNombre(nombrePlataforma).orElseThrow(() -> new
                PlataformaNotFoundException("No existe la plataforma con el nombre: " + nombrePlataforma));
        if(nombrePlataforma.isBlank()) {
            throw new InvalidParameterException("La búsqueda debe de contener por lo menos un caracter válido.");
        }
        List<Videojuego> queryResult = service.encontrarPorNombrePlataforma(nombrePlataforma);
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porNombreGenero")
    public ResponseEntity<?> encontrarPorNombreGenero (@RequestParam String nombreGenero,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean presentDTO) throws GeneroNotFoundException,
                                                       InvalidParameterException {
        log.info("Comienza el proceso de búqueda por nombre de género coincidente con {}", nombreGenero);
        Genero g = generoService.encontrarGeneroPorDescripcion(nombreGenero).orElseThrow(() -> new
                GeneroNotFoundException("No existe el género con el nombre: " + nombreGenero));
        if(nombreGenero.isBlank()) {
            throw new InvalidParameterException("La búsqueda debe de contener por lo menos un caracter válido.");
        }
        List<Videojuego> queryResult = service.encontrarPorGenero(nombreGenero);
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porIdEstudio/{idEstudio}")
    public ResponseEntity<?> encontrarPorIdEstudio (@PathVariable int idEstudio,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean presentDTO) throws EstudioNotFoundException {
        log.info("Comienza el proceso de búsqueda de videojuegos por el ID de estudio: {}", idEstudio);
        Estudio e = estudioService.encontrarPorId(idEstudio).orElseThrow(() -> new
                EstudioNotFoundException("No existe ningún estudio con el id: " + idEstudio));
        List<Videojuego> queryResult = service.encontrarPorIdEstudio(idEstudio);
        log.info("Se encontraron {} resultados.", queryResult.size());
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porNombreEstudio")
    public ResponseEntity<?> encontrarPorNombreEstudio (@RequestParam String nombreEstudio,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean presentDTO) throws InvalidParameterException,
                                                       EstudioNotFoundException {
        log.info("Comienza el proceso de búsqueda por nombre de estudio coincidente con {}", nombreEstudio);
        Estudio e = estudioService.encontrarEstudioPorNombre(nombreEstudio).orElseThrow(() -> new
                EstudioNotFoundException("No existe el estudio con el nombre: " + nombreEstudio));
        if(nombreEstudio.isBlank()) {
            throw new InvalidParameterException("La búsqueda debe de contener por lo menos un caracter válido.");
        }
        List<Videojuego> queryResult = service.encontrarPorNombreEstudio(nombreEstudio);
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porRangoDePrecio")
    public ResponseEntity<?> encontrarPorRangoDePrecio (@RequestParam float precioMin, @RequestParam float precioMax,
                                                        @RequestParam (required = false, defaultValue = "false")
                                                        boolean presentDTO) throws InvalidParameterException {
        log.info("Comienza el proceso de búsqueda por rango de precio que esté entre {} y {}", precioMin, precioMax);
        if(precioMin>precioMax) {
            throw new InvalidParameterException("El precio mínimo debe de ser menor que el precio máximo.");
        }
        List<Videojuego> queryResult = service.encontrarPorRangoPrecio(precioMin, precioMax);
        List<?> result = presentDTO ? converter.convertFromEntityList(queryResult) : queryResult;
        return ResponseEntity.ok(result);
    }

    @PostMapping("/crearVideojuego")
    public  ResponseEntity<?> crearVideojuego(@Valid @RequestBody Videojuego videojuego, Errors errores)
            throws InvalidParameterException, VideojuegoAlreadyExistsException, EstudioNotFoundException,
            GeneroNotFoundException, PlataformaNotFoundException {
        log.info("Comienza el proceso de creación de un nuevo videojuego en la BD.");
        HttpStatus status;
        String detail;
        if(errores.hasErrors()) {
            throw new InvalidParameterException("El videojuego no cumple con las validaciones: " +
                    ErrorUtils.errorsToStringSet(errores));
        }
        validarInexistenciaVideojuego(videojuego.getNombreVideojuego());
        videojuegoUtils.fixedVideogameReference(videojuego);
        try {
            int id = service.guardarVideojuego(videojuego);
            detail = "Se ha creado exitosamente un nuevo videojuego en la BD con el ID <" + id + ">";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió un error al crear un nuevo videojuego: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail,e);
        }
        log.info("Finaliza proceso de creación de un nuevo videojuego");
        return new ResponseEntity<>(detail, status);
    }

    @PutMapping("/actualizarVideojuego/{idVideojuego}")
    public  ResponseEntity<?> actualizarVideojuego(@Valid @PathVariable int idVideojuego,
                                                   @RequestBody Videojuego videojuego, Errors errores)
                                                   throws InvalidParameterException, VideojuegoNotFoundException,
                                                   EstudioNotFoundException, GeneroNotFoundException,
                                                   PlataformaNotFoundException {
        log.info("Comienza el proceso de modificación de un nuevo videojuego en la BD.");
        HttpStatus status;
        String detail;
        if(errores.hasErrors()) {
            throw new InvalidParameterException("El videojuego no cumple con las validaciones: " +
                    ErrorUtils.errorsToStringSet(errores));
        }
        Videojuego anterior = validarExistenciaDeVideojuego(idVideojuego);
        videojuego.setIdVideojuego(anterior. getIdVideojuego());
        videojuegoUtils.fixedVideogameReference(videojuego);
        try {
            int id = service.guardarVideojuego(videojuego);
            detail = "Se ha actualizado exitosamente un nuevo videojuego en la BD con el ID <" + id + ">";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió un error al actualizar un nuevo videojuego: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(detail,e);
        }
        log.info("Finaliza proceso de actualización de un nuevo videojuego");
        return new ResponseEntity<>(detail, status);
    }

    @DeleteMapping("/borrarVideojuego/{idVideojuego}")
    public ResponseEntity<?> eliminarVideojuego (@PathVariable int idVideojuego) {
        log.info("Comienza el proceso de eliminación del videojuego con el ID: {}", idVideojuego);
        HttpStatus status;
        String detail;
        int id = validarExistenciaDeVideojuego(idVideojuego).getIdVideojuego();
        try {
            service.eliminarVideojuego(idVideojuego);
            detail = "Se ha eliminado el videojuego con el ID: " + idVideojuego + " de la base de datos.";
            status = HttpStatus.OK;
        } catch (Exception e) {
            detail = "Ocurrió un error al eliminar el videojuego con el ID: " + idVideojuego + " de la base de datos."
                    + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(detail, status);
    }


    private void validarInexistenciaVideojuego (String nombreVideojuego) throws VideojuegoAlreadyExistsException {
        int id = service.encontrarVideojuegoPorNombre(nombreVideojuego)
                .orElse(Videojuego.builder().idVideojuego(-1).build())
                .getIdVideojuego();
        if(id > 0) {
            throw new VideojuegoAlreadyExistsException("El videojuego <" + nombreVideojuego + "> ya existe en la BD");
        }
    }

    private Videojuego validarExistenciaDeVideojuego(int idVideojuego) throws VideojuegoNotFoundException{
        return service.encontrarPorId(idVideojuego)
                .orElseThrow(() -> new VideojuegoNotFoundException("No existe el videojuego con el ID: <" + idVideojuego
                        + ">"));
    }




}
