package org.jk.chat.messaging;


import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.java.Log;
import org.jk.chat.domain.ActionDispatcher;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * prepares JMS listener and sends a message
 */
@Log
@ApplicationScoped
public class MessageListener implements Runnable {

    private static final String JMS_TOPIC = "chat";

    private final ConnectionFactory connectionFactory;
    private final JMSContext context;
    private final JMSConsumer messageConsumer;
    private final Topic topic;

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();
    private final ActionDispatcher actionDispatcher;

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }


    @Inject
    public MessageListener(ConnectionFactory connectionFactory,
                           ActionDispatcher actionDispatcher) {

        log.info(" -> MessageListener.constructor");

        this.connectionFactory = connectionFactory;
        this.context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE);

        this.topic = context.createTopic(JMS_TOPIC);
        this.messageConsumer = context.createConsumer(this.topic);

        this.actionDispatcher = actionDispatcher;

        log.info(" <- MessageListener.constructor");
    }


    @Override
    public void run() {

        log.info(" -> MessageListener.run, start message listener ");

        while (true) {

            Message message = messageConsumer.receive();

            if (message == null) {
                log.info(" -- MessageListener.run, empty message");
                return;
            }


            try {

                if (message instanceof ObjectMessage) {
                    Object object = ((ObjectMessage) message).getObject();

                    log.info(" -- MessageListener.run, got message ");
                    TransferObject body = (TransferObject) object;

//                    log.info(" -- MessageListener.run, message = " + body.getClientId() + "@" + body.getChatRoom() + " " + body.getMessage());
                    log.info(" -- MessageListener.run, message = " + body.toString());

                    actionDispatcher.dispatch(body);

                } else {
                    log.info(" -> MessageListener.run, message = " + message.getBody(String.class));
                }

            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
