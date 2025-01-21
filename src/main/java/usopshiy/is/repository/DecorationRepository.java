package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.ColonyItemKey;
import usopshiy.is.entity.Decoration;

@Repository
public interface DecorationRepository extends JpaRepository<Decoration, ColonyItemKey> {

    //TODO actually implement such procedure in PostrgeSQL
    //@Query(value = "CALL CREATE_DEC_BY_VALUES(:colonyId, :itemName, :amount)", nativeQuery = true)
    void createByValues(@Param("colonyId") Long colonyId, @Param("itemName") String itemName,  @Param("amount") int amount);
}
