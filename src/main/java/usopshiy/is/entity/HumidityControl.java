package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class HumidityControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
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
