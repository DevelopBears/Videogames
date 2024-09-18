package org.grizzielicious.VideoGames.bo;


import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.converters.PrecioConverter;
import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.entities.Precio;
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
        Precio precio = service.listarPreciosPorFechaVideojuego(fecha, idVideojuego).orElse(null);
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
}
