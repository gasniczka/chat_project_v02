package org.jk.chat.client;


import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.client.io.FileUtils;
import org.jk.chat.client.messaging.MessageProducer;
import org.jk.chat.common.Action;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.EnumSet;

import static org.jk.chat.client.ActionDispatcher.SERVER_CLIENT;
import static org.jk.chat.common.Action.CHANGE_ROOM;
import static org.jk.chat.common.Action.DOWNLOAD_FILE;
import static org.jk.chat.common.Action.VIEW_FILES;
import static org.jk.chat.common.Action.VIEW_HISTORY;
import static org.jk.chat.common.Action.VIEW_ROOMS;


@Log
@ApplicationScoped
public class CommandInterpreter {


    private static final String CLIENT_FILES_DIRECTORY = "/chatClientFiles/";
    private static final String SPLIT_SEPARATOR = " ";

    @Inject
    MessageProducer messageProducer;

    @Inject
    ChatClient chatClient;


    public void interpret(String text) {

        log.info("text: '" + text + "'");

        if (Action.VIEW_HELP == Action.getByValue(text)) {

            System.out.println(Action.HELP_MESSAGE);
            return;
        }


        String[] command = text.split(SPLIT_SEPARATOR);

//        switch (Action.getByValue(command[0])) {
//
//            case VIEW_HELP -> {
//
//                System.out.println(Action.HELP_MESSAGE);
//                return;
//            }
//            case SEND_FILE -> {
//            }
//            case VIEW_HISTORY, CHANGE_ROOM, VIEW_ROOMS, VIEW_FILES, DOWNLOAD_FILE -> {
//
//                TransferObject transferObject = TransferObject.builder()
//                        .clientId(chatClient.getClientId())
//                        .chatRoom(chatClient.getRoom())
//                        .receipient(SERVER_CLIENT)
//                        .message(text)
//                        .build();
//
//                messageProducer.sendTransferObject(transferObject);
//                return;
//            }
//            default -> {
//
//                TransferObject transferObject = TransferObject.builder()
//                        .clientId(chatClient.getClientId())
//                        .chatRoom(chatClient.getRoom())
//                        .message(text)
//                        .build();
//
//                messageProducer.sendTransferObject(transferObject);
//
//            }
//
//        }

        final EnumSet<Action> simpleRequestActions = EnumSet.of(VIEW_HISTORY, VIEW_ROOMS, VIEW_FILES);

        if (simpleRequestActions.contains(Action.getByValue(text))) {

            TransferObject transferObject = TransferObject.builder()
                    .clientId(chatClient.getClientId())
                    .chatRoom(chatClient.getRoom())
                    .receipient(SERVER_CLIENT)
                    .message(text)
                    .build();

            messageProducer.sendTransferObject(transferObject);
            return;
        }


        final EnumSet<Action> complexRequestActions = EnumSet.of(CHANGE_ROOM, DOWNLOAD_FILE);

        if (complexRequestActions.contains(Action.getByValue(command[0]))) {

            TransferObject transferObject = TransferObject.builder()
                    .clientId(chatClient.getClientId())
                    .chatRoom(chatClient.getRoom())
                    .receipient(SERVER_CLIENT)
                    .message(text)
                    .build();

            messageProducer.sendTransferObject(transferObject);
            return;
        }


        if (Action.SEND_FILE == Action.getByValue(command[0])) {

            if ((command.length == 1) || (command.length > 1 && StringUtils.isBlank(command[1]))) {
                System.out.println("filename is not specified");
                return;
            }


            File file = FileUtils.getFileByName(command[1]);


            byte[] fileContent = FileUtils.getFileContent(file, command[1]);

            if (fileContent != null) {

                TransferObject transferObject = TransferObject.builder()
                        .clientId(chatClient.getClientId())
                        .receipient(SERVER_CLIENT)
                        .message(text)
                        .file(file)
                        .fileContent(fileContent)
                        .build();

                messageProducer.sendTransferObject(transferObject);
            }

            return;
        }

        // for sending ordinary message
        TransferObject transferObject = TransferObject.builder()
                .clientId(chatClient.getClientId())
                .chatRoom(chatClient.getRoom())
                .message(text)
                .build();

        messageProducer.sendTransferObject(transferObject);
    }

}
