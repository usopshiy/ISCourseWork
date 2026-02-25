package usopshiy.is.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import usopshiy.is.dto.MessageInfo;
import usopshiy.is.dto.ValueDto;
import usopshiy.is.service.HumidityService;
import usopshiy.is.service.ThermometerService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Schema(description = "change sensor mock values for demonstration purposes")
public class AdminController {

    private final ThermometerService thermometerService;
    private final HumidityService humidityService;

    @Operation(summary = "set thermo emulation value")
    @PostMapping("/set/thermo")
    public MessageInfo setThermo(@RequestBody ValueDto value) {
        thermometerService.setTemperature(value.getValue());
        return new MessageInfo("success");
    }

    @Operation(summary = "set humidity emulation value")
    @PostMapping("/set/humidity")
    public MessageInfo setHumidity(@RequestBody ValueDto value) {
        humidityService.setHumidity(value.getValue());
        return new MessageInfo("success");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfo handleException(Exception ex) {
        return new MessageInfo(ex.getMessage());
    }
}
