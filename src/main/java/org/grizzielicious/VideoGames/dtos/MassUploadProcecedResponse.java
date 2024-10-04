package org.grizzielicious.VideoGames.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MassUploadProcecedResponse<T> implements Serializable {
    private int registrosAceptados;
    private int registrosRechazados;
    private int registrosGuardados;
    private String message;
    private List<T> aceptados;
    private List<T> rechazados;
}
