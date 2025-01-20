package usopshiy.is.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import usopshiy.is.entity.Request;
import usopshiy.is.entity.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RequestDto {

    @JsonProperty
    private Long id;

    @NotBlank
    @JsonProperty
    private String creator;

    @JsonProperty
    private String assignee;

    @NotBlank
    @JsonProperty
    private String type;

    @NotNull
    @JsonProperty
    private LocalDateTime creationDate;

    @JsonProperty
    private LocalDate completionTime;

    @JsonProperty
    private Status status;

    @NotBlank
    @JsonProperty
    private String details;

    public RequestDto(Request obj) {
        this.id = obj.getId();
        this.creator = obj.getCreator().getUsername();
        if (obj.getAssignee() != null) {
            this.assignee = obj.getAssignee().getUsername();
        } else {
            this.assignee = null;
        }
        this.type = obj.getType();
        this.creationDate = obj.getCreationDate();
        this.completionTime = obj.getCompletionTime();
        this.status = obj.getStatus();
        this.details = obj.getDetails();
    }
}
