package org.dustyroom.operations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dustyroom.model.ParamsMap;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum Operation {

    BACKUP(params -> new BackupOperationExecutor(params, false)),
    RESTORE(params -> new BackupOperationExecutor(params, true));

    private final Function<ParamsMap.Params, Runnable> executorProvider;
}
