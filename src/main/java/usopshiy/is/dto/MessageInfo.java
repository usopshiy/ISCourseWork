package usopshiy.is.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "string transfer")
public class MessageInfo {

    @Schema(description = "Any string", example = "Hello, world!")
    private String message;
}
