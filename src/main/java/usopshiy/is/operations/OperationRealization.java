package usopshiy.is.operations;

import usopshiy.is.entity.Operation;

public interface OperationRealization {
    String getType();
    Operation executeStage(Operation operation);
}
