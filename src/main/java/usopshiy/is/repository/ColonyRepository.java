package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.Colony;

import java.util.List;

@Repository
public interface ColonyRepository extends JpaRepository<Colony, Long> {

}
