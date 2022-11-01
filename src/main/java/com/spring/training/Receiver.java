package com.spring.training;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@AllArgsConstructor
public class Receiver {

    @JmsListener(destination = ApplicationConfig.SPRING_QUEUE)
    public void consumeFromQueue(@Payload @Validated Message message) {
        log.info("message received : {} ", message);
    }

    @JmsListener(destination = ApplicationConfig.SPRING_TOPIC)
    public void consumeFromTopic(@Payload @Validated Message message) {
        log.info("message received : {} ", message);
    }
}
