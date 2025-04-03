package usopshiy.is.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private float value;

    @Column(name="zone", nullable = false)
    @NotBlank
    private String zone;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Colony colony;

    @PreUpdate
    public void preUpdate() {
        valueTimestamp = java.time.LocalDateTime.now();
    }
}
