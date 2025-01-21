package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DecorationDto {

    @JsonProperty
    @NotBlank
    private String itemName;

    @JsonProperty
    @Min(1)
    private int amount;
}
