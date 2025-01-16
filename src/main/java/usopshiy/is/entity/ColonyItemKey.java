package usopshiy.is.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ColonyItemKey implements Serializable {

    @Column(name = "colony_id")
    private Long colonyId;

    @Column(name = "item_id")
    private Long itemId;
}
