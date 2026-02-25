package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.RequestDto;
import usopshiy.is.entity.Operation;
import usopshiy.is.entity.Request;
import usopshiy.is.entity.Status;
import usopshiy.is.entity.User;
import usopshiy.is.exception.*;
import usopshiy.is.repository.RequestRepository;

import java.util.List;

/**
 * Сервис для управления запросами в системе.
 * Отвечает за создание, обновление и получение запросов,
 * связанных с операциями и задачами пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    /**
     * Создает новый запрос на основе предоставленных данных.
     * <p>
     * Устанавливает создателя запроса (текущего пользователя)
     * и начальный статус ON_ASSIGNMENT.
     *
     * @param dto объект передачи данных с информацией о запросе
     * @throws InvalidRequestDataException если данные запроса некорректны
     */
    @Transactional
    public void create(RequestDto dto) {
        log.info("Creating new request");

        if (dto == null) {
            throw new InvalidRequestDataException("Request data cannot be null");
        }

        Request obj = new Request().updateByDto(dto);
        obj.setCreator(userService.getCurrentUser());
        obj.setStatus(Status.ON_ASSIGNMENT);

        requestRepository.save(obj);
        log.info("Request created successfully with id: {}", obj.getId());
    }

    /**
     * Возвращает запрос по его идентификатору.
     *
     * @param id идентификатор запроса
     * @return найденный запрос
     * @throws RequestNotFoundException если запрос с указанным ID не существует
     */
    @Transactional(readOnly = true)
    public Request getById(Long id) {
        log.debug("Fetching request by id: {}", id);

        return requestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Request with id {} not found", id);
                    return new RequestNotFoundException("Request with id " + id + " not found");
                });
    }

    /**
     * Обновляет существующий запрос.
     * <p>
     * Назначает исполнителя (текущего пользователя), обновляет статус
     * на следующий в последовательности и устанавливает время завершения,
     * если оно предоставлено.
     *
     * @param dto объект передачи данных с обновленной информацией о запросе
     * @throws RequestNotFoundException если запрос с указанным ID не существует
     * @throws InvalidRequestDataException если данные запроса некорректны
     */
    @Transactional
    public void update(RequestDto dto) {
        log.info("Updating request with id: {}", dto != null ? dto.getId() : "null");

        if (dto == null || dto.getId() == null) {
            throw new InvalidRequestDataException("Request ID cannot be null");
        }

        Request request = requestRepository.findById(dto.getId())
                .orElseThrow(() -> {
                    log.warn("Cannot update: request with id {} not found", dto.getId());
                    return new RequestNotFoundException("Request with id " + dto.getId() + " not found");
                });

        User user = userService.getCurrentUser();
        if (user == null) {
            throw new UserNotFoundException("Current user not found");
        }

        request.setAssignee(user);
        request.setNextStatus();

        if (dto.getCompletionTime() != null) {
            request.setCompletionTime(dto.getCompletionTime());
        }

        requestRepository.save(request);
        log.info("Request with id {} updated successfully. New status: {}",
                dto.getId(), request.getStatus());
    }

    /**
     * Создает автоматический запрос для операции.
     * <p>
     * Используется для создания запроса, связанного с запущенной операцией,
     * где создатель и исполнитель совпадают (текущий пользователь).
     *
     * @param operation операция, для которой создается запрос
     * @return созданный запрос
     * @throws InvalidOperationDataException если операция некорректна
     */
    @Transactional
    public Request createSelf(Operation operation) {
        log.debug("Creating self-request for operation");

        if (operation == null) {
            throw new InvalidOperationDataException("Operation cannot be null when creating self-request");
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UserNotFoundException("Current user not found");
        }

        Request request = Request.builder()
                .creator(currentUser)
                .assignee(currentUser)
                .type(operation.getType())
                .status(Status.ASSIGNED)
                .details("complete an operation you started")
                .build();

        Request savedRequest = requestRepository.save(request);
        log.info("Self-request created with id: {} for operation type: {}",
                savedRequest.getId(), operation.getType());

        return savedRequest;
    }

    /**
     * Возвращает список всех запросов в системе.
     *
     * @return список всех запросов
     */
    @Transactional(readOnly = true)
    public List<Request> getRequests() {
        log.debug("Fetching all requests");
        List<Request> requests = requestRepository.findAll();
        log.debug("Fetched {} requests", requests.size());
        return requests;
    }

    /**
     * Возвращает список активных запросов, созданных текущим пользователем.
     *
     * @return список запросов, созданных текущим пользователем
     */
    @Transactional(readOnly = true)
    public List<Request> getRequestsByCreator() {
        log.debug("Fetching requests created by current user");
        User user = userService.getCurrentUser();

        if (user == null) {
            log.warn("Current user is null, returning empty list");
            return List.of();
        }

        List<Request> requests = requestRepository.getActiveRequestByCreator(user);
        log.debug("Found {} requests created by user {}", requests.size(), user.getId());
        return requests;
    }

    /**
     * Возвращает список активных запросов, назначенных текущему пользователю.
     *
     * @return список запросов, назначенных текущему пользователю
     */
    @Transactional(readOnly = true)
    public List<Request> getRequestsByAssignee() {
        log.debug("Fetching requests assigned to current user");
        User user = userService.getCurrentUser();

        if (user == null) {
            log.warn("Current user is null, returning empty list");
            return List.of();
        }

        List<Request> requests = requestRepository.getActiveRequestByAssignee(user);
        log.debug("Found {} requests assigned to user {}", requests.size(), user.getId());
        return requests;
    }

    /**
     * Возвращает список запросов, ожидающих назначения исполнителя.
     *
     * @return список ожидающих запросов
     */
    @Transactional(readOnly = true)
    public List<Request> getAwaitingRequests() {
        log.debug("Fetching awaiting requests");
        List<Request> requests = requestRepository.getAwaitingRequests();
        log.debug("Found {} awaiting requests", requests.size());
        return requests;
    }
}