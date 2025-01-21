package usopshiy.is.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.OperationDto;
import usopshiy.is.entity.*;
import usopshiy.is.operations.ColonyStart;
import usopshiy.is.operations.CreateIncubator;
import usopshiy.is.operations.OperationRealization;
import usopshiy.is.repository.OperationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final CreateIncubator createIncubator;
    private final ColonyStart colonyStart;
    private final ColonyService colonyService;
    private final RequestService requestService;
    private final UserService userService;


    private HashMap<String, OperationRealization> allOperations;

    @PostConstruct
    private void init() {
        allOperations = new HashMap<>(){
            {
                put("createIncubator", createIncubator);
                put("startColony", colonyStart);
            }
        };
    }

    @Transactional
    public Operation startOperation(OperationDto dto) {
        Operation operation = new Operation().updateByDto(dto);

        if (dto.getColony_id() != null) {
            Colony colony = colonyService.getColonyById(dto.getColony_id());
            operation.setColony(colony);
        }

        Request request;
        if (dto.getRequest_id() != null) {
            request = requestService.getById(dto.getRequest_id());
        }
        else {
            request = requestService.createSelf(operation);
        }
        operation.setRequest(request);

        return progress(operation);
    }

    public Operation progressById(Long id) {
        Operation operation = operationRepository.findById(id).orElse(null);
        if (operation == null) {
            throw new RuntimeException("Operation not found");
        }
        return progress(operation);
    }

    public Operation progress(Operation operation) {
        return allOperations.get(operation.getType()).executeStage(operation);
    }

    public List<Operation> getAllUserOperations() {
        List<Operation> operations = operationRepository.findAll();
        User currentUser = userService.getCurrentUser();
        try {
            operations.stream()
                    .filter(item -> item.getRequest().getCreator() == currentUser && item.getRequest().getStatus() != Status.COMPLETED)
                    .collect(Collectors.toUnmodifiableList());
        }
        catch (NullPointerException e) {
            return new ArrayList<>();
        }
        return operations;
    }
}
