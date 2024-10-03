package org.grizzielicious.VideoGames.service;

import jakarta.transaction.Transactional;
import org.grizzielicious.VideoGames.dao.VideojuegoDao;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideojuegoServiceImpl implements VideojuegoService {

    @Autowired
    private VideojuegoDao repository;

    @Override
    public Optional<Videojuego> encontrarVideojuegoPorNombre(String nombreVideojuego) {
        return repository.findVideojuegoByNombreVideojuego(nombreVideojuego);
    }

    @Override
    public List<Videojuego> encontrarPorNombreLike(String nombreVideojuego) {
        return repository.findVideojuegoByNombreVideojuegoContains(nombreVideojuego);
    }

    @Override
    public List<Videojuego> encontrarPorNombrePlataforma(String plataforma) {
        return repository.findByVideojuegoByPlataforma(plataforma);
    }

    @Override
    public List<Videojuego> encontrarPorGenero(String nombreGenero) {
        return repository.findVideojuegosByGenero(nombreGenero);
    }

    @Override
    public List<Videojuego> encontrarPorIdEstudio(int idEstudio) {
        return repository.findVideojuegosByEstudio(idEstudio);
    }

    @Override
    public List<Videojuego> encontrarPorNombreEstudio(String nombreEstudio) {
        return repository.findVideojuegosByEstudio(nombreEstudio);
    }

    @Override
    public Optional<Videojuego> encontrarPorId(int idVideojuego) {
        return repository.findById(idVideojuego);
    }

    @Override
    public List<Videojuego> encontrarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Videojuego> encontrarPorMultijugador(boolean esMultijugador) {
        return repository.findByEsMultijugador(esMultijugador);
    }

    @Override
    public List<Videojuego> encontrarPorIdPlataforma(int idPlataforma) {
        return repository.findAllRelatedByPlataforma(idPlataforma);
    }

    @Override
    public List<Videojuego> encontrarPorRangoPrecio(float precioMin, float precioMax) {
        return repository.findVideojuegoByPriceRange(precioMin, precioMax);
    }

    @Transactional
    @Override
    public void eliminarVideojuego(int idVideojuego) {
        repository.deleteById(idVideojuego);
    }

    @Override
    public int guardarVideojuego(Videojuego videojuego) {
        return repository.saveAndFlush(videojuego).getIdVideojuego();
    }

    @Override
    public int guardarListaVideojuegos(List<Videojuego> videojuegoList) {
        return repository.saveAllAndFlush(videojuegoList).size();
    }
}
