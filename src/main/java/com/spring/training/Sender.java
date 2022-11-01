package com.spring.training;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class Sender {

    final JmsTemplate template;

    public void sendToQueue(Message message) {
        template.convertAndSend(ApplicationConfig.SPRING_QUEUE, message);
    }

    public void sendToTopic(Message message) {
        template.convertAndSend(ApplicationConfig.SPRING_TOPIC, message);
    }

}
