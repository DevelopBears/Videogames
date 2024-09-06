package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dao.GeneroDao;
import org.grizzielicious.VideoGames.dto.Genero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroServiceImpl implements GeneroService{

    @Autowired
    private GeneroDao generoRepository;


    @Override
    public List<Genero> listarTodosLosGeneros() {
        return generoRepository.findAllByEstaActivoIsTrueOrderByDescripcion();
    }

    @Override
    public Optional<Genero> encontrarGeneroPorId(int genero) {
        return generoRepository.findById(genero);
    }

    @Override
    public Optional<Genero> encontrarGeneroPorDescripcion(String descripcion) {
        return generoRepository.findGeneroByDescripcion(descripcion);
    }

    @Override
    public int guardaGenero(Genero genero) {
        return generoRepository.save(genero).getIdGenero();
    }
}
