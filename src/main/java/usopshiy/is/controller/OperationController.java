package usopshiy.is.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import usopshiy.is.dto.OperationDto;
import usopshiy.is.service.OperationService;

import java.util.List;

@RestController
@RequestMapping("/operations")
@RequiredArgsConstructor
@Schema(description = "controller for operations")
public class OperationController {

    private final OperationService operationService;

    @Operation(summary = "start an operation")
    @PostMapping("/start")
    public usopshiy.is.entity.Operation start(@RequestBody @Valid OperationDto dto) {
        return operationService.startOperation(dto);
    }

    @Operation(summary = "progress an operation")
    @GetMapping("progress/{id}")
    public usopshiy.is.entity.Operation progress(@PathVariable Long id) {
        return operationService.progressById(id);
    }

    @Operation(summary = "get all active operations of user")
    @GetMapping("")
    public List<usopshiy.is.entity.Operation> getAllActiveOperations() {
        return operationService.getAllUserOperations();
    }
}
