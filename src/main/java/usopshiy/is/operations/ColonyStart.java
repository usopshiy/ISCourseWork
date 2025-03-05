package usopshiy.is.operations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.entity.*;
import usopshiy.is.repository.ItemRepository;
import usopshiy.is.repository.OperationRepository;
import usopshiy.is.repository.UsedItemsRepository;
import usopshiy.is.service.HumidityService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ColonyStart implements OperationRealization {
    
    private final ItemRepository itemRepository;
    private final UsedItemsRepository usedItemsRepository;
    private final OperationRepository operationRepository;
    private final HumidityService humidityService;

    private final List<String> stageDescriptions = new ArrayList<>() {
        {
            add("Не хватает инкубаторов на складе. Продолжение операции невозможно"); //0
            add("Увлажните формикарий до нужного уровня, как будете готовы - продолжите операцию."); //1
            add("Перекройте часть ходов формикария - чаще всего 1-2 камер достаточно для старта колонии."); //2
            add("Ограничьте поступление света в формикарий: накройте жилую зону картоном или красной плёнкой"); //3
            add("Положите инкубатор на арену, выньте ватный тампон и накройте арену крышкой"); //4
            add("Ожидайте в течение часа - если муравьи не переселятся - проверьте уровни влажности и света. В случае если " +
                    "ситуация не изменится за 2-3 часа, простимулируйте переселение оставив свет на арене."); //5
            add("Опциональный этап: начните декорирование арены формикария через соотвествующий интерфейс."); //6
        }
    };


    @Transactional
    @Override
    public Operation executeStage(Operation operation) {
        return switch (operation.getStage()) {
            case 0 -> stage0(operation);
            case 1 -> stage1(operation);
            case 6 -> operation; //fail-safe
            default -> informationalStage(operation);
        };
    }

    @Transactional
    protected Operation stage0(Operation operation) {
        Item incubator = itemRepository.findByName("Incubator");
        if (incubator != null && incubator.getStored() > 0) {
            OperationItemKey key = OperationItemKey.builder()
                    .operationId(operation.getId())
                    .itemId(incubator.getId())
                    .build();

            UsedItem obj = UsedItem.builder()
                    .id(key)
                    .amount(1)
                    .build();
            usedItemsRepository.save(obj);

            operation.setStage(1);
        }

        operation.setStageDescription(stageDescriptions.get(operation.getStage()));
        operation.setLastInteraction(LocalDateTime.now());
        return operationRepository.save(operation);
    }

    private Operation stage1(Operation operation) {
        Long colonyId = operation.getColony().getId();
        for (HumidityControl control : operation.getColony().getColonyHumidities()) {
            humidityService.updateHumidity(control);
        }
        String unparsed = operation.getColony().getAnt().getOptimalHumidity();
        String[] hums = unparsed.split(" ");
        if (humidityService.checkHumidity(colonyId, Float.parseFloat(hums[0]), Float.parseFloat(hums[1]))) {
            operation.setStage(2);
            operation.setStageDescription(stageDescriptions.get(operation.getStage()));
        }
        else {
            operation.setStageDescription("Incorrect humidity: check readings and try again");
        }
        operation.setLastInteraction(LocalDateTime.now());
        return operationRepository.save(operation);
    }

    private Operation informationalStage(Operation operation) {
        operation.setStage(operation.getStage() + 1);
        operation.setStageDescription(stageDescriptions.get(operation.getStage()));
        operation.setLastInteraction(LocalDateTime.now());
        return operationRepository.save(operation);
    }
}
