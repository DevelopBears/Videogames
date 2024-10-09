package org.grizzielicious.VideoGames.dtos;

import lombok.Data;
import org.grizzielicious.VideoGames.entities.Precio;

import java.util.List;

@Data
public class MassUploadResponse<T> {
    private int registrosAceptados;
    private int registrosErroneos;
    private List<T> listaAceptados;

    public MassUploadResponse() {
        this.registrosAceptados = 0;
        this.registrosErroneos = 0;
    }
    public void aumentaRegistrosAceptados() {
        this.registrosAceptados++;
    }
    public void aumentaRegistrosErroneos() {
        this.registrosErroneos++;
    }
}
