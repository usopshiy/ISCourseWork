package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usopshiy.is.dto.ItemDto;
import usopshiy.is.entity.Item;
import usopshiy.is.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public void updateItem(ItemDto dto) {
        Item item = itemRepository.findByName(dto.getName());
        if (item == null) {
            throw new RuntimeException("Item not found");
        }
        itemRepository.save(item.updateByDto(dto));
    }

    public Item getItemByName(String name) {
        return itemRepository.findByName(name);
    }

    public Item create(ItemDto dto) {
        Item item = new Item().updateByDto(dto);
        return itemRepository.save(item);
    }
}
