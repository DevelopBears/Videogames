package org.grizzielicious.VideoGames.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrecioDto {
    private int idPrecio;
    private float precioUnitario;
    private VideojuegoDTO videojuego;
    private LocalDateTime inicioVigencia;
    private LocalDateTime finVigencia;
}
