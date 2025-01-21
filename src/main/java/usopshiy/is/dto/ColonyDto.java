package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import usopshiy.is.entity.Colony;

import java.time.LocalDate;

@Data
public class ColonyDto {

    @JsonProperty
    private Long id;

    @NotBlank
    @JsonProperty
    private String name;

    @NotBlank
    @JsonProperty
    private LocalDate creationTimestamp;

    @Min(0)
    @JsonProperty
    private Long population;

    @NotNull
    @JsonProperty
    private String ant;

    @NotNull
    @JsonProperty
    private String formicarium;

    public ColonyDto(Colony obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.creationTimestamp = obj.getCreationTimestamp();
        this.population = obj.getPopulation();
        this.ant = obj.getAnt().getName();
        this.formicarium = obj.getFormicarium().getName();
    }
}
