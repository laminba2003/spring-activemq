package com.spring.training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

@Configuration
@AllArgsConstructor
@Slf4j
public class ApplicationConfig implements JmsListenerConfigurer {

    static final String SPRING_TOPIC = "spring.Topic";
    static final String SPRING_QUEUE = "spring.Queue";
    final ActiveMQConfig config;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.setContainerFactory(listenerContainerFactory());
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public JmsListenerContainerFactory<?> listenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setErrorHandler(t -> log.error(t.getCause().getMessage()));
        factory.setMessageConverter(messageConverter());
        factory.setDestinationResolver(destinationResolver());
        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setTrustAllPackages(true);
        factory.setBrokerURL(config.getBrokerUrl());
        factory.setPassword(config.getPassword());
        factory.setUserName(config.getUserName());
        factory.setRedeliveryPolicy(redeliveryPolicy());
        return factory;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setInitialRedeliveryDelay(0);
        policy.setRedeliveryDelay(1000);
        policy.setUseExponentialBackOff(false);
        policy.setMaximumRedeliveries(2);
        return policy;
    }

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver() {
            @Override
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
                pubSubDomain = destinationName.endsWith("Topic");
                return super.resolveDestinationName(session, destinationName, pubSubDomain);
            }
        };
    }

    @Bean
    public Validator messageValidator() {
        return new OptionalValidatorFactoryBean();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(messageValidator());
        return factory;
    }

}