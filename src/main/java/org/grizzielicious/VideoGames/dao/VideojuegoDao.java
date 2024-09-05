package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.dto.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideojuegoDao extends JpaRepository<Videojuego, Integer> {

    Optional<Videojuego> findVideojuegoByNombreVideojuego (@Param("nombre") String nombreVideojuego);

    List<Videojuego> findByEsMultijugador (@Param("esMultijugador") boolean esMultijugador);

    List<Videojuego> findVideojuegoByNombreVideojuegoLike(@Param("nombre") String nombreVideojuego);

    List<Videojuego> findAllByOrderByNombreVideojuego();

    @Query(value = "SELECT v.* FROM videojuego v "+
            "INNER JOIN plataforma_videojuego pv ON pv.id_videojuego = v.id_videojuego "+
            "INNER JOIN plataforma p ON p.id_plataforma = pv.id_plataforma "+
            "WHERE p.nombre_plataforma =:plataforma", nativeQuery = true)
    List<Videojuego> findByVideojuegoByPlataforma(String plataforma);

    @Query(value = "SELECT v.* FROM videojuego v "+
            "INNER JOIN genero g ON g.id_genero = v.id_genero "+
            "WHERE g.descripcion = :nombreGenero", nativeQuery = true)
    List<Videojuego> findVideojuegosByGenero(@Param("nombreGenero") String nombreGenero);

}
