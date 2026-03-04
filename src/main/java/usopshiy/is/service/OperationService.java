package usopshiy.is.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.OperationDto;
import usopshiy.is.entity.*;
import usopshiy.is.exception.NotFoundException;
import usopshiy.is.operations.OperationRealization;
import usopshiy.is.repository.OperationRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final ColonyService colonyService;
    private final RequestService requestService;
    private final UserService userService;

    // Spring injects all OperationRealization beans as a list automatically.
    // We immediately index them by type for O(1) lookup.
    private final Map<String, OperationRealization> allOperations;

    public OperationService(
            OperationRepository operationRepository,
            ColonyService colonyService,
            RequestService requestService,
            UserService userService,
            List<OperationRealization> operationRealizations
    ) {
        this.operationRepository = operationRepository;
        this.colonyService = colonyService;
        this.requestService = requestService;
        this.userService = userService;
        this.allOperations = operationRealizations.stream()
                .collect(Collectors.toMap(OperationRealization::getType, Function.identity()));
    }

    @Transactional
    public Operation startOperation(OperationDto dto) {
        Operation operation = new Operation().updateByDto(dto);

        if (dto.getColony_id() != null) {
            Colony colony = colonyService.getColonyById(dto.getColony_id());
            operation.setColony(colony);
        }

        Request request = dto.getRequest_id() != null
                ? requestService.getById(dto.getRequest_id())
                : requestService.createSelf(operation);
        operation.setRequest(request);

        return progress(operation);
    }

    public Operation progressById(Long id) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Operation not found: " + id));
        return progress(operation);
    }

    public Operation progress(Operation operation) {
        OperationRealization realization = allOperations.get(operation.getType());
        if (realization == null) {
            throw new NotFoundException("Unknown operation type: " + operation.getType());
        }
        return realization.executeStage(operation);
    }

    public List<Operation> getAllUserOperations() {
        User currentUser = userService.getCurrentUser();
        return operationRepository.findAll().stream()
                .filter(op -> op.getRequest() != null
                        && op.getRequest().getCreator().equals(currentUser)
                        && op.getRequest().getStatus() != Status.COMPLETED)
                .collect(Collectors.toList());
    }
}