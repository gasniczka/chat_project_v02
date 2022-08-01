package org.jk.chat.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString(exclude = "fileContent")
public class TransferObject implements Serializable {

    // obiekt do przesyłania między klientem i serverem
    String clientId;
    String chatRoom;
    String receipient;
    String message;
    File file;
    byte[] fileContent;

}
