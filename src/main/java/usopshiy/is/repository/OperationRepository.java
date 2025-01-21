package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
