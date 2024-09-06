package org.grizzielicious.VideoGames.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int idGenero;

    //todo añadir las validaciones
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "is_active")
    @JsonIgnore
    private boolean estaActivo = true;

}
