package com.example.demojsonb;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private PersonWithAddressJsonbObject payload;

    @KafkaListener(topics = "${test.topic}")
    public void receive(ConsumerRecord<String, String> consumerRecord) throws Exception {
        payload = objectMapper.readValue(consumerRecord.value(), PersonWithAddressJsonbObject.class);
        System.out.println(payload);
    }
}