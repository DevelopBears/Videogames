package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dto.Estudio;

import java.util.List;
import java.util.Optional;

public interface EstudioService {
    public List<Estudio> listarTodosLosEstudios();
    public Estudio encontrarEstudio(Estudio estudio);
    public Optional<Estudio> encontrarEstudioPorNombre(String nombre);
}
