package org.grizzielicious.VideoGames.converters;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.NotImplementedException;
import org.grizzielicious.VideoGames.exceptions.VideojuegoNotFoundException;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PrecioConverter implements iConverter<PrecioDto, Precio> {

    @Autowired
    private VideojuegoConverter videojuegoConverter;

    @Autowired
    private VideojuegoService videojuegoService;

    @Override
    public Precio convertFromDto(PrecioDto dto) throws VideojuegoNotFoundException {
        Videojuego videojuego = videojuegoService
                .encontrarVideojuegoPorNombre(dto.getNombreVideojuego())
                .orElseThrow(() -> new VideojuegoNotFoundException("No existe ningún videojuego con el nombre: "
                        +  dto.getNombreVideojuego()));
        return Precio.builder()
                .precioUnitario(dto.getPrecioUnitario())
                .fechaInicioVigencia(dto.getInicioVigencia())
                .fechaFinVigencia(dto.getFinVigencia())
                .videojuego(videojuego)
                .build();
    }

    @Override
    public PrecioDto convertFromEntity(Precio entity) {
        return PrecioDto.builder()
                .idPrecio(entity.getIdPrecio())
                .precioUnitario(entity.getPrecioUnitario())
                .videojuego(videojuegoConverter.convertFromEntity(entity.getVideojuego()))
                .inicioVigencia(entity.getFechaInicioVigencia())
                .finVigencia(entity.getFechaFinVigencia())
                .build();
    }



}
