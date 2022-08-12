package org.jk.chat.domain;

import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.jk.chat.common.Action;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;

import static org.jk.chat.common.Configuration.SERVER_CLIENT;
import static org.jk.chat.event.EventActions.CHANGE_ROOM;
import static org.jk.chat.event.EventActions.GET_FILES;
import static org.jk.chat.event.EventActions.GET_HISTORY;
import static org.jk.chat.event.EventActions.GET_ROOMS;
import static org.jk.chat.event.EventActions.SAVE_FILE;
import static org.jk.chat.event.EventActions.SAVE_HISTORY;
import static org.jk.chat.event.EventActions.SEND_FILE;


@Log
@ApplicationScoped
public class ActionDispatcher {


    private final EventBus eventBus;

    public ActionDispatcher(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    public void dispatch(TransferObject transferObject) {

        log.info(" -> dispatch: " + transferObject.toString());

        String clientId = transferObject.getClientId();
        String chatroom = transferObject.getChatRoom();
        String command = transferObject.getCommand();
        String receipient = transferObject.getReceipient();
        String message = transferObject.getMessage();

        if (SERVER_CLIENT.equals(clientId)) {
            // odrzuć wszystkie wiadomości wysylane przez siebie
            log.info("reject sent by itself");
            return;
        }


        if (SERVER_CLIENT.equals(receipient) && command != null) {
            // jeżeli nadawane są do servera to wykonaj akcje

            log.info("do action on server: " + transferObject.toString());
            log.info(clientId);
            log.info(command);
            log.info(message);

            switch (Action.getByValue(command)) {

                case VIEW_HISTORY -> {
                    log.info("looking for history");

                    eventBus.publish(GET_HISTORY, transferObject);
                    return;
                }
                // kolejne akcje mozna rozpisac jako kolejne eventy
                case VIEW_ROOMS -> {
                    log.info("looking for rooms");

                    eventBus.publish(GET_ROOMS, transferObject);
                    return;
                }
                case CHANGE_ROOM -> {
                    log.info("changing room");

                    eventBus.publish(CHANGE_ROOM, transferObject);
                    return;
                }
                case VIEW_FILES -> {
                    log.info("looking for files");

                    eventBus.publish(GET_FILES, transferObject);
                    return;
                }
                case DOWNLOAD_FILE -> {
                    log.info("sending file");

                    eventBus.publish(SEND_FILE, transferObject);
                    return;
                }
                case SEND_FILE -> {
                    log.info("saving file");

                    eventBus.publish(SAVE_FILE, transferObject);
                    return;
                }
                default -> {
                    return;
                }

            }
        }

        // wszystkie pozostale
        // zapisz do bazy
        eventBus.publish(SAVE_HISTORY, transferObject);

        // zaloguj
        log.info(clientId + "@" + chatroom + " " + message);
    }


}
