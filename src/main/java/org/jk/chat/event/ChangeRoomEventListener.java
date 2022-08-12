package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.common.Configuration;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;

import static org.jk.chat.common.Configuration.SERVER_CLIENT;
import static org.jk.chat.event.EventActions.CHANGE_ROOM;
import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
@ApplicationScoped
public class ChangeRoomEventListener {

    private final EventBus eventBus;
    private final Configuration configuration;

    public ChangeRoomEventListener(EventBus eventBus, Configuration configuration) {

        this.eventBus = eventBus;
        this.configuration = configuration;
    }

    @ConsumeEvent(CHANGE_ROOM)
    @Blocking
    public void changeRoom(TransferObject transferObject) {

        log.info(" -> changeRoom: " + transferObject.toString());

        String message = transferObject.getMessage();

        if (StringUtils.isNotBlank(message)) {

            TransferObject responseObject = TransferObject.builder()
                    .clientId(SERVER_CLIENT)
                    .receipient(transferObject.getClientId())
                    .chatRoom(message)
                    .command("//sr")
                    .build();

            configuration.addChatRoom(message);

            eventBus.publish(SEND_MESSAGE, responseObject);
        }
    }

}
