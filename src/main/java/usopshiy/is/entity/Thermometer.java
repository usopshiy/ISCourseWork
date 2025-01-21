package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Thermometers")
public class Thermometer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thermometer_id")
    private Long id;

    @Column(name = "value_timestamp")
    private java.time.LocalDateTime valueTimestamp;

    @Column(name = "value")
    private float value;

    @Column(name="zone", nullable = false)
    @NotBlank
    private String zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Colony colony;

    @PreUpdate
    public void preUpdate() {
        valueTimestamp = java.time.LocalDateTime.now();
    }
}
