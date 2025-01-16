package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Formicarium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "formicarium_id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "type", nullable = false)
    @NotBlank
    private String type;

    @Column(name = "stored", nullable = false)
    @Min(0)
    private int stored;
}
