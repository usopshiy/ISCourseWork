package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByName(String name);
}
