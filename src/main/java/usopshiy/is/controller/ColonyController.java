package usopshiy.is.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import usopshiy.is.dto.ColonyDto;
import usopshiy.is.dto.DecorationDto;
import usopshiy.is.dto.MessageInfo;
import usopshiy.is.entity.Colony;
import usopshiy.is.service.ColonyService;

import java.util.List;

@RestController
@RequestMapping("/colonies")
@RequiredArgsConstructor
@Schema(description = "controller for colony interactions")
public class ColonyController {

    private final ColonyService colonyService;

    @Operation(summary = "get all colonies")
    @GetMapping("")
    public List<Colony> getAllColonies() {
        return colonyService.getAllColonies();
    }

    @Operation(summary = "get specific colony by id")
    @GetMapping("/{id}")
    public Colony getColonyById(@PathVariable Long id) {
        return colonyService.getColonyById(id);
    }

    @Operation(summary = "update a colony")
    @PostMapping("/update")
    public MessageInfo updateColony(@RequestBody ColonyDto dto) {
        colonyService.updateColony(dto);
        return new MessageInfo("success");
    }


    @Operation(summary = "add decoration for a colony")
    @PostMapping("/{id}/add-decoration")
    public MessageInfo addDecoration(@PathVariable Long id, @RequestBody DecorationDto dto) {
        colonyService.addDecoration(id, dto);
        return new MessageInfo("success");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleException(Exception e) {
        return new MessageInfo(e.getMessage());
    }
}
