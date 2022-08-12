package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.jk.chat.common.TransferObject;
import org.jk.chat.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;

import static org.jk.chat.common.Configuration.SERVER_CLIENT;
import static org.jk.chat.event.EventActions.GET_FILES;
import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
@ApplicationScoped
public class GetFilesEventListener {

    private final EventBus eventBus;

    public GetFilesEventListener(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @ConsumeEvent(GET_FILES)
    @Blocking
    public void getFiles(TransferObject transferObject) {

        log.info(" -> getFiles: " + transferObject.toString());

        TransferObject responseObject = TransferObject.builder()
                .clientId(SERVER_CLIENT)
                .receipient(transferObject.getClientId())
                .message("available files: \n" + FileUtils.getFilesToDownload())
                .build();

        eventBus.send(SEND_MESSAGE, responseObject);
    }

}
