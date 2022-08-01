package org.jk.chat.domain;

import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.common.Action;
import org.jk.chat.common.TransferObject;
import org.jk.chat.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jk.chat.event.EventActions.GET_HISTORY;
import static org.jk.chat.event.EventActions.SAVE_HISTORY;
import static org.jk.chat.event.EventActions.SEND_MESSAGE;


@Log
@ApplicationScoped
public class ActionDispatcher {

    private static final Set<String> chatRooms = new HashSet<>();

    private static final String SPLIT_SEPARATOR = " ";
    private static final String JOINING_DELIMITER = "\n";

    public static final String SERVER_CLIENT = "server";


    private final EventBus eventBus;

    @Inject
    public ActionDispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
        chatRooms.add("general");
    }

    public void dispatch(TransferObject transferObject) {

        log.info(" -> dispatch: " + transferObject.toString());

        String clientId = transferObject.getClientId();
        String chatroom = transferObject.getChatRoom();
        String receipient = transferObject.getReceipient();
        String message = transferObject.getMessage();

        if (SERVER_CLIENT.equals(clientId)) {
            // odrzuć wszystkie wiadomości wysylane przez siebie
            log.info("reject sent by itself");
            return;
        }


        if (SERVER_CLIENT.equals(receipient)) {
            // jeżeli nadawane są do servera to wykonaj akcje

            log.info("do action on server: " + transferObject.toString());
            log.info(clientId);
            log.info(message);

            String[] command = message.split(SPLIT_SEPARATOR);

            switch (Action.getByValue(command[0])) {

                case VIEW_HISTORY -> {
                    log.info("looking for history");

                    eventBus.publish(GET_HISTORY, transferObject);
                    return;
                }
                case VIEW_ROOMS -> {
                    log.info("looking for rooms");

                    TransferObject responseObject = TransferObject.builder()
                            .clientId(SERVER_CLIENT)
                            .receipient(transferObject.getClientId())
                            .message("chat rooms: \n" + chatRooms.stream().collect(Collectors.joining(JOINING_DELIMITER)))
                            .build();

                    eventBus.publish(SEND_MESSAGE, responseObject);
                    return;
                }
                case CHANGE_ROOM -> {
                    log.info("changing room");

                    if (command.length > 1 && StringUtils.isNotBlank(command[1])) {

                        TransferObject responseObject = TransferObject.builder()
                                .clientId(SERVER_CLIENT)
                                .receipient(transferObject.getClientId())
                                .chatRoom(command[1])
                                .message("change room")
                                .build();

                        chatRooms.add(command[1]);

                        eventBus.publish(SEND_MESSAGE, responseObject);
                    }

                    return;
                }
                case VIEW_FILES -> {
                    log.info("looking for files");

                    TransferObject responseObject = TransferObject.builder()
                            .clientId(SERVER_CLIENT)
                            .receipient(transferObject.getClientId())
                            .message("availablefiles: \n" + FileUtils.getFilesToDownload())
                            .build();

                    eventBus.publish(SEND_MESSAGE, responseObject);
                    return;
                }
                case DOWNLOAD_FILE -> {
                    log.info("sending files");

                    TransferObject responseObject = null;

                    if (command.length > 1 && StringUtils.isNotBlank(command[1])) {


                        File file = FileUtils.getFileByName(command[1]);

                        if (file == null) {

                            responseObject = TransferObject.builder()
                                    .clientId(SERVER_CLIENT)
                                    .receipient(transferObject.getClientId())
                                    .message("Can not send file, file does not exists: " + command[1])
                                    .build();
                        }

                        byte[] fileContent = FileUtils.getFileContent(file, command[1]);

                        if (fileContent == null) {

                            responseObject = TransferObject.builder()
                                    .clientId(SERVER_CLIENT)
                                    .receipient(transferObject.getClientId())
                                    .message("Can not send file, error reading file: " + command[1])
                                    .build();
                        }

                        responseObject = TransferObject.builder()
                                .clientId(SERVER_CLIENT)
                                .receipient(transferObject.getClientId())
                                .message("save file")
                                .file(file)
                                .fileContent(fileContent)
                                .build();

                        eventBus.publish(SEND_MESSAGE, responseObject);

                        return;
                    }

                    responseObject = TransferObject.builder()
                            .clientId(SERVER_CLIENT)
                            .receipient(transferObject.getClientId())
                            .message("Can not send file, file name not specified: " + command[1])
                            .build();

                    eventBus.publish(SEND_MESSAGE, responseObject);
                    return;
                }
                case SEND_FILE -> {
                    log.info("saving files");

                    if (command.length > 1 && StringUtils.isNotBlank(command[1]) && transferObject.getFile() != null) {

                        FileUtils.saveFile(transferObject.getFile(), transferObject.getFileContent());
                    }
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
