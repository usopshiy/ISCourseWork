package usopshiy.is.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import usopshiy.is.dto.ItemDto;
import usopshiy.is.dto.MessageInfo;
import usopshiy.is.entity.Item;
import usopshiy.is.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Schema(description = "controller for accessing items")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "getting all items")
    @GetMapping("")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @Operation(summary = "get item by name")
    @GetMapping("{name}")
    public Item getItemByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    @Operation(summary = "create new item in the database")
    @PostMapping("create")
    public MessageInfo createItem(@RequestBody @Valid ItemDto itemDto) {
        itemService.create(itemDto);
        return new MessageInfo("success");
    }

    @Operation(summary = "update an item in the database")
    @PostMapping("update")
    public MessageInfo updateItem(@RequestBody @Valid ItemDto itemDto) {
        itemService.updateItem(itemDto);
        return new MessageInfo("success");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleException(Exception ex) {
        return new MessageInfo(ex.getMessage());
    }
}
