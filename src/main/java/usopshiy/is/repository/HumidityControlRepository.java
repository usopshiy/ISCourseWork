package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.HumidityControl;

@Repository
public interface HumidityControlRepository extends JpaRepository<HumidityControl, Long> {

    //TODO: implement function in PostgreSQL
    //@Query(value = "CALL CHECK_HUMIDITIES(:colonyId, :min, :max)", nativeQuery = true)
    boolean checkHumidities(@Param("colonyId") Long colonyId, @Param("min") float min, @Param("min") float max);
}
