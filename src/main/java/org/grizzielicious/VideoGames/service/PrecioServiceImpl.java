package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dao.PrecioDao;
import org.grizzielicious.VideoGames.entities.Precio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrecioServiceImpl implements PrecioService{

    @Autowired
    private PrecioDao repository;

    @Override
    public List<Precio> listarPreciosPorFecha(LocalDateTime fecha) {
        return repository.findAllByDate(fecha);
    }

    @Override
    public Optional<Precio> listarPreciosPorFechaVideojuego(LocalDateTime fecha, int idVideojuego) {
        return repository.findAllByCurrentDate(fecha, idVideojuego);
    }

    @Override
    public Optional<Precio> encontrarPorId(int idPrecio) {
        return repository.findById(idPrecio);
    }

    @Override
    public int guardarPrecio(Precio precio) {
        return repository.saveAndFlush(precio).getIdPrecio();
    }

    @Override
    public List<Precio> encontrarPreciosActivos() {
        return repository.findAllByDate(LocalDateTime.now());
    }

    @Override
    public Optional<Precio> encontrarPrecioActivoParaVideojuego(int idVideojuego) {
        return repository.findAllByCurrentDate(LocalDateTime.now(),idVideojuego);
    }

    @Override
    public int guardarListaDePrecios(List<Precio> lista) {
        return repository.saveAllAndFlush(lista).size();
    }


}
