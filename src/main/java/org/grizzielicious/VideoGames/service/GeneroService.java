package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dto.Genero;

import java.util.List;
import java.util.Optional;

public interface GeneroService {
    public List<Genero> listarTodosLosGeneros();
    public Optional<Genero> encontrarGeneroPorId(int genero);
    public Optional<Genero> encontrarGeneroPorDescripcion(String descripcion);
    public int guardaGenero(Genero genero);
    public void eliminaGenero(Genero genero);
    //TODO cambiar elliminarGenero para borrado lógico. Cambios en BD. Borrado por boolean (lógico)
    //TODO el borrado en firme se hará en estudio (para diversión de PanPan)
    //TODO Planear la inclusión de precios para videojuegos. M:1 para tener precios por temporada. Borrado lógico por caducidad
    //TODO A PanPan le encanta molestarme con las consultas SQL, por lo cual me asigna hacer una consulta para un endpoint que obtenga los VJ por rango de precio
}
