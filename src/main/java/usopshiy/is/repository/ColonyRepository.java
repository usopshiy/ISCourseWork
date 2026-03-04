package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.Colony;

@Repository
public interface ColonyRepository extends JpaRepository<Colony, Long> {

}
