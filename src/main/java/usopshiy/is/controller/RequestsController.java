package usopshiy.is.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import usopshiy.is.dto.MessageInfo;
import usopshiy.is.dto.RequestDto;
import usopshiy.is.entity.Request;
import usopshiy.is.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Schema(description = "controller for accessing requests")
public class RequestsController {

    private final RequestService requestService;

    @Operation(summary = "getting list of all requests")
    @GetMapping("")
    public List<Request> getAllRequests() {
        return requestService.getRequests();
    }

    @Operation(summary = "getting all request with user as a creator")
    @GetMapping("/created")
    public List<Request> getCreatedRequests() {
        return requestService.getRequestsByCreator();
    }

    @Operation(summary = "getting all request with a user as a assignee")
    @GetMapping("/taken")
    public List<Request> getTakenRequests() {
        return requestService.getRequestsByAssignee();
    }

    @Operation(summary = "getting all request that are on assignment")
    @GetMapping("/awaiting")
    public List<Request> getAwaitingRequests() {
        return requestService.getAwaitingRequests();
    }

    @Operation(summary = "creating new request")
    @PostMapping("/create")
    public MessageInfo createRequest(@RequestBody @Valid RequestDto requestDto) {
        requestService.create(requestDto);
        return new MessageInfo("success");
    }

    @Operation(summary = "updating request")
    @PostMapping("/update")
    public MessageInfo updateRequest(@RequestBody @Valid RequestDto requestDto) {
        requestService.update(requestDto);
        return new MessageInfo("success");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleException(Exception ex) {
        return new MessageInfo(ex.getMessage());
    }
}
