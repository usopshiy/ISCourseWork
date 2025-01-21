package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ValueDto {

    @JsonProperty
    private float value;
}
