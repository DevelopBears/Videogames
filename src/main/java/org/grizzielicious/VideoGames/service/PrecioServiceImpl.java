package org.grizzielicious.VideoGames.service;

import jakarta.transaction.Transactional;
import org.grizzielicious.VideoGames.dao.PrecioDao;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    public Optional<Precio> encontrarPrecioPorFechaVideojuego(LocalDateTime fecha, int idVideojuego) {
        return repository.findAllByCurrentDate(fecha, idVideojuego);
    }

    @Override
    public Optional<Precio> encontrarPorId(int idPrecio) {
        return repository.findById(idPrecio);
    }

    @Override
    public int guardarPrecio(Precio precio) throws InvalidParameterException {
        this.validateDates(precio);
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
    @Transactional
    public int guardarListaDePrecios(List<Precio> lista) throws InvalidParameterException {
        for(Precio p : lista) {
            this.validateDates(p);
        }
        return repository.saveAllAndFlush(lista).size();
    }

    @Override
    public List<Precio> encontrarPreciosEnConflicto(int idVideojuego, LocalDateTime inicioVigencia, LocalDateTime finVigencia) {
        return repository.findConflictingPrices(idVideojuego, inicioVigencia, finVigencia);
    }

    private void validateDates(Precio precio) throws InvalidParameterException {
        if (Objects.nonNull(precio.getFechaFinVigencia()) && precio.getFechaInicioVigencia().
                isAfter(precio.getFechaFinVigencia())){
            throw new InvalidParameterException("La fecha de fin de vigencia no puede ser previa a la de inicio");
        }
    }





}
