package org.jk.chat.messaging;



import lombok.extern.java.Log;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;


@Log
@ApplicationScoped
public class MessageProducer {

    private static final String JMS_TOPIC = "chat";

    private final ConnectionFactory connectionFactory;
    private final JMSContext context;
    private final JMSProducer messageProducer;
    private final Topic topic;


    @Inject
    public MessageProducer(ConnectionFactory connectionFactory) {

        log.info(" -> MessageProducer.constructor");

        this.connectionFactory = connectionFactory;
        this.context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE);
        this.messageProducer = context.createProducer();
        this.topic = context.createTopic(JMS_TOPIC);
        log.info(" <- MessageProducer.constructor");
    }

    public void sendMessage(String message) {
        log.info(" -> MessageProducer.sendMessage message = %s".formatted(message));
        messageProducer.send(topic, message);
    }


    public void sendTransferObject(TransferObject transferObject) {
        log.info(" -> MessageProducer.sendTransferObject message = %s".formatted(transferObject.getMessage()));
        ObjectMessage objectMessage = context.createObjectMessage(transferObject);
        messageProducer.send(topic, transferObject);
    }





}
