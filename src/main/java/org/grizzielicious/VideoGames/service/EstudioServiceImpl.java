package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.converters.VideojuegoConverter;
import org.grizzielicious.VideoGames.dao.EstudioDao;
import org.grizzielicious.VideoGames.dao.VideojuegoDao;
import org.grizzielicious.VideoGames.dtos.VideojuegosPorEstudioResponse;
import org.grizzielicious.VideoGames.entities.Estudio;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.EstudioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstudioServiceImpl implements EstudioService{
    @Autowired
    private EstudioDao repository;

    @Autowired
    private VideojuegoDao videojuegoRespository;

    @Autowired
    private VideojuegoConverter videojuegoConverter;

    @Override
    public List<Estudio> listarTodosLosEstudios() {
        return (List<Estudio>) repository.findAll();
    }

    @Override
    public Optional<Estudio> encontrarEstudioPorNombre(String nombreEstudio) {
        return repository.findEstudioByEstudio(nombreEstudio);
    }

    @Override
    public List<Estudio> encontrarPorNombreLike(String nombreEstudio) {
        return repository.findEstudioByEstudioContains(nombreEstudio);
    }

    @Override
    public VideojuegosPorEstudioResponse encontrarVideojuegosPorEstudio(int idEstudio) throws EstudioNotFoundException {
        List<Videojuego> videojuegos = videojuegoRespository.findVideojuegosByEstudio(idEstudio);
        VideojuegosPorEstudioResponse response;
        if(videojuegos.isEmpty()) {
            response = VideojuegosPorEstudioResponse
                    .builder()
                    .nombreEstudio(this.encontrarPorId(idEstudio)
                            .orElseThrow(() -> new EstudioNotFoundException("El estudio con id <" + idEstudio + "> noo existe"))
                            .getEstudio()
                    )
                    .videojuegos(new ArrayList<>())
                    .build();
        } else {
            response = VideojuegosPorEstudioResponse
                    .builder()
                    .nombreEstudio(videojuegos.get(0).getEstudioDesarrollador().getEstudio())
                    .videojuegos(videojuegoConverter.convertFromEntityList(videojuegos))
                    .build();
        }
        return response;
    }

    @Override
    public int guardarEstudio(Estudio estudio) {
        return repository.saveAndFlush(estudio).getIdEstudio();
    }

    @Override
    public void eliminarEstudio(int idEstudio) {
        repository.deleteById(idEstudio);
    }

    @Override
    public Optional<Estudio> encontrarPorId(int idEstudio) {
        return repository.findById(idEstudio);
    }


}
