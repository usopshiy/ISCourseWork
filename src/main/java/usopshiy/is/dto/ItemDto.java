package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import usopshiy.is.entity.Item;

@Data
public class ItemDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotBlank
    private String name;

    @JsonProperty
    @Min(0)
    private int stored;

    public ItemDto(Item obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.stored = obj.getStored();
    }
}
