package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.entity.Item;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    @JsonProperty
    @NotBlank
    private String name;

    @JsonProperty
    @Min(0)
    private int stored;

    public ItemDto(Item obj) {
        this.name = obj.getName();
        this.stored = obj.getStored();
    }
}
