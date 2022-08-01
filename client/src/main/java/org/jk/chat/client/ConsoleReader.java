package org.jk.chat.client;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.java.Log;
import org.jk.chat.client.CommandInterpreter;
import org.jk.chat.common.TransferObject;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Log
@ApplicationScoped
public class ConsoleReader implements Runnable {

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();


    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }



    @Inject
    CommandInterpreter commandInterpeter;


    @Override
    public void run() {
        read();
    }


    public void read() {

        String text;

        TransferObject transferObject;


        while ((text = new Scanner(System.in).nextLine()) != null) {

            commandInterpeter.interpret(text);

        }

    }


}
