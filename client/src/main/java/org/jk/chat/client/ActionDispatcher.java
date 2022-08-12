package org.jk.chat.client;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.client.io.FileUtils;
import org.jk.chat.client.ports.TextWriter;
import org.jk.chat.common.Action;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;


@Log
@ApplicationScoped
public class ActionDispatcher {


    public static final String SERVER_CLIENT = "server";

    private final ChatClient chatClient;
    private final TextWriter textWriter;


    public ActionDispatcher(ChatClient chatClient, TextWriter textWriter) {

        this.chatClient = chatClient;
        this.textWriter = textWriter;
    }


    public void dispatch(TransferObject transferObject) {

        log.info(" -> dispatch, " + transferObject.toString());

        String clientId = transferObject.getClientId();
        String chatroom = transferObject.getChatRoom();
        String receipient = transferObject.getReceipient();
        String command = transferObject.getCommand();
        String message = transferObject.getMessage();


        if (SERVER_CLIENT.equals(receipient)) {
            // odrzuć wszystkie wiadomości wysylane do servera
            return;
        }


        if (clientId.equals(chatClient.getClientId())) {
            // odrzuć wszystkie wiadomości wysylane przez siebie
            return;
        }


        if (SERVER_CLIENT.equals(clientId) && chatClient.getClientId().equals(receipient)) {
            // jeżeli nadawca jest server i odbiorcą ten klient

            if (StringUtils.isNotBlank(chatroom) && Action.CHANGE_ROOM == Action.getByValue(command)) {
                // zmien chatroom
                chatClient.setRoom(chatroom);

                textWriter.printLine(clientId + "@null");
                textWriter.printLine("changed room to: " + chatroom);
                return;
            }


            if (transferObject.getFile() != null && Action.DOWNLOAD_FILE == Action.getByValue(command)) {
                // zapisz plik
                FileUtils.saveFile(chatClient.getClientId(), transferObject.getFile(), transferObject.getFileContent());

                textWriter.printLine(clientId + "@null");
                textWriter.printLine("saved file: " + transferObject.getFile());
                return;
            }


            // procesuj informacje z servera
            textWriter.printLine(clientId + "@" + chatroom + " " + message);
            return;
        }


        if (chatroom.equals(chatClient.getRoom())) {
            // jezeli nadano w tym samym pokoju
            textWriter.printLine(clientId + "@" + chatroom + " " + message);
            return;
        }

    }

}
