package org.grizzielicious.VideoGames.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.CANNOT_BE_NULL_OR_EMPTY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genero")
public class Genero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //todo añadir las validaciones
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero")
    private int idGenero;

    //todo añadir las validaciones
    @Column(name = "descripcion")
    private String descripcion;

//    @Column(name = "is_active")
//    private boolean isActive;

}
