package usopshiy.is.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import usopshiy.is.entity.Request;
import usopshiy.is.entity.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{

    @Query("SELECT p FROM Requests p WHERE p.creator = ?1 AND p.status != 'COMPLETED'")
    List<Request> getActiveRequestByCreator(User creator);

    @Query("SELECT p FROM Requests p WHERE p.assignee = ?1 AND p.status != 'COMPLETED'")
    List<Request> getActiveRequestByAssignee(User assignee);

    @Query("SELECT p FROM Requests p WHERE p.status = 'ON_ASSIGNMENT'")
    List<Request> getAwaitingRequests();
}
