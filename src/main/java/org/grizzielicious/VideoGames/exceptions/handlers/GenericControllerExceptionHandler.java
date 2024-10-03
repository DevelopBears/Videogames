package org.grizzielicious.VideoGames.exceptions.handlers;

import org.grizzielicious.VideoGames.controllers.*;
import org.grizzielicious.VideoGames.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(assignableTypes = {GeneroController.class, PlataformaController.class, EstudioController.class,
        PrecioController.class, VideojuegoController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GenericControllerExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, NumberFormatException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequest(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Hubo una excepción relacionada con los argumentos recibidos" + e.getMessage());
    }

    @ExceptionHandler({
            GeneroNotFoundException.class, GeneroAlreadyExistsException.class,
            PlataformaNotFoundException.class, PlataformaAlreadyExistsException.class,
            EstudioNotFoundException.class, EstudioAlreadyExistsException.class,
            PrecioNotFoundException.class, PrecioAlreadyExistsException.class,
            VideojuegoNotFoundException.class, VideojuegoAlreadyExistsException.class,
            InvalidFileException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNotFoundAndAlreadyExistsExceptions (Exception e ){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Hubo una excepción relacionada con los argumentos recibidos: " + e.getMessage());
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidRequest (Exception e ){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Hubo una excepción relacionada con los argumentos recibidos: " + e.getMessage());
    }
}
