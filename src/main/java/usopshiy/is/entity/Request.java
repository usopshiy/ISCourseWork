package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.dto.RequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "Requests")
@Builder
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @Column(name = "type", nullable = false)
    @NotBlank
    private String type;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "expected_completion_time")
    private LocalDate completionTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "details", nullable = false)
    @NotBlank
    private String details;

    @PrePersist
    public void prePersist() {
        creationDate = LocalDateTime.now();
    }

    public Request updateByDto(RequestDto dto) {
        this.type = dto.getType();
        this.completionTime = dto.getCompletionTime();
        this.details = dto.getDetails();
        return this;
    }

    public void setNextStatus() {
        switch (this.status) {
            case ON_ASSIGNMENT:
                this.status = Status.ASSIGNED;
                break;
            case ASSIGNED:
                this.status = Status.COMPLETED;
                break;
        }
    }
}
