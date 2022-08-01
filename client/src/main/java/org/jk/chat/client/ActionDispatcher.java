package org.jk.chat.client;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.client.io.FileUtils;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@Log
@ApplicationScoped
public class ActionDispatcher {


    public static final String SERVER_CLIENT = "server";

    private final ChatClient chatClient;

    @Inject
    public ActionDispatcher(ChatClient chatClient) {

        this.chatClient = chatClient;
    }


    public void dispatch(TransferObject transferObject) {

        log.info(" -> dispatch, " + transferObject.toString());


        String clientId = transferObject.getClientId();
        String chatroom = transferObject.getChatRoom();
        String receipient = transferObject.getReceipient();
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


            if (StringUtils.isNotBlank(chatroom) && "change room".equals(message)) {
                // zmien chatroom
                chatClient.setRoom(chatroom);

                System.err.println(clientId + "@null");
                System.err.println("changed room to: " + chatroom);
                return;
            }


            if (transferObject.getFile() != null && "save file".equals(message)) {
                // zapis pliku
                FileUtils.saveFile(chatClient.getClientId(), transferObject.getFile(), transferObject.getFileContent());
                return;
            }


            // procesuj informacje z servera
            System.err.println(clientId + "@" + chatroom);
            System.err.println(message);
            return;
        }

        if (chatroom.equals(chatClient.getRoom())) {
            // jezeli nadano w tym samym pokoju
            System.err.println(clientId + "@" + chatroom + " " + message);
            return;
        }

    }

}
