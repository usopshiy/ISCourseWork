package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import usopshiy.is.entity.Thermometer;
import usopshiy.is.repository.ThermometerRepository;

@Service
@RequiredArgsConstructor
public class ThermometerService {

    private final ThermometerRepository thermometerRepository;
    @Setter
    private float temperature = 25.0F;

    public void updateTemperature(Thermometer thermometer) {
        thermometer.setValue(temperature);
        thermometerRepository.save(thermometer);
    }

    public boolean checkTemps(Long colonyId, Float min, Float max) {
        return thermometerRepository.checkTemps(colonyId, min, max);
    }
}
