package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "decorations")
public class Decoration {

    @EmbeddedId
    private ColonyItemKey id;

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
