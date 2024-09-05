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
    private EstudioDao estudioDao;


    @Override
    public List<Estudio> listarTodosLosEstudios() {
        return (List<Estudio>) estudioDao.findAll();
    }

    @Override
    public Estudio encontrarEstudio(Estudio estudio) {
        return estudioDao.findById(estudio.getIdEstudio()).orElse(null);
    }

    @Override
    public Optional<Estudio> encontrarEstudioPorNombre(String nombre) {
        return estudioDao.findEstudioByEstudio(nombre);
    }
}
