package org.grizzielicious.VideoGames.dao;

import org.grizzielicious.VideoGames.entities.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrecioDao extends JpaRepository<Precio, Integer> {
    @Query(value = "SELECT p.* FROM precios p "+
            "WHERE p.fecha_inicio_vigencia < :fecha " +
            "AND (p.fecha_fin_vigencia > :fecha OR p.fecha_fin_vigencia is null) ", nativeQuery = true)
    List<Precio> findAllByDate (LocalDateTime fecha);

    @Query(value = "SELECT p.* FROM precios p "+
            "WHERE p.fecha_inicio_vigencia < :fecha " +
            "AND (p.fecha_fin_vigencia > :fecha OR p.fecha_fin_vigencia is null) " +
            "AND p.id_videojuego = :idVideojuego", nativeQuery = true)
    Optional<Precio> findAllByCurrentDate (LocalDateTime fecha, int idVideojuego);

    Optional<Precio> findById(int idPrecio);

    @Query(value = "SELECT p.* FROM precios p " +
            "WHERE p.id_videojuego = :idVideojuego " +
            "AND (fecha_fin_vigencia IS NULL OR fecha_fin_vigencia >= :inicioVigencia) " +
            "AND (:finVigencia IS NULL || p.fecha_inicio_vigencia < :finVigencia)", nativeQuery = true)
    List<Precio> findConflictingPrices(int idVideojuego, LocalDateTime inicioVigencia, LocalDateTime finVigencia);
}
