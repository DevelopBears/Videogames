package org.grizzielicious.VideoGames.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serial;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="estudio")
public class Estudio implements Serializable {


    @Serial //TODO preguntar si es necesario dado que marca warn si no se tiene
    private static final long serialVersionUID = 1L;

    //todo añadir las validaciones
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudio")
    private int idEstudio;

    //todo añadir las validaciones
    @Column(name="nombre_estudio") //este nombre se tiene que emplear en el DAO
    private String estudio;

}

