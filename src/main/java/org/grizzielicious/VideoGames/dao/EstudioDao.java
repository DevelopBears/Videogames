package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.dto.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstudioDao extends JpaRepository<Estudio, Integer> {

    List<Estudio> findAllByOrderByEstudio ();

    Optional<Estudio> findEstudioByEstudio (String nombreEstudio);

    List<Estudio> findAllByEstudioContains (String nombreEstudio);

    @Query(value = "SELECT v.* FROM videojuego v\n" +
            "INNER JOIN estudio e ON e.id_estudio = v.id_estudio\n" +
            "WHERE e.id_estudio = :nombreEstudio", nativeQuery = true )
    List<Estudio> findByDeveloperEstudio (String nombreEstudio);
}


//
//Añadir nuevas estudios
//Consultar todos los estudios OK
//Consultar estudio por nombre OK
//Consultar estudio por nombre like OK
//Consultar todos los videojuegos que ha desarrollado un estudio OK
//Eliminar estudio por id