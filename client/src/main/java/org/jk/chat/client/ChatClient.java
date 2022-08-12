package org.jk.chat.client;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.util.Random;


@Getter
// @ApplicationScoped
// can be destroyed and recreated at runtime
// Existing injection points just work because the injected proxy delegates to the current instance.
@Singleton
public class ChatClient {


    private final String clientId = String.format("ID_%s", StringUtils.leftPad(String.valueOf(new Random().nextInt(100)), 5, "0"));

    @Setter
    private String room = "general";

}
