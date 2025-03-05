package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.Thermometer;

@Repository
public interface ThermometerRepository extends JpaRepository<Thermometer, Long> {

    @Query(value = "SELECT check_temps(:colonyId, :min, :max)", nativeQuery = true)
    boolean checkTemps(@Param("colonyId") Long colonyId, @Param("min") float min, @Param("min") float max);
}
