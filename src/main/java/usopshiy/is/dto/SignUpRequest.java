package usopshiy.is.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import usopshiy.is.entity.Role;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "request for sign up")
public class SignUpRequest {

    @Schema(description = "username", example = "user1")
    @Size(min = 1, max = 50, message = "username should contain from 1 to 50 characters")
    @NotBlank(message = "username cannot be empty")
    private String username;

    @Schema(description = "password", example = "vErYcOoLpAsS777")
    @Size(min = 4, max = 255, message = "password should be contain 4 to 255 characters")
    @NotBlank(message = "password cannot be empty")
    private String password;

    @Schema(description = "User role", example = "OPERATOR")
    @Enumerated(EnumType.STRING)
    private Role role;
}
