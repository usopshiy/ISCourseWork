package usopshiy.is.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
@Builder
@AllArgsConstructor
public class OperationItemKey implements Serializable {

    @Column(name = "operation_id")
    private Long operationId;

    @Column(name = "item_id")
    private Long itemId;
}
