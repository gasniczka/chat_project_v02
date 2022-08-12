package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.jk.chat.common.TransferObject;
import org.jk.chat.database.HistoryObject;
import org.jk.chat.ports.HistoryRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

import static org.jk.chat.common.Configuration.SERVER_CLIENT;
import static org.jk.chat.event.EventActions.GET_HISTORY;
import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
@ApplicationScoped
public class GetHistoryEventListener {

    private final HistoryRepository historyRepository;
    private final EventBus eventBus;


    public GetHistoryEventListener(HistoryRepository historyRepository, EventBus eventBus) {
        this.historyRepository = historyRepository;
        this.eventBus = eventBus;
    }

    @ConsumeEvent(GET_HISTORY)
    @Blocking
    public void getHistory(TransferObject transferObject) {

        log.info(" -> getHistory: " + transferObject.toString());

        String history = historyRepository.getAllFromRoomByClient(transferObject.getClientId())
                .stream()
                .map(HistoryObject::toString)
                .collect(Collectors.joining("\n"));

        log.info("history: \n" + history);

        TransferObject responseObject = TransferObject.builder()
                .clientId(SERVER_CLIENT)
                .receipient(transferObject.getClientId())
                .message("chat history: \n" + history)
                .build();

        eventBus.send(SEND_MESSAGE, responseObject);
    }

}
