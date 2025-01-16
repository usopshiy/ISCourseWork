package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "Requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "asignee")
    private User assignee;

    @Column(name = "type", nullable = false)
    @NotBlank
    private String type;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "expected_completion_time")
    private LocalDate completionTime;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "details", nullable = false)
    @NotBlank
    private String details;

    @PrePersist
    public void prePersist() {
        creationDate = LocalDateTime.now();
    }
}
