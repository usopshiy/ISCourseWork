package usopshiy.is.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Sign in request")
public class SignInRequest {
    @Schema(description = "username", example = "user1")
    @Size(min = 1, max = 50, message = "username should contain from 1 to 50 characters")
    @NotBlank(message = "username cannot be empty")
    private String username;

    @Schema(description = "password", example = "vErYcOoLpAsS777")
    @Size(min = 4, max = 255, message = "password should be contain 4 to 255 characters")
    @NotBlank(message = "password cannot be empty")
    private String password;
}
