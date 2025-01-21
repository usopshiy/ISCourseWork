package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import usopshiy.is.entity.Colony;

@Data
public class ColonyDto {

    @JsonProperty
    private Long id;

    @NotBlank
    @JsonProperty
    private String name;

    @Min(0)
    @JsonProperty
    private Long population;

    public ColonyDto(Colony obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.population = obj.getPopulation();
    }
}
