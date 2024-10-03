package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.entities.Videojuego;

import java.util.List;
import java.util.Optional;

public interface VideojuegoService {



    public Optional<Videojuego> encontrarPorId(int idVideojuego);
    public List<Videojuego> encontrarTodos();
    public List<Videojuego> encontrarPorMultijugador(boolean esMultijugador);
    public Optional<Videojuego> encontrarVideojuegoPorNombre (String nombreVideojuego);
    public List<Videojuego> encontrarPorNombreLike(String nombreVideojuego);
    public List<Videojuego> encontrarPorNombrePlataforma(String plataforma);
    public List<Videojuego> encontrarPorGenero(String nombreGenero);
    public List<Videojuego> encontrarPorIdEstudio(int idEstudio);
    public List<Videojuego> encontrarPorNombreEstudio(String nombreEstudio);
    public List<Videojuego> encontrarPorIdPlataforma(int idPlataforma);
    public List<Videojuego> encontrarPorRangoPrecio(float precioMin, float precioMax);
    public void eliminarVideojuego (int idVideojuego);
    public int guardarVideojuego(Videojuego videojuego);
    public int guardarListaVideojuegos(List<Videojuego> videojuegoList);

}
