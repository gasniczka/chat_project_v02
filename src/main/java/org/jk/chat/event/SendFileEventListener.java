package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.common.TransferObject;
import org.jk.chat.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;

import static org.jk.chat.common.Configuration.SERVER_CLIENT;
import static org.jk.chat.event.EventActions.SEND_FILE;
import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
@ApplicationScoped
public class SendFileEventListener {

    private final EventBus eventBus;


    public SendFileEventListener(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @ConsumeEvent(SEND_FILE)
    @Blocking
    public void sendFile(TransferObject transferObject) {

        log.info(" -> sendFile: " + transferObject.toString());

        String message = transferObject.getMessage();

        TransferObject responseObject = null;

        if (StringUtils.isNotBlank(message)) {

            File file = FileUtils.getFileByName(message);

            if (file == null) {

                responseObject = TransferObject.builder()
                        .clientId(SERVER_CLIENT)
                        .receipient(transferObject.getClientId())
                        .message("Can not send file, file does not exists: " + message)
                        .build();
            }

            byte[] fileContent = FileUtils.getFileContent(file, message);

            if (fileContent == null) {

                responseObject = TransferObject.builder()
                        .clientId(SERVER_CLIENT)
                        .receipient(transferObject.getClientId())
                        .message("Can not send file, error reading file: " + message)
                        .build();
            }

            responseObject = TransferObject.builder()
                    .clientId(SERVER_CLIENT)
                    .receipient(transferObject.getClientId())
                    .command("//df")
                    .file(file)
                    .fileContent(fileContent)
                    .build();

            eventBus.publish(SEND_MESSAGE, responseObject);
            return;
        }

        responseObject = TransferObject.builder()
                .clientId(SERVER_CLIENT)
                .receipient(transferObject.getClientId())
                .message("Can not send file, file name not specified: " + message)
                .build();

        eventBus.publish(SEND_MESSAGE, responseObject);
    }

}
