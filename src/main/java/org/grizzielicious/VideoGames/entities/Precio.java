package org.grizzielicious.VideoGames.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.grizzielicious.VideoGames.constants.ValidationsConstants.PRICE_OUT_OF_RANGE;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="precios")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Precio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @NotEmpty(message = "idPrecio" + CANNOT_BE_NULL_OR_EMPTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_precio")
    private int idPrecio;

    @NotEmpty(message = "precioUnitario" + CANNOT_BE_NULL_OR_EMPTY)
    @Range(min = 0, max = 8000, message = PRICE_OUT_OF_RANGE)
    @Column(name = "precio_unitario", columnDefinition = "DECIMAL(8,2)")
    private float precioUnitario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GTM-6")
    @NotEmpty(message = "fechaInicioVigencia" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "fecha_inicio_vigencia")
    private LocalDateTime fechaInicioVigencia;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GTM-6")
    @Column(name = "fecha_fin_vigencia")
    private LocalDateTime fechaFinVigencia;

    @ManyToOne
    @NotEmpty(message = "idVideojuego" + CANNOT_BE_NULL_OR_EMPTY)
    @JoinColumn(name = "id_videojuego")
    private Videojuego videojuego;

    @Column(name = "fecha_creacion")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_ultima_mod")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaUltimaMod;

    @PrePersist //lanza evento antes de realizar una inserción
    private void validateDates() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaUltimaMod = LocalDateTime.now();
    }

    @PreUpdate //lanza evento antes de realizar un update
    private void validateUpdateDate() {
        this.fechaUltimaMod = LocalDateTime.now();
    }
}
