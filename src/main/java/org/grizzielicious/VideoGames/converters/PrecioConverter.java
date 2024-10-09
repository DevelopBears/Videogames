package org.grizzielicious.VideoGames.converters;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.VideojuegoNotFoundException;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.grizzielicious.VideoGames.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrecioConverter implements iConverter<PrecioDto, Precio> {

    @Autowired
    private VideojuegoConverter videojuegoConverter;

    @Autowired
    private VideojuegoService videojuegoService;

    @Override
    public Precio convertFromDto(PrecioDto dto) throws VideojuegoNotFoundException {
        Videojuego videojuego;
        if(CommonUtils.isIntegerParseable(dto.getNombreOIdVideojuego())) {
            videojuego = videojuegoService
                    .encontrarPorId(Integer.parseInt( dto.getNombreOIdVideojuego() ))
                    .orElseThrow(() -> new VideojuegoNotFoundException("No existe ningún videojuego con el Id: "
                            +  dto.getNombreOIdVideojuego()));
        } else {
            videojuego = videojuegoService
                    .encontrarVideojuegoPorNombre(dto.getNombreOIdVideojuego())
                    .orElseThrow(() -> new VideojuegoNotFoundException("No existe ningún videojuego con el Nombre: "
                            +  dto.getNombreOIdVideojuego()));
        }
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
