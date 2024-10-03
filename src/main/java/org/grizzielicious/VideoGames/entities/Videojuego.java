package org.grizzielicious.VideoGames.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="videojuego")
public class Videojuego implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_videojuego")
    private int idVideojuego;

    @NotEmpty(message = "nombreVideojuego" + CANNOT_BE_NULL_OR_EMPTY)
    @Size(min = 3, max = 50, message = "nombreVideojuego" + LENGTH_NOT_VALID50)
    @Column(name = "nombre_videojuego")
    private String nombreVideojuego;

    @Min(value = 1988, message = YEAR_OUT_OF_RANGE)
    @Column(name = "anio_lanzamiento")
    private int anioLanzamiento;

    @ManyToOne
    @JoinColumn(name = "id_estudio")
    private Estudio estudioDesarrollador;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Genero genero;

    @Column(name = "es_multijugador")
    private boolean esMultijugador;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "plataforma_videojuego",
            joinColumns = @JoinColumn(name = "id_videojuego", referencedColumnName = "id_videojuego", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma", referencedColumnName = "id_plataforma", nullable = false)
    )
    private List<Plataforma> plataformas;
}