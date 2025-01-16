package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class AntSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ant_id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "optimal_temperature", nullable = false)
    @NotBlank
    private String optimalTemperature;

    @Column(name = "optimal_humidity", nullable = false)
    @NotBlank
    private String optimalHumidity;

    @ManyToMany
    @JoinTable(
            name = "Ant_diet",
            joinColumns = @JoinColumn(name = "ant_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private Set<Food> diet;
}
