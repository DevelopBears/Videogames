package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.entities.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudioDao extends JpaRepository<Estudio, Integer> {
    Optional<Estudio> findEstudioByEstudio (String nombreEstudio);

    List<Estudio> findEstudioByEstudioContains (String nombreEstudio);

    List<Estudio> findAllByOrderByEstudio ();




}
