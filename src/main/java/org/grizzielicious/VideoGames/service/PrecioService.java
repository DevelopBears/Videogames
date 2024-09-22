package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrecioService {
    public List<Precio> listarPreciosPorFecha (LocalDateTime fecha);
    public Optional<Precio> encontrarPrecioPorFechaVideojuego(LocalDateTime fecha, int idVideojuego);
    public Optional<Precio> encontrarPorId(int idPrecio);
    public int guardarPrecio (Precio precio) throws InvalidParameterException;
    public List<Precio> encontrarPreciosActivos();
    public Optional<Precio> encontrarPrecioActivoParaVideojuego(int idVideojuego);
    public int guardarListaDePrecios (List<Precio> lista) throws InvalidParameterException;
}
