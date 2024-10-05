package org.grizzielicious.VideoGames.bo;


import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.converters.PrecioConverter;
import org.grizzielicious.VideoGames.dtos.MassUploadProcecedResponse;
import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.*;
import org.grizzielicious.VideoGames.service.PrecioService;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Component
public class PrecioBO {

    @Autowired
    private PrecioService service;

    @Autowired
    private PrecioConverter precioConverter;

    @Autowired
    private VideojuegoService videojuegoService;

    public PrecioDto getPrecioPorId(int id) {
        Precio precio = service.encontrarPorId(id).orElse(null);
        return (Objects.isNull(precio) ? null: precioConverter.convertFromEntity(precio));
    }

    public List<PrecioDto> getEncontrarPorFecha(LocalDateTime fecha) {
        List<Precio> precioList = service.listarPreciosPorFecha(fecha);
        return precioList.isEmpty() ? new ArrayList<>() : precioConverter.convertFromEntityList(precioList);
    }

    public PrecioDto getEncontrarVideojuegoPorFecha (LocalDateTime fecha, int idVideojuego) {
        Precio precio = service.encontrarPrecioPorFechaVideojuego(fecha, idVideojuego).orElse(null);
        return (Objects.isNull(precio)) ? null : precioConverter.convertFromEntity(precio);
    }

    public List<PrecioDto> getEncontrarPreciosActivos() {
        List<Precio> precioList = service.encontrarPreciosActivos();
        return precioList.isEmpty() ? new ArrayList<>() : precioConverter.convertFromEntityList(precioList);
    }

    public PrecioDto getPreciosPorVideojuego(int idVideojuego) {
        Precio precio = service.encontrarPrecioActivoParaVideojuego(idVideojuego).orElse(null);
        return (Objects.isNull(precio) ? null : precioConverter.convertFromEntity(precio));
    }

    public int crearPrecio (PrecioDto precioDto) throws PrecioAlreadyExistsException, VideojuegoNotFoundException,
            InvalidParameterException {
        int idGuardado = 0;
        Precio precio = precioConverter.convertFromDto(precioDto);
        Precio anterior = service.encontrarPrecioPorFechaVideojuego(precio.getFechaInicioVigencia(),
                precio.getVideojuego().getIdVideojuego()).orElse(null);
        if(Objects.isNull(anterior)) {
            idGuardado = service.guardarPrecio(precio);
        } else if (Objects.isNull(anterior.getFechaFinVigencia() )
            && precio.getFechaInicioVigencia().minusSeconds(1).isAfter(anterior.getFechaInicioVigencia()))  {
            anterior.setFechaFinVigencia(precio.getFechaInicioVigencia().minusSeconds(1));
            service.guardarPrecio(anterior);
            idGuardado = service.guardarPrecio(precio);
        } else {
            throw new PrecioAlreadyExistsException(
                    "Ya existe un precio cuya vigencia se traslapa con la solicitada. ver precio con id <"
                            + anterior.getIdPrecio() + ">");
        }
        return idGuardado;

    }

    public void actualizarPrecio (int id, Float precioUnitario, LocalDateTime inicioVigencia, LocalDateTime finVigencia,
                                  Boolean setFinVigenciaAsNull) throws PrecioNotFoundException, InvalidParameterException,
                                    PrecioAlreadyExistsException {
        Precio precio = service.encontrarPorId(id)
                .orElseThrow(() -> new PrecioNotFoundException("No existe el precio con el ID <" + id + ">"));
        if (Objects.nonNull(precioUnitario)) {
            precio.setPrecioUnitario(precioUnitario);
        }
        if(Objects.nonNull(inicioVigencia)) {
            precio.setFechaInicioVigencia(inicioVigencia);
        }
        if(Objects.nonNull(finVigencia)) {
            precio.setFechaFinVigencia(finVigencia);
        }
        if (Objects.nonNull(setFinVigenciaAsNull) && setFinVigenciaAsNull) {
            precio.setFechaFinVigencia(null);
        }
        this.validarInexistenciaPrecio(precio.getVideojuego().getIdVideojuego(), precio.getFechaInicioVigencia(),
                precio.getFechaFinVigencia(), true);
        service.guardarPrecio(precio);
    }

    public int clonaPrecio(int idPrecio, int idVidejuego) throws PrecioNotFoundException, VideojuegoNotFoundException,
            InvalidParameterException, PrecioAlreadyExistsException {
        Precio precioAClonar = service.encontrarPorId(idPrecio)
                .orElseThrow(() -> new PrecioNotFoundException("No existe el precio con el ID: " + idPrecio));
        Videojuego videojuego = videojuegoService.encontrarPorId(idVidejuego)
                .orElseThrow(() -> new VideojuegoNotFoundException("No existe el videojuyego con el ID: " + idVidejuego));
        Precio nuevoPrecio = Precio.builder()
                .precioUnitario(precioAClonar.getPrecioUnitario())
                .fechaInicioVigencia(precioAClonar.getFechaInicioVigencia())
                .fechaFinVigencia(precioAClonar.getFechaFinVigencia())
                .videojuego(videojuego)
                .build();
        validarInexistenciaPrecio(nuevoPrecio.getVideojuego().getIdVideojuego(), nuevoPrecio.getFechaInicioVigencia(),
                nuevoPrecio.getFechaFinVigencia(), false);
        return service.guardarPrecio(nuevoPrecio);
    }

    public MassUploadProcecedResponse<PrecioDto> procesaCargaMasiva(List<Precio> precioList) {
        List<Precio> aceptados = new ArrayList<>();
        List<Precio> rechazados = new ArrayList<>();
        int registrosGuardados = 0;
        String message;
        for( Precio p : precioList) {
            try{
                this.validarInexistenciaPrecio(p.getVideojuego().getIdVideojuego(), p.getFechaInicioVigencia(),
                        p.getFechaFinVigencia(), false);
                aceptados.add(p);
            } catch (PrecioAlreadyExistsException | InvalidParameterException e) {
                rechazados.add(p);
                log.error("Precio rechazado debido a: {}", e.getMessage());
            }
        }
        try {
            registrosGuardados = service.guardarListaDePrecios(aceptados);
            if(registrosGuardados == 0) {
                message = "Todos los registros del layout fueron rechazados al fallar en las validaciones";
            } else if (!rechazados.isEmpty() || registrosGuardados < aceptados.size()) {
                message = "Algunos registros del layout fueron rechazados";
            } else {
                message = "La lista de precio se guardó correctamente en el BD";
            }
        } catch (Exception e) {
            message = "Ocurrió una excepción al guardar la lista de precios: " + e.getMessage();
            if(e instanceof InvalidParameterException) {
                log.error(e.getMessage());
            } else {
                log.error(e.getMessage(), e);
            }
        }

        return MassUploadProcecedResponse.<PrecioDto>builder()
                .registrosAceptados(aceptados.size())
                .registrosRechazados(rechazados.size())
                .registrosGuardados(registrosGuardados)
                .aceptados(precioConverter.convertFromEntityList(aceptados))
                .rechazados(precioConverter.convertFromEntityList(rechazados))
                .message(message)
                .build();
    }

    private void validarInexistenciaPrecio(int idVideojuego, LocalDateTime inicioVigencia, LocalDateTime finVigencia,
                                           boolean isUpdate) throws PrecioAlreadyExistsException,
                                           InvalidParameterException {
        List<Precio> preciosEnConflicto = service.encontrarPreciosEnConflicto(idVideojuego, inicioVigencia, finVigencia);
        if(preciosEnConflicto.isEmpty()) {
            return;
        }
        if(preciosEnConflicto.size()>1) {
            String idsEnConflicto = preciosEnConflicto.stream()
                    .map(Precio::getIdPrecio)
                    .map(id -> Integer.toString(id))
                    .collect(Collectors.joining(","));
            throw new PrecioAlreadyExistsException("Existe más de un precio en conflicto para el videojuego <" +
                    idVideojuego + "> con los precios: " + idsEnConflicto);
        }
        if(isUpdate) {
            return;
        }
        Precio anterior = preciosEnConflicto.get(0);
        anterior.setFechaFinVigencia(inicioVigencia.minusSeconds(1));
        service.guardarPrecio(anterior);
        log.info("Precio actualizado: {}", anterior.getIdPrecio());
    }



}
