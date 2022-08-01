package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.java.Log;
import org.jk.chat.common.TransferObject;
import org.jk.chat.messaging.MessageProducer;

import javax.inject.Inject;

import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
public class SendMessageEventListener {

    private final MessageProducer messageProducer;

    @Inject
    public SendMessageEventListener(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @ConsumeEvent(SEND_MESSAGE)
    @Blocking
    public void sendMessage(TransferObject transferObject) {

        messageProducer.sendTransferObject(transferObject);
    }

}
