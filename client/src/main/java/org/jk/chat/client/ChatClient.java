package org.jk.chat.client;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;


@Getter
@ApplicationScoped
public class ChatClient {


    private String clientId = String.format("ID_%s", StringUtils.leftPad(String.valueOf(new Random().nextInt(100)), 5, "0"));

    @Setter
    private String room = "general";


    public boolean doAcceptMessage(String clientId, String room) {
        return this.clientId.equals(clientId) || this.room.equals(room);
    }

}
