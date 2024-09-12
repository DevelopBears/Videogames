package org.grizzielicious.VideoGames.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;


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
    @Column(name = "id_plataforma")
    private int idPlataforma;

    //todo realizar las validaciones
    @Column(name = "nombre_plataforma")
    private String plataforma;

    @JsonIgnore
    @ManyToMany(mappedBy = "plataformas")
    private List<Videojuego> videojuegos;

}
