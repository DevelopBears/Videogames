package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.dto.Genero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GeneroDao extends CrudRepository<Genero, Integer> {
        Optional<Genero> findGeneroByDescripcion(@Param("descripcion") String descripcion);
        List<Genero> findAllByEstaActivoIsTrueOrderByDescripcion();

}
