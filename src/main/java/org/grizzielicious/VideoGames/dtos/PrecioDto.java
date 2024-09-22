package org.grizzielicious.VideoGames.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.grizzielicious.VideoGames.constants.ValidationsConstants.PRICE_OUT_OF_RANGE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrecioDto implements Serializable {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int idPrecio;

    @NotNull(message = "precioUnitario" + CANNOT_BE_NULL_OR_EMPTY)
    @Range(min = 0, max = 8000, message = PRICE_OUT_OF_RANGE)
    private float precioUnitario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = VideojuegoDTO.class)
    private VideojuegoDTO videojuego;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM-6")
    //@NotNull(message = "fechaInicioVigencia" + CANNOT_BE_NULL_OR_EMPTY)
    private LocalDateTime inicioVigencia;

    //@Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM-6")
    private LocalDateTime finVigencia;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nombreVideojuego;

}
