package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dao.VideojuegoDao;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideojuegoServiceImpl implements VideojuegoService {

    @Autowired
    private VideojuegoDao repository;

    @Override
    public Optional<Videojuego> encontrarVideojuegoPorNombre(String nombreVideojuego) {
        return repository.findVideojuegoByNombreVideojuego(nombreVideojuego);
    }
}
