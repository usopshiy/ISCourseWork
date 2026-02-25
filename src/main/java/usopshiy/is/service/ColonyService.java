package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.ColonyDto;
import usopshiy.is.dto.DecorationDto;
import usopshiy.is.entity.Colony;
import usopshiy.is.exception.*;
import usopshiy.is.repository.ColonyRepository;
import usopshiy.is.repository.DecorationRepository;

import java.util.List;

/**
 * Сервис для управления колониями муравьев.
 * Предоставляет функционал для получения информации о колониях,
 * их обновления, а также управления декоративными элементами в колониях.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ColonyService {

    private final ColonyRepository colonyRepository;
    private final DecorationRepository decorationRepository;

    /**
     * Возвращает список всех колоний в системе.
     * <p>
     * Метод выполняет чтение всех записей из репозитория колоний.
     * В случае возникновения ошибки при обращении к базе данных,
     * исключение логируется и пробрасывается выше для централизованной обработки.
     *
     * @return список всех колоний
     * @throws DatabaseAccessException если произошла ошибка при доступе к базе данных
     */
    @Transactional(readOnly = true)
    public List<Colony> getAllColonies() {
        log.debug("Fetching all colonies from database");
        try {
            List<Colony> colonies = colonyRepository.findAll();
            log.debug("Successfully fetched {} colonies", colonies.size());
            return colonies;
        } catch (Exception e) {
            log.error("Error occurred while fetching all colonies: {}", e.getMessage(), e);
            throw new DatabaseAccessException("Failed to retrieve colonies from database", e);
        }
    }

    /**
     * Возвращает колонию по её идентификатору.
     * <p>
     * Метод выполняет поиск колонии в репозитории по указанному ID.
     * Если колония не найдена, выбрасывается соответствующее исключение.
     *
     * @param id идентификатор колонии
     * @return найденная колония
     * @throws ColonyNotFoundException если колония с указанным ID не существует
     */
    @Transactional(readOnly = true)
    public Colony getColonyById(Long id) {
        log.debug("Fetching colony with id: {}", id);

        return colonyRepository.findById(id)
                .map(colony -> {
                    log.debug("Successfully fetched colony with id: {}", id);
                    return colony;
                })
                .orElseThrow(() -> {
                    log.warn("Colony with id {} not found", id);
                    return new ColonyNotFoundException("Colony with id " + id + " not found");
                });
    }

    /**
     * Обновляет информацию о существующей колонии.
     * <p>
     * Метод находит колонию по идентификатору из DTO, обновляет её поля
     * с использованием метода {@link Colony#updateByDto(ColonyDto)} и сохраняет
     * изменения в базе данных.
     *
     * @param colonyDto объект передачи данных с обновленной информацией о колонии
     * @throws ColonyNotFoundException если колония с указанным ID не существует
     * @throws DatabaseAccessException если произошла ошибка при сохранении в базу данных
     */
    @Transactional
    public void updateColony(ColonyDto colonyDto) {
        log.info("Attempting to update colony with id: {}", colonyDto.getId());

        Colony colony = colonyRepository.findById(colonyDto.getId())
                .orElseThrow(() -> {
                    log.warn("Cannot update: colony with id {} not found", colonyDto.getId());
                    return new ColonyNotFoundException("Colony with id " + colonyDto.getId() + " not found");
                });

        try {
            Colony updatedColony = colony.updateByDto(colonyDto);
            colonyRepository.save(updatedColony);
            log.info("Successfully updated colony with id: {}", colonyDto.getId());
        } catch (Exception e) {
            log.error("Error occurred while saving colony with id {}: {}", colonyDto.getId(), e.getMessage(), e);
            throw new DatabaseAccessException("Failed to update colony with id " + colonyDto.getId(), e);
        }
    }

    /**
     * Добавляет декоративный элемент в указанную колонию.
     * <p>
     * Метод создает новую запись о декоративном элементе, связывая его
     * с колонией по указанному идентификатору. Проверяет существование колонии
     * перед добавлением декоративного элемента.
     *
     * @param colonyId идентификатор колонии, в которую добавляется декоративный элемент
     * @param dto      объект передачи данных с информацией о декоративном элементе
     * @throws ColonyNotFoundException        если колония с указанным ID не существует
     * @throws InvalidDecorationDataException если данные декоративного элемента некорректны
     * @throws DatabaseAccessException        если произошла ошибка при сохранении в базу данных
     */
    @Transactional
    public void addDecoration(Long colonyId, DecorationDto dto) {
        log.info("Attempting to add decoration to colony with id: {}", colonyId);

        // Проверяем существование колонии
        if (!colonyRepository.existsById(colonyId)) {
            log.warn("Cannot add decoration: colony with id {} not found", colonyId);
            throw new ColonyNotFoundException("Colony with id " + colonyId + " not found");
        }

        try {
            decorationRepository.createByValues(colonyId, dto.getItemName(), dto.getAmount());
            log.info("Successfully added decoration '{}' (amount: {}) to colony with id: {}",
                    dto.getItemName(), dto.getAmount(), colonyId);
        } catch (JpaSystemException e) {
            log.error("JPA error occurred while adding decoration to colony {}: {}", colonyId, e.getMessage(), e);
            throw new DatabaseAccessException("Failed to add decoration due to database error", e);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while adding decoration to colony {}: {}", colonyId, e.getMessage(), e);
            throw new InvalidDecorationDataException("Invalid decoration data or constraint violation", e);
        }
    }
}