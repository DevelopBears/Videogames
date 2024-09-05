package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.dto.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstudioDao extends JpaRepository<Estudio, Integer> {
    Optional<Estudio> findEstudioByEstudio(@Param("nombreEstudio") String nombreEstudio);

    //@Query(value = "SELECT e.* FROM estudio e WHERE e.nombre_estudio LIKE %:nombreEstudio%", nativeQuery = true)
    List<Estudio> findEstudioByEstudioLike(@Param("nombreEstudio") String nombreEstudio);

    List<Estudio> findAllByOrderByEstudio ();
}
