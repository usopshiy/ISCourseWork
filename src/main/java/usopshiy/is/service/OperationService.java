package usopshiy.is.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.OperationDto;
import usopshiy.is.entity.*;
import usopshiy.is.exception.*;
import usopshiy.is.operations.ColonyStart;
import usopshiy.is.operations.CreateIncubator;
import usopshiy.is.operations.OperationRealization;
import usopshiy.is.repository.OperationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления операциями и сценариями в колонии муравьев.
 * Отвечает за запуск, выполнение и отслеживание прогресса различных операций,
 * таких как создание инкубатора, запуск колонии и другие сценарии управления.
 */
@Slf4j
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

    /**
     * Инициализирует карту доступных операций после создания бина.
     * Сопоставляет строковые идентификаторы операций с их реализациями.
     */
    @PostConstruct
    private void init() {
        log.debug("Initializing operations map");
        allOperations = new HashMap<>() {
            {
                put("createIncubator", createIncubator);
                put("startColony", colonyStart);
            }
        };
        log.info("Operations map initialized with {} operations", allOperations.size());
    }

    /**
     * Запускает новую операцию на основе предоставленных данных.
     * <p>
     * Процесс включает:
     * <ul>
     *   <li>Создание операции из DTO</li>
     *   <li>Привязку к колонии (если указана)</li>
     *   <li>Создание или привязку запроса</li>
     *   <li>Запуск выполнения операции</li>
     * </ul>
     *
     * @param dto объект передачи данных с информацией об операции
     * @return обновленная операция после выполнения первого этапа
     * @throws ColonyNotFoundException       если указанная колония не существует
     * @throws RequestNotFoundException     если указанный запрос не существует
     * @throws OperationTypeNotFoundException если указанный тип операции не поддерживается
     */
    @Transactional
    public Operation startOperation(OperationDto dto) {
        log.info("Starting new operation with type: {}", dto != null ? dto.getType() : "null");

        if (dto == null) {
            throw new InvalidOperationDataException("Operation data cannot be null");
        }

        Operation operation = new Operation().updateByDto(dto);

        if (dto.getColony_id() != null) {
            log.debug("Linking operation to colony with id: {}", dto.getColony_id());
            Colony colony = colonyService.getColonyById(dto.getColony_id());
            operation.setColony(colony);
        }

        Request request;
        if (dto.getRequest_id() != null) {
            log.debug("Linking operation to existing request with id: {}", dto.getRequest_id());
            request = requestService.getById(dto.getRequest_id());
            if (request == null) {
                throw new RequestNotFoundException("Request with id " + dto.getRequest_id() + " not found");
            }
        } else {
            log.debug("Creating new request for operation");
            request = requestService.createSelf(operation);
        }
        operation.setRequest(request);

        log.info("Operation created successfully, proceeding with execution");
        return progress(operation);
    }

    /**
     * Продолжает выполнение операции по её идентификатору.
     *
     * @param id идентификатор операции
     * @return обновленная операция после выполнения текущего этапа
     * @throws OperationNotFoundException если операция с указанным ID не найдена
     */
    @Transactional
    public Operation progressById(Long id) {
        log.debug("Progressing operation by id: {}", id);

        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Operation with id {} not found", id);
                    return new OperationNotFoundException("Operation with id " + id + " not found");
                });

        return progress(operation);
    }

    /**
     * Выполняет текущий этап операции.
     * <p>
     * Делегирует выполнение конкретной реализации операции на основе её типа.
     *
     * @param operation объект операции для выполнения
     * @return обновленная операция после выполнения текущего этапа
     * @throws OperationTypeNotFoundException если тип операции не поддерживается
     */
    @Transactional
    public Operation progress(Operation operation) {
        log.debug("Progressing operation id: {} of type: {}", operation.getId(), operation.getType());

        if (operation.getType() == null) {
            throw new InvalidOperationDataException("Operation type cannot be null");
        }

        OperationRealization realization = allOperations.get(operation.getType());
        if (realization == null) {
            log.error("Operation type '{}' not supported", operation.getType());
            throw new OperationTypeNotFoundException("Operation type '" + operation.getType() + "' is not supported");
        }

        Operation progressedOperation = realization.executeStage(operation);
        log.info("Operation id: {} progressed to stage: {}",
                progressedOperation.getId(), progressedOperation.getStage());

        return progressedOperation;
    }

    /**
     * Возвращает список всех операций, доступных текущему пользователю.
     * <p>
     * Операции считаются доступными, если они:
     * <ul>
     *   <li>Созданы текущим пользователем</li>
     *   <li>Имеют статус, отличный от COMPLETED</li>
     * </ul>
     *
     * @return список операций текущего пользователя (незавершенные)
     */
    @Transactional(readOnly = true)
    public List<Operation> getAllUserOperations() {
        log.debug("Fetching all operations for current user");

        List<Operation> operations = operationRepository.findAll();
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            log.warn("Current user is null, returning empty operation list");
            return new ArrayList<>();
        }

        try {
            List<Operation> userOperations = operations.stream()
                    .filter(item -> {
                        Request request = item.getRequest();
                        return request != null &&
                                request.getCreator() != null &&
                                request.getCreator().equals(currentUser) &&
                                request.getStatus() != Status.COMPLETED;
                    })
                    .collect(Collectors.toList());

            log.debug("Found {} operations for current user", userOperations.size());
            return userOperations;

        } catch (NullPointerException e) {
            log.warn("Null pointer encountered while filtering operations: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}