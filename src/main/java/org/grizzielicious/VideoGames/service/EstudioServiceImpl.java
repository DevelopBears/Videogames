package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dao.EstudioDao;
import org.grizzielicious.VideoGames.dto.Estudio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudioServiceImpl implements EstudioService{
    @Autowired
    private EstudioDao repository;


    @Override
    public List<Estudio> listarTodosLosEstudios() {
        return repository.findAllByOrderByEstudio();
    }

    @Override
    public Optional<Estudio> encontrarEstudioPorId(int idEstudio) {
        return repository.findById(idEstudio);
    }

    @Override
    public Optional<Estudio> encontrarEstudioPorNombre(String nombreEstudio) {
        return repository.findEstudioByEstudio(nombreEstudio);
    }

    @Override
    public List<Estudio> encontrarEstudioPorNombreLike(String nombreEstudio) {
        return repository.findAllByEstudioContains(nombreEstudio);
    }

    @Override
    public List<Estudio> encontrarVideojuegoPorEstudio(String nombreEstudio) {
        return repository.findByDeveloperEstudio(nombreEstudio);
    }

    @Override
    public int guardarEstudio(Estudio estudio) {
        return repository.saveAndFlush(estudio).getIdEstudio();
    }

    @Override
    public void eliminarEstudio(int idEstudio) {
        repository.deleteById(idEstudio);
    }


}
