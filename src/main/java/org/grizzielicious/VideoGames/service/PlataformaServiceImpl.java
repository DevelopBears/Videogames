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
    private PlataformaDao plataformaDao;


    @Override
    public List<Plataforma> listarTodasLasPlataformas() {
        return List.of();
    }

    @Override
    public Optional<Plataforma> encontrarPlataformaPorId(Plataforma plataforma) {
        return plataformaDao.findByIdPlataforma(plataforma.getIdPlataforma());
    }

    @Override
    public Optional<Plataforma> encontrarPlataformaPorNombre(String plataforma) {
        return plataformaDao.findByPlataforma(plataforma);
    }
}
