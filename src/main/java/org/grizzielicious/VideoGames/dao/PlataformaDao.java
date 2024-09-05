package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.dto.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlataformaDao extends JpaRepository<Plataforma, Integer> {

    Optional<Plataforma> findByIdPlataforma(@Param("idPlataforma") int idPlataforma);

    Optional<Plataforma> findByPlataforma(@Param("plataforma") String Plataforma);

    List<Plataforma> findByPlataformaLike(String Plataforma);

    List<Plataforma> findAllByOrderByPlataforma();
}
