package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDto {

    @JsonProperty
    private Long request_id;

    @JsonProperty
    private Long colony_id;

    @JsonProperty
    @NotBlank
    private String type;
}
