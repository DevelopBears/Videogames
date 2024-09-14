package org.grizzielicious.VideoGames.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.grizzielicious.VideoGames.constants.ValidationsConstants.LENGTH_NOT_VALID25;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plataforma")
public class Plataforma implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //todo realizar las validaciones
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_plataforma")
    private int idPlataforma;

    @NotEmpty(message = "plataforma" + CANNOT_BE_NULL_OR_EMPTY)
    @Size(min = 3, max = 25, message = "plataforma" + LENGTH_NOT_VALID25)
    @Column(name = "nombre_plataforma")
    private String plataforma;

    @JsonIgnore
    @ManyToMany(mappedBy = "plataformas")
    private List<Videojuego> videojuegos;

}
