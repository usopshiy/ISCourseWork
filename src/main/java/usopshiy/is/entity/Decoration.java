package usopshiy.is.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "decorations")
public class Decoration {

    @JsonIgnore
    @EmbeddedId
    private ColonyItemKey id;

    @JsonIgnore
    @ManyToOne
    @MapsId("colonyId")
    @JoinColumn(name = "colony_id")
    private Colony colony;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "amount", nullable = false)
    @Min(1)
    private int amount;
}
