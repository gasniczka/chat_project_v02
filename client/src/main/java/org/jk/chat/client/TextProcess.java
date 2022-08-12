package org.jk.chat.client;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.java.Log;
import org.jk.chat.client.ports.TextReader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Log
@ApplicationScoped
public class TextProcess implements Runnable {

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();


    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    private final CommandInterpreter commandInterpeter;

    private final TextReader textReader;

    public TextProcess(CommandInterpreter commandInterpeter, TextReader textReader) {
        this.commandInterpeter = commandInterpeter;
        this.textReader = textReader;
    }


    @Override
    public void run() {
        read();
    }


    public void read() {

        String text;
        while ((text = textReader.readLine()) != null) {
            commandInterpeter.interpret(text);
        }
    }

}
