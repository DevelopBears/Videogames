package org.grizzielicious.VideoGames.converters;

import org.grizzielicious.VideoGames.dtos.PrecioDto;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.exceptions.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrecioConverter implements iConverter<PrecioDto, Precio> {

    @Autowired
    private VideojuegoConverter videojuegoConverter;

    @Override
    public Precio convertFromDto(PrecioDto dto) throws NotImplementedException {
        throw new NotImplementedException("Esta funcionalidad aún no está implementada");
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
