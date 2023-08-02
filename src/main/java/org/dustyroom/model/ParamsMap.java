package org.dustyroom.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dustyroom.operations.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Data
@Slf4j
public class ParamsMap {

    private int threadPoolCapacity;
    private Map<Operation, List<Params>> operationToParamsList;

    public void execute() {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolCapacity);
        List<Future<?>> futureList = new ArrayList<>();
        operationToParamsList.forEach(
                (operation, paramsList) -> paramsList.forEach(
                        param -> futureList.add(executorService.submit(operation.getExecutorProvider().apply(param)))
                )
        );
        executorService.shutdown();
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.warn("Task was not completed {}", future);
            }
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Params {
        private String backupOrigin;
        private String backupDestination;
    }
}