package usopshiy.is.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.dto.ColonyDto;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "ant_id")
    private AntSpecies ant;

    @ManyToOne
    @JoinColumn(name = "formicarium")
    private Formicarium formicarium;

    @OneToMany(mappedBy = "colony")
    private List<Thermometer> colonyThermometers;

    @OneToMany(mappedBy = "colony")
    private List<HumidityControl> colonyHumidities;

    @OneToMany(mappedBy = "colony")
    private List<Decoration> decorations;

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
