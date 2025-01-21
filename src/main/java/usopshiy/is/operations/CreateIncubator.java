package usopshiy.is.operations;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.entity.*;
import usopshiy.is.repository.ItemRepository;
import usopshiy.is.repository.OperationRepository;
import usopshiy.is.repository.UsedItemsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateIncubator implements OperationRealization {

    private final ItemRepository itemRepository;
    private final UsedItemsRepository usedItemsRepository;
    private final OperationRepository operationRepository;
    @Setter
    private boolean bubbleMock = false;

    private final List<String> stageDescriptions = new ArrayList<>() {
        {
            add("Не хватает пробирок на складе: Продолжение операции невозможно."); //0
            add("Залейте в пробирку воду, накройте ватой. Оставьте пробирку в камере мониторинга пузыря."); //1
            add("Ожидайте 5 минут. В случае ошибки, система вернет уведомление и вернёт вас на предыдущий этап"); //2
            add("Заберите готовый инкубатор, он добавится в систему"); //3
            add("Операция завершена"); //4
        }
    };


    @Override
    public Operation executeStage(Operation operation) {
        switch (operation.getStage()) {
            case 0:
                return stage0(operation);
            case 2:
                return stage2(operation);
            case 3:
                return stage3(operation);
            case 4:
                return operation; //fail-safe
            default:
                return informationalStage(operation);
        }
    }

    @Transactional
    protected Operation stage0(Operation operation) {
        Item testTube = itemRepository.findByName("Test Tube");
        if (testTube != null && testTube.getStored() != 0) {
            OperationItemKey key = OperationItemKey.builder()
                    .operationId(operation.getId())
                    .itemId(testTube.getId())
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

    private Operation stage2(Operation operation) {
        if (operation.getLastInteraction().plusMinutes(5).isBefore(LocalDateTime.now())) {
            if (bubbleMock) {
                operation.setStage(1);
                operation.setLastInteraction(LocalDateTime.now());
            }
        }
        else {
            operation.setStage(3);
            operation.setLastInteraction(LocalDateTime.now());
        }

        operation.setStageDescription(stageDescriptions.get(operation.getStage()));
        return operationRepository.save(operation);
    }

    @Transactional
    protected Operation stage3(Operation operation) {
        Item incubator = itemRepository.findByName("Incubator");
        if (incubator == null) {
            incubator = new Item();
            incubator.setName("Incubator");
            incubator.setStored(0);
        }
        incubator.setStored(incubator.getStored() + 1);
        itemRepository.save(incubator);

        operation.setStage(4);
        operation.setLastInteraction(LocalDateTime.now());
        operation.setStageDescription(stageDescriptions.get(operation.getStage()));
        operation.getRequest().setStatus(Status.COMPLETED);
        operation.getRequest().setCompletionTime(LocalDate.now());
        return operationRepository.save(operation);
    }

    private Operation informationalStage(Operation operation) {
        operation.setStage(operation.getStage() + 1);
        operation.setStageDescription(stageDescriptions.get(operation.getStage()));
        operation.setLastInteraction(LocalDateTime.now());
        return operationRepository.save(operation);
    }
}
