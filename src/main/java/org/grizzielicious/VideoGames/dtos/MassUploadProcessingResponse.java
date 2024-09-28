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
public class MassUploadProcessingResponse implements Serializable {
    private int registrosAceptados;
    private int registrosRechazados;
    private int registrosGuardados;
    private String message;
    private List<PrecioDto> aceptados;
    private List<PrecioDto> rechazados;
}
