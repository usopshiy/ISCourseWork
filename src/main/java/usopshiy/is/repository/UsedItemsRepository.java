package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.OperationItemKey;
import usopshiy.is.entity.UsedItem;

@Repository
public interface UsedItemsRepository extends JpaRepository<UsedItem, OperationItemKey> {

}
