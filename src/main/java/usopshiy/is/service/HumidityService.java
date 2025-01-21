package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import usopshiy.is.entity.HumidityControl;
import usopshiy.is.repository.HumidityControlRepository;

@Service
@RequiredArgsConstructor
public class HumidityService {

    private final HumidityControlRepository humidityControlRepository;
    @Setter
    private float humidity = 70.0F;

    public void updateHumidity(HumidityControl humidityControl) {
        humidityControl.setValue(humidity);
        humidityControlRepository.save(humidityControl);
    }

    public boolean checkHumidity(Long colonyId, float min, float max) {
        return humidityControlRepository.checkHumidities(colonyId, min, max);
    }
}
