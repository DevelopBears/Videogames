package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dto.Plataforma;

import java.util.List;
import java.util.Optional;

public interface PlataformaService {
    public List<Plataforma> listarTodasLasPlataformas();
    public Optional<Plataforma> encontrarPlataformaPorId(Plataforma plataforma);
    public Optional<Plataforma> encontrarPlataformaPorNombre(String plataforma);
}
