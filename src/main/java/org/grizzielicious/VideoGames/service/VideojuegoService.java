package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.entities.Videojuego;

import java.util.Optional;

public interface VideojuegoService {
    public Optional<Videojuego> encontrarVideojuegoPorNombre (String nombreVideojuego);
}
