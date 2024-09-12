package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.dto.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlataformaDao extends JpaRepository<Plataforma, Integer> {

    Optional<Plataforma> findByIdPlataforma(@Param("idPlataforma") int idPlataforma);

    Optional<Plataforma> findByPlataforma(@Param("plataforma") String Plataforma);

    List<Plataforma> findByPlataformaContains(String Plataforma); //THE Jr has beaten up a Sr!!! Ü

    List<Plataforma> findAllByOrderByPlataforma();

    @Query(value = "SELECT * FROM plataforma p " +
            "INNER JOIN plataforma_videojuego pv ON pv.id_plataforma = p.id_plataforma " +
            "WHERE pv.id_videojuego = :idVideojuego", nativeQuery = true)
    List<Plataforma> findByVideogameAvailability(int idVideojuego);
}
