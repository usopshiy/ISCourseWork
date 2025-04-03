package usopshiy.is.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.dto.OperationDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "Operations")
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "colony_id")
    private Colony colony;

    @Column(name = "type", nullable = false)
    @NotBlank
    private String type;

    @Column(name = "stage", nullable = false)
    @Min(0)
    private int stage;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

    @Column(name = "stage_description")
    private String stageDescription;

    @JsonIgnore
    @OneToMany(mappedBy = "operation")
    List<UsedItem> usedItems;

    @PrePersist
    private void prePersist() {
        creationDate = LocalDateTime.now();
        stage = 0;
    }

    public Operation updateByDto(OperationDto dto) {
        this.type = dto.getType();
        return this;
    }
}
