package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.entities.Precio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrecioService {
    public List<Precio> listarPreciosPorFecha (LocalDateTime fecha);
    public Optional<Precio> listarPreciosPorFechaVideojuego (LocalDateTime fecha, int idVideojuego);
    public Optional<Precio> encontrarPorId(int idPrecio);
    public int guardarPrecio (Precio precio);
    public List<Precio> encontrarPreciosActivos();
    public Optional<Precio> encontrarPrecioActivoParaVideojuego(int idVideojuego);
    public int guardarListaDePrecios (List<Precio> lista);
}
