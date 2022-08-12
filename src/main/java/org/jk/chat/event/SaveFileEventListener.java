package org.jk.chat.event;


import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jk.chat.common.TransferObject;
import org.jk.chat.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;

import java.io.File;

import static org.jk.chat.event.EventActions.SAVE_FILE;


@Log
@ApplicationScoped
public class SaveFileEventListener {


    @ConsumeEvent(SAVE_FILE)
    @Blocking
    public void saveFile(TransferObject transferObject) {

        log.info(" -> saveFile: " + transferObject.toString());

        String message = transferObject.getMessage();

        if (transferObject.getFile() != null) {

            FileUtils.saveFile(transferObject.getFile(), transferObject.getFileContent());
        }
    }

}
