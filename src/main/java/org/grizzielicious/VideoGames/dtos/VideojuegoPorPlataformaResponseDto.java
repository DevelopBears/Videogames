package org.grizzielicious.VideoGames.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideojuegoPorPlataformaResponseDto implements Serializable {
    private int idPlataforma;
    private String nombrePlataforma;
    private List<VideojuegoDTO> videojuegos;
}
