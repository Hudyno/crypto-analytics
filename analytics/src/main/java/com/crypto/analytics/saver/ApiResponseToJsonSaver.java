package com.crypto.analytics.saver;

import com.crypto.analytics.util.JsonUtil;
import com.crypto.analytics.util.CollectionUtil;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Service
@Log4j2
@RequiredArgsConstructor
public abstract class ApiResponseToJsonSaver {

    protected final JsonUtil jsonUtil;
    protected final CollectionUtil collectionUtil;

    @NonNull
    protected final Integer batchSize;

    @NonNull
    protected final Integer rateLimit;

    public void saveApiResponse(Supplier<Object> apiMethod, String fileName) {
        Object response = apiMethod.get();
        jsonUtil.writeJson(new File(fileName), response);
    }

    public void saveApiResponse(Function<Object[], Object> apiMethod, String fileName, Object... apiParameters) {
        Object response = apiMethod.apply(apiParameters);
        jsonUtil.writeJson(new File(fileName), response);
    }

    public void batchSaveApiResponse(BiFunction<List<?>, Object[], List<?>> apiMethod, String fileName,
                                     List<?> listToSplit, Object... apiParameters) {

        File file = new File(fileName);
        ArrayNode jsonArray = jsonUtil.readJsonAsArrayNode(file);

        IntStream.range(0, collectionUtil.nSubListByBatchSize(batchSize, listToSplit.size()))
                .mapToObj(i -> collectionUtil.subListFromBatchSize(listToSplit, batchSize, i))
                .forEach(subList -> {
                    List<?> response = apiMethod.apply(subList, apiParameters);
                    jsonUtil.appendObjectToArrayNode(file, jsonArray, response);

                    try {
                        log.info("Sleeping for {} s", rateLimit / 1000);
                        Thread.sleep(rateLimit);
                    } catch (InterruptedException e) {
                        log.error(e);
                        Thread.currentThread().interrupt();
                    }
                });

        log.info("FINISHED");
    }
}
