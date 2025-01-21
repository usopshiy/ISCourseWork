package usopshiy.is.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.dto.ColonyDto;

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
    @JoinColumn(name = "ant_id")
    private AntSpecies ant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formicarium")
    private Formicarium formicarium;

    @OneToMany(mappedBy = "colony")
    private Set<Thermometer> colonyThermometers;

    @OneToMany(mappedBy = "colony")
    private Set<HumidityControl> colonyHumidities;

    @OneToMany(mappedBy = "colony")
    private Set<Decoration> decorations;

    @PrePersist
    public void prePersist() {
        creationTimestamp = java.time.LocalDate.now();
    }

    public Colony updateByDto(ColonyDto dto) {
        this.name = dto.getName();
        this.population = dto.getPopulation();
        return this;
    }
}
