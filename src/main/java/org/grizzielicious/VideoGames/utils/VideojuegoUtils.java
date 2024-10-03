package org.grizzielicious.VideoGames.utils;

import org.grizzielicious.VideoGames.entities.Estudio;
import org.grizzielicious.VideoGames.entities.Genero;
import org.grizzielicious.VideoGames.entities.Plataforma;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.EstudioNotFoundException;
import org.grizzielicious.VideoGames.exceptions.GeneroNotFoundException;
import org.grizzielicious.VideoGames.exceptions.PlataformaNotFoundException;
import org.grizzielicious.VideoGames.service.EstudioService;
import org.grizzielicious.VideoGames.service.GeneroService;
import org.grizzielicious.VideoGames.service.PlataformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VideojuegoUtils {

    @Autowired
    private PlataformaService plataformaService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private EstudioService estudioService;

    public void fixedVideogameReference (Videojuego videojuego) throws EstudioNotFoundException,
            GeneroNotFoundException, PlataformaNotFoundException {
        Estudio fixedEstudio = estudioService.encontrarEstudioPorNombre(videojuego.getEstudioDesarrollador()
                        .getEstudio()).orElseThrow(() -> new EstudioNotFoundException("El estudio con nombre "
                        + videojuego.getEstudioDesarrollador().getEstudio()
                        + " no se encuentra en la BD"));
        Genero fixedGenero = generoService.encontrarGeneroPorDescripcion(videojuego.getGenero().getDescripcion())
                .orElseThrow(() -> new GeneroNotFoundException("El género <" + videojuego.getGenero().getDescripcion()
                        + "> no existe en la base de datos"));
        List<Plataforma> fixedPlataformaList = new ArrayList<>();
        for(Plataforma p : videojuego.getPlataformas()) {
            fixedPlataformaList.add(
                    plataformaService.encontrarPlataformaPorNombre(p.getPlataforma())
                            .orElseThrow(() -> new PlataformaNotFoundException("La plataforma con nombre <"
                                    + p.getPlataforma() + "> no existe enn la BD"))
            );
        }
        videojuego.setGenero(fixedGenero);
        videojuego.setEstudioDesarrollador(fixedEstudio);
        videojuego.setPlataformas(fixedPlataformaList);


    }
}
