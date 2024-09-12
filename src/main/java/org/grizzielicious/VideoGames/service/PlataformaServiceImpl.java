package org.grizzielicious.VideoGames.service;

import lombok.extern.slf4j.Slf4j;
import org.grizzielicious.VideoGames.dao.PlataformaDao;
import org.grizzielicious.VideoGames.dto.Plataforma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlataformaServiceImpl implements PlataformaService{

    @Autowired
    private PlataformaDao repository;

    @Override
    public List<Plataforma> listarTodasLasPlataformas() {
        return repository.findAllByOrderByPlataforma();
    }

    @Override
    public Optional<Plataforma> encontrarPlataformaPorId(int idPlataforma) {
        return repository.findByIdPlataforma(idPlataforma);
    }

    @Override
    public Optional<Plataforma> encontrarPlataformaPorNombre(String plataforma) {
        return repository.findByPlataforma(plataforma);
    }

    @Override
    public List<Plataforma> encontrarPlataformaPorNombreLike(String plataforma) {
        return repository.findByPlataformaContains(plataforma);
    }

    @Override
    public List<Plataforma> encontrarPlataformaPorVideojuego(int idVideojuego) {
        return repository.findByVideogameAvailability(idVideojuego);
    }

    @Override
    public int guardarPlataforma(Plataforma plataforma) {
        return repository.saveAndFlush(plataforma).getIdPlataforma();
    }

    @Override
    public void eliminarPlataforma(int idPlataforma) {
        repository.deleteById(idPlataforma);
    }
}
