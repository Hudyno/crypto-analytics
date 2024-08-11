package com.crypto.analytics.util;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionUtil {

    public  <T> List<T> subListFromBatchSize(List<T> fullList, int batchSize, int fromIndex) {
        return fullList.subList(fromIndex * batchSize, Math.min((fromIndex + 1) * batchSize, fullList.size()));
    }

    public int nSubListByBatchSize(int batchSize, int collectionSize) {
        return (collectionSize + batchSize - 1) / batchSize;
    }
}
