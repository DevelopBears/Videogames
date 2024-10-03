package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.entities.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideojuegoDao extends JpaRepository<Videojuego, Integer> {

    List<Videojuego> findAllByOrderByNombreVideojuego();

    List<Videojuego> findByEsMultijugador (@Param("esMultijugador") boolean esMultijugador);

    Optional<Videojuego> findVideojuegoByNombreVideojuego (@Param("nombre") String nombreVideojuego);

    List<Videojuego> findVideojuegoByNombreVideojuegoContains(@Param("nombre") String nombreVideojuego);

    @Query(value = "SELECT v.* FROM videojuego v "+
            "INNER JOIN plataforma_videojuego pv ON pv.id_videojuego = v.id_videojuego "+
            "INNER JOIN plataforma p ON p.id_plataforma = pv.id_plataforma "+
            "WHERE p.nombre_plataforma =:plataforma", nativeQuery = true)
    List<Videojuego> findByVideojuegoByPlataforma(String plataforma);

    @Query(value = "SELECT v.* FROM videojuego v "+
            "INNER JOIN genero g ON g.id_genero = v.id_genero "+
            "WHERE g.descripcion = :nombreGenero", nativeQuery = true)
    List<Videojuego> findVideojuegosByGenero(@Param("nombreGenero") String nombreGenero);

    @Query(value = "SELECT v.* FROM videojuego v " +
            "INNER JOIN estudio e ON e.id_estudio = v.id_estudio " +
            "WHERE e.nombre_estudio = :nombreEstudio", nativeQuery = true)
    List<Videojuego> findVideojuegosByEstudio(String nombreEstudio);

    @Query(value = "SELECT v.* FROM videojuego v " +
            "WHERE v.id_estudio = :idEstudio", nativeQuery = true)
    List<Videojuego> findVideojuegosByEstudio(int idEstudio);

    @Query(value = "SELECT v.* " +
            "FROM videojuego v " +
            "INNER JOIN plataforma_videojuego pv ON pv.id_videojuego = v.id_videojuego " +
            "WHERE pv.id_plataforma = :idPlataforma", nativeQuery = true)
    List<Videojuego> findAllRelatedByPlataforma (int idPlataforma);

    @Query(value = "SELECT v.* " +
            "FROM videojuego v " +
            "INNER JOIN precios p ON p.id_videojuego = v.id_videojuego " +
            "        AND p.fecha_inicio_vigencia <= CURRENT_DATE " +
            "        AND (p.fecha_fin_vigencia IS NULL OR p.fecha_fin_vigencia >= CURRENT_DATE) " +
            "WHERE p.precio_unitario BETWEEN :precioMin AND :precioMax", nativeQuery = true)
    List<Videojuego> findVideojuegoByPriceRange(float precioMin, float precioMax);
}
