package org.jk.chat.client.domain;


import org.jk.chat.client.ports.TextWriter;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ConsoleWriter implements TextWriter {

    public void printLine(String text) {
        System.err.println(text);
    }

}
