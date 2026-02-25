package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.ItemDto;
import usopshiy.is.entity.Item;
import usopshiy.is.exception.*;
import usopshiy.is.repository.ItemRepository;

import java.util.List;

/**
 * Сервис для управления предметами и материалами на складе.
 * Предоставляет функционал для создания, обновления, получения
 * и мониторинга предметов, используемых в колонии муравьев.
 *
 * <p>Предметы могут представлять собой материалы для обустройства колонии,
 * корм, декоративные элементы и другие расходные материалы.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Возвращает список всех предметов, доступных на складе.
     * <p>
     * Метод выполняет чтение всех записей из репозитория предметов.
     * Используется для отображения полного каталога предметов в системе.
     *
     * @return список всех предметов
     */
    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        log.debug("Fetching all items from inventory");
        List<Item> items = itemRepository.findAll();
        log.debug("Successfully fetched {} items", items.size());
        return items;
    }

    /**
     * Возвращает предмет по его точному наименованию.
     * <p>
     * Поиск выполняется по полному совпадению имени (case-sensitive,
     * зависит от реализации репозитория). Если предмет не найден,
     * возвращается null, что может быть обработано на уровне контроллера.
     *
     * @param name точное наименование предмета
     * @return найденный предмет или null, если предмет не существует
     */
    @Transactional(readOnly = true)
    public Item getItemByName(String name) {
        log.debug("Fetching item by name: '{}'", name);

        if (name == null || name.trim().isEmpty()) {
            log.warn("Attempted to fetch item with null or empty name");
            return null;
        }

        Item item = itemRepository.findByName(name);
        if (item == null) {
            log.debug("Item with name '{}' not found", name);
        } else {
            log.debug("Successfully fetched item: '{}' (id: {})", name, item.getId());
        }
        return item;
    }

    /**
     * Создает новый предмет в системе складского учета.
     * <p>
     * Метод преобразует DTO в сущность Item и сохраняет её в базе данных.
     *
     * @param dto объект передачи данных с информацией о новом предмете
     */
    @Transactional
    public void create(ItemDto dto) {
        Item item = new Item().updateByDto(dto);
        itemRepository.save(item);
    }

    /**
     * Обновляет информацию о существующем предмете.
     * <p>
     * Метод находит предмет по наименованию, обновляет его поля
     * с использованием метода {@link Item#updateByDto(ItemDto)} и сохраняет
     * изменения в базе данных. Позволяет изменять количество, цену
     * и другие характеристики предмета.
     *
     * @param dto объект передачи данных с обновленной информацией о предмете
     * @throws ItemNotFoundException     если предмет с указанным наименованием не существует
     * @throws InvalidItemDataException  если данные предмета некорректны
     */
    @Transactional
    public void updateItem(ItemDto dto) {
        log.info("Attempting to update item with name: '{}'", dto != null ? dto.getName() : "null");

        Item item = itemRepository.findByName(dto.getName());
        if (item == null) {
            log.warn("Cannot update item: item with name '{}' not found", dto.getName());
            throw new ItemNotFoundException("Item with name '" + dto.getName() + "' not found");
        }

        try {
            Item updatedItem = item.updateByDto(dto);
            itemRepository.save(updatedItem);
            log.info("Successfully updated item with name: '{}'", dto.getName());
        } catch (Exception e) {
            log.error("Error occurred while updating item '{}': {}", dto.getName(), e.getMessage(), e);
            throw new InvalidItemDataException("Failed to update item with name '" + dto.getName() + "'", e);
        }
    }
}