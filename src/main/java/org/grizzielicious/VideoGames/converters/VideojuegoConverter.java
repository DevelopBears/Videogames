package org.grizzielicious.VideoGames.converters;

import org.grizzielicious.VideoGames.dtos.VideojuegoDTO;
import org.grizzielicious.VideoGames.entities.Plataforma;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.NotImplementedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideojuegoConverter {
    public Videojuego convertFromDto(VideojuegoDTO dto) throws NotImplementedException {
        throw new NotImplementedException("Esta funcionalidad aún no está implementada");
    }

    public List<Videojuego> convertFromDtos (List<VideojuegoDTO> listDto) throws NotImplementedException {
        throw new NotImplementedException("Esta funcionalidad aún no está implementada");
    }

    public VideojuegoDTO convertFromEntity(Videojuego entity) {
        return VideojuegoDTO.builder()
                .idVideojuego(entity.getIdVideojuego())
                .nombreVideojuego(entity.getNombreVideojuego())
                .allPlataformsAppended(entity.getPlataformas().stream()
                        .map(Plataforma::getPlataforma)
                        .collect(Collectors.joining(", "))
                )
                .build();
    }

    public List<VideojuegoDTO> convertFromEntities(List<Videojuego> listEntity) {
        return listEntity.stream()
                .map(this::convertFromEntity)
                .toList();
    }
}

