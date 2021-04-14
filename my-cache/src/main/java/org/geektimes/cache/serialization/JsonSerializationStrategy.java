package org.geektimes.cache.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * serialization strategy implementation of json {@link SerializationStrategy}
 *
 * @author C.HD
 * @since 1.0
 */
public class JsonSerializationStrategy implements SerializationStrategy {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializationStrategy() {
    }

    @Override
    public <T> byte[] serialize(T object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data) {
        try {
            return objectMapper.readValue(data, new TypeReference<T>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(InputStream inputStream) {
        try {
            return objectMapper.readValue(inputStream, new TypeReference<T>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
