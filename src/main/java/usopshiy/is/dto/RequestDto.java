package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.entity.Request;
import usopshiy.is.entity.Status;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @JsonProperty
    private Long id;

    @NotBlank
    @JsonProperty
    private String type;

    @JsonProperty
    private LocalDate completionTime;

    @JsonProperty
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotBlank
    @JsonProperty
    private String details;

    public RequestDto(Request obj) {
        this.id = obj.getId();
        this.type = obj.getType();
        this.status = obj.getStatus();
        this.details = obj.getDetails();
    }
}
