package one.digitalinnovation.beerstock.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class JsonConvertionUtils {
    public static String asJsonString(Object bookDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, state:false);
            ObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, state:false);
            ObjectMapper.registerModules(new JavaTimeModule());

            return objectMapper.writeValueAsString(bookDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
