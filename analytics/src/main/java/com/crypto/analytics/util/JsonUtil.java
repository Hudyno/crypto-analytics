package com.crypto.analytics.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Log4j2
@Service
public class JsonUtil {

    private static final String JSON_PARSE_ERROR_MSG = "Could not parse json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T readJson(File file, TypeReference<T> typeReference) {
        try {
            log.info("Reading data from {}", file.toString());
            return objectMapper.readValue(file, typeReference);

        }catch (IOException e) {
            throw new RuntimeException(JSON_PARSE_ERROR_MSG, e);
        }
    }

    public void writeJson(File file, Object object) {
        try {
            log.info("Writing data to {}", file.toString());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
            log.info("Writing finished");

        }catch (IOException e) {
            throw new RuntimeException(JSON_PARSE_ERROR_MSG, e);
        }
    }

    public ArrayNode readJsonAsArrayNode(File file) {
        if (file.exists() && file.length() > 0) {
            try {
                log.info("Reading data from {} as ArrayNode", file.toString());
                return (ArrayNode) objectMapper.readTree(file);

            }catch (IOException e) {
                throw new RuntimeException(JSON_PARSE_ERROR_MSG, e);
            }
        }
        return objectMapper.createArrayNode();
    }

    public ObjectNode valueToNode(Object object) {
        try {
            return objectMapper.valueToTree(object);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Could not parse object to node", e);
        }
    }

    public List<ObjectNode> valueToNode(List<?> objectList) {
        return objectList.stream().map(this::valueToNode).toList();
    }

    public void appendObjectToArrayNode(File file, ArrayNode currentArrayNode, List<?> objectToAdd) {
        List<ObjectNode> objectNodeList = valueToNode(objectToAdd);
        currentArrayNode.addAll(objectNodeList);
        writeJson(file, currentArrayNode);
        log.info("Nodes written: {}", objectNodeList.size());
    }

}
