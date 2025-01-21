package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OperationDto {

    @JsonProperty
    private Long Request_id;

    @JsonProperty
    private Long Colony_id;

    @JsonProperty
    @NotBlank
    private String type;
}
