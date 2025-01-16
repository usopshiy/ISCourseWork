package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Thermometer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thermometer_id")
    private Long id;

    @Column(name = "value_timestamp")
    private java.time.LocalDateTime valueTimestamp;

    @Column(name = "value")
    private Float value;

    @Column(name="zone", nullable = false)
    @NotBlank
    private String zone;

    @PreUpdate
    public void preUpdate() {
        valueTimestamp = java.time.LocalDateTime.now();
    }
}
