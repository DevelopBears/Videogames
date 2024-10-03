package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dtos.VideojuegosPorEstudioResponse;
import org.grizzielicious.VideoGames.entities.Estudio;
import org.grizzielicious.VideoGames.exceptions.EstudioNotFoundException;

import java.util.List;
import java.util.Optional;

public interface EstudioService {
    public List<Estudio> listarTodosLosEstudios();
    public Optional<Estudio> encontrarEstudioPorNombre(String nombreEstudio);
    public List<Estudio> encontrarPorNombreLike (String nombreEstudio);
    public VideojuegosPorEstudioResponse encontrarVideojuegosPorEstudio (int idEstudio) throws EstudioNotFoundException;
    public int guardarEstudio (Estudio estudio);
    public void eliminarEstudio (int idEstudio);
    public Optional<Estudio> encontrarPorId (int idEstudio);

}
