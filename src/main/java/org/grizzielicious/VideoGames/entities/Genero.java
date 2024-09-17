package org.grizzielicious.VideoGames.entities;

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

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.grizzielicious.VideoGames.constants.ValidationsConstants.LENGTH_NOT_VALID25;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genero")
public class Genero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int idGenero;

    @NotEmpty(message = "descripcionGenero" + CANNOT_BE_NULL_OR_EMPTY)
    @Size(min = 3, max = 25, message = "descripcionGenero" + LENGTH_NOT_VALID25)
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "is_active")
    @JsonIgnore
    private boolean estaActivo = true;

}
