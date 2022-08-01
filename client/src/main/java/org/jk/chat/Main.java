package org.jk.chat;


import io.quarkus.arc.Arc;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.java.Log;
import org.jk.chat.client.ChatClient;
import org.jk.chat.common.TransferObject;
import org.jk.chat.client.messaging.MessageProducer;


@Log
@QuarkusMain
public class Main implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {

        log.info(" -> main.run ");

        // wysyłanie informacji
        MessageProducer messageProducer = Arc.container().instance(MessageProducer.class).get();

        messageProducer.sendMessage("Hello !!! - send by client Main.run");


        ChatClient chatClient = Arc.container().instance(ChatClient.class).get();
        messageProducer.sendTransferObject(
                TransferObject.builder()
                        .clientId(chatClient.getClientId())
                        .chatRoom(chatClient.getRoom())
                        .message("test messsage from main")
                        .build()
        );


        Quarkus.waitForExit();
        log.info(" <- main.run ");
        return 0;
    }

}