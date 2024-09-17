package org.grizzielicious.VideoGames.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideojuegoDTO {
    private int idVideojuego;
    private String nombreVideojuego;
    private String allPlataformsAppended;

}
