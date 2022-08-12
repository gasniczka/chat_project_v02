package org.jk.chat.common;


import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Singleton
public final class Configuration {

    public static final String JOINING_DELIMITER = "\n";

    public static final String SERVER_CLIENT = "server";

    private final Set<String> chatRooms;


    private Configuration() {
        chatRooms = new HashSet<>();
        chatRooms.add("general");
    }


    public void addChatRoom(String room) {
        chatRooms.add(room);
    }

    public String getChatRooms() {
        return chatRooms.stream().collect(Collectors.joining(JOINING_DELIMITER));
    }

}
