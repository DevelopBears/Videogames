package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dto.Plataforma;

import java.util.List;
import java.util.Optional;

public interface PlataformaService {
    public List<Plataforma> listarTodasLasPlataformas();
    public Optional<Plataforma> encontrarPlataformaPorId(int idPlataforma);
    public Optional<Plataforma> encontrarPlataformaPorNombre(String plataforma);
    public List<Plataforma> encontrarPlataformaPorNombreLike (String plataforma);
    public List<Plataforma> encontrarPlataformaPorVideojuego (int idVideojuego);
    public int guardarPlataforma (Plataforma plataforma);
    public void eliminarPlataforma(int idPlataforma);
}
