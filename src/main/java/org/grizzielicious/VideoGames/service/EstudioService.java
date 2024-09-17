package org.grizzielicious.VideoGames.service;

import org.grizzielicious.VideoGames.dto.Estudio;

import java.util.List;
import java.util.Optional;

public interface EstudioService {
    public List<Estudio> listarTodosLosEstudios();
    public Optional<Estudio> encontrarEstudioPorId(int idEstudio);
    public Optional<Estudio> encontrarEstudioPorNombre(String nombreEstudio);
    public List<Estudio> encontrarEstudioPorNombreLike(String nombreEstudio);
    public List<Estudio> encontrarVideojuegoPorEstudio(String nombreEstudio);
    public int guardarEstudio(Estudio estudio);
    public void eliminarEstudio(int idEstudio);

}


//Añadir nuevas estudios
//Consultar todos los estudios OK
//Consultar estudio por nombre OK
//Consultar estudio por nombre like OK
//Consultar todos los videojuegos que ha desarrollado un estudio OK
//Eliminar estudio por id