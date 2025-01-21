package usopshiy.is.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import usopshiy.is.dto.ItemDto;

@Data
@NoArgsConstructor
@Entity(name = "Items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "stored", nullable = false)
    @Min(0)
    private int stored;

    public Item updateByDto(ItemDto dto){
        this.name = dto.getName();
        this.stored = dto.getStored();
        return this;
    }
}
