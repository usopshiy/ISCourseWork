package usopshiy.is.operations;

import usopshiy.is.entity.Operation;

public interface OperationRealization {

    Operation executeStage(Operation operation);
}
