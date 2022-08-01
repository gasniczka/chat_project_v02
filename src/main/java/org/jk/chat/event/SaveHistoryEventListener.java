package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.java.Log;
import org.jk.chat.common.TransferObject;
import org.jk.chat.database.HistoryObject;
import org.jk.chat.ports.HistoryRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static org.jk.chat.event.EventActions.SAVE_HISTORY;


@Log
@ApplicationScoped
public class SaveHistoryEventListener {

    private final HistoryRepository historyRepository;

    @Inject
    public SaveHistoryEventListener(HistoryRepository historyRepository) {

        this.historyRepository = historyRepository;
    }

    @ConsumeEvent(SAVE_HISTORY)
    @Blocking
    public void saveHistory(TransferObject transferObject) {

        log.info(" -> saveHistory: " + transferObject.toString());

        HistoryObject historyObject = HistoryObject.builder()
                .clientId(transferObject.getClientId())
                .chatRoom(transferObject.getChatRoom())
                .message(transferObject.getMessage())
                .build();

        historyRepository.save(historyObject);
        log.info(" <- saveHistory: " + transferObject.toString());
    }

}
