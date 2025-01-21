package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usopshiy.is.dto.RequestDto;
import usopshiy.is.entity.Operation;
import usopshiy.is.entity.Request;
import usopshiy.is.entity.Status;
import usopshiy.is.entity.User;
import usopshiy.is.repository.RequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    public Request create(RequestDto dto){
        Request obj = new Request().updateByDto(dto);
        obj.setCreator(userService.getCurrentUser());
        obj.setStatus(Status.ON_ASSIGNMENT);
        return requestRepository.save(obj);
    }

    public Request getById(Long id){
        Request request = requestRepository.findById(id).orElse(null);
        if(request == null){
            throw new RuntimeException("Request not found");
        }
        return request;
    }

    public void update(RequestDto dto){
        Request request = requestRepository.findById(dto.getId()).orElse(null);
        User user = userService.getCurrentUser();
        if (request == null) {
            throw new RuntimeException("No such request");
        }
        request.setAssignee(user);
        request.setNextStatus();
        if (dto.getCompletionTime() != null) {
            request.setCompletionTime(dto.getCompletionTime());
        }
        requestRepository.save(request);
    }

    public Request createSelf(Operation operation) {
        Request request = Request.builder()
                .creator(userService.getCurrentUser())
                .assignee(userService.getCurrentUser())
                .type(operation.getType())
                .status(Status.ASSIGNED)
                .details("complete an operation you started")
                .build();
        return requestRepository.save(request);
    }

    public List<Request> getRequests() {
        return requestRepository.findAll();
    }

    public List<Request> getRequestsByCreator() {
        User user = userService.getCurrentUser();
        return requestRepository.getActiveRequestByCreator(user);
    }

    public List<Request> getRequestsByAssignee() {
        User user = userService.getCurrentUser();
        return requestRepository.getActiveRequestByAssignee(user);
    }

    public List<Request> getAwaitingRequests() {
        return requestRepository.getAwaitingRequests();
    }
}
