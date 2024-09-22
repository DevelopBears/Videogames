package org.grizzielicious.VideoGames.bo;


import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.converters.PrecioConverter;
import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;
import org.grizzielicious.VideoGames.exceptions.PrecioAlreadyExistsException;
import org.grizzielicious.VideoGames.exceptions.PrecioNotFoundException;
import org.grizzielicious.VideoGames.exceptions.VideojuegoNotFoundException;
import org.grizzielicious.VideoGames.service.PrecioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PrecioBO {

    @Autowired
    private PrecioService service;

    @Autowired
    private PrecioConverter precioConverter;

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
                                  Boolean setFinVigenciaAsNull) throws PrecioNotFoundException, InvalidParameterException {
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
        service.guardarPrecio(precio);
    }



}
