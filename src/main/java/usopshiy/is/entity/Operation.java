package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity(name = "Operations")
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "colony_id", nullable = false)
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

    @OneToMany(mappedBy = "operation")
    Set<UsedItem> usedItems;

    @PrePersist
    private void prePersist() {
        creationDate = LocalDateTime.now();
    }
}
