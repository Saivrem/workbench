package org.dustyroom.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.dustyroom.model.ParamsMap;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class SerDeUtils {

    private final static ObjectMapper objectMapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ParamsMap readParams(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }
}
