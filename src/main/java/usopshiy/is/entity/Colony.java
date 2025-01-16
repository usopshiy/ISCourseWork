package usopshiy.is.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity(name = "Colony")
@Data
@NoArgsConstructor
public class Colony {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colony_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_timestamp")
    private java.time.LocalDate creationTimestamp;

    @Column(name = "population")
    private Long population;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ant")
    private AntSpecies ant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formicarium")
    private Formicarium formicarium;

    @ManyToMany
    @JoinTable(
            name = "Colony_thermos",
            joinColumns = @JoinColumn(name = "colony_id"),
            inverseJoinColumns = @JoinColumn(name = "thermometer_id")
    )
    private Set<Thermometer> colonyThermometers;

    @ManyToMany
    @JoinTable(
            name = "Colony_humidities",
            joinColumns = @JoinColumn(name = "colony_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    private Set<HumidityControl> colonyHumidities;

    @OneToMany(mappedBy = "colony")
    private Set<Decoration> decorations;

    @PrePersist
    public void prePersist() {
        creationTimestamp = java.time.LocalDate.now();
    }
}
