package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.jk.chat.common.Configuration;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;

import static org.jk.chat.common.Configuration.SERVER_CLIENT;
import static org.jk.chat.event.EventActions.GET_ROOMS;
import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
@ApplicationScoped
public class GetRoomsEventListener {

    private final EventBus eventBus;
    private final Configuration configuration;


    public GetRoomsEventListener(EventBus eventBus, Configuration configuration) {
        this.eventBus = eventBus;
        this.configuration = configuration;
    }

    @ConsumeEvent(GET_ROOMS)
    @Blocking
    public void getRooms(TransferObject transferObject) {

        log.info(" -> getRooms: " + transferObject.toString());

        TransferObject responseObject = TransferObject.builder()
                .clientId(SERVER_CLIENT)
                .receipient(transferObject.getClientId())
                .message("chat rooms: \n" + configuration.getChatRooms())
                .build();

        eventBus.publish(SEND_MESSAGE, responseObject);


        eventBus.send(SEND_MESSAGE, responseObject);
    }

}
