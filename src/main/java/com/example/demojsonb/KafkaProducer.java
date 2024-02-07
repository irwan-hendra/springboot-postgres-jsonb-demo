package com.example.demojsonb;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, PersonWithAddressJsonbObject payload) throws Exception {
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(objectMapper.writeValueAsString(payload)));

    }

    public void send(String topic, PersonWithoutAddressJsonbObject payload) throws Exception{
        kafkaTemplate.send(topic, transform(objectMapper.writeValueAsString(payload)));
    }

    private String transform(String payload) {
        return payload
                .replace("\\\"", "\"")
                .replace("\"{", "{")
                .replace("}\"", "}");
    }
}