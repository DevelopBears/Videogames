package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.entities.Genero;

import java.util.List;
import java.util.Optional;

public interface GeneroService {
    public List<Genero> listarTodosLosGeneros();
    public Optional<Genero> encontrarGeneroPorId(int genero);
    public Optional<Genero> encontrarGeneroPorDescripcion(String descripcion);
    public int guardaGenero(Genero genero);


}
