package com.example.config.instant;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class InstantCustomizeConfig {
    //Using when config global Instant Date Only Serializer and Deserializer
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer customizer() {
//        return builder -> {
//            JavaTimeModule module = new JavaTimeModule();
//            module.addSerializer(Instant.class, new InstantDateOnlySerializer());
//            module.addDeserializer(Instant.class, new InstantDateOnlyDeserializer());
//            builder.modules(module);
//            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        };
//    }
}
