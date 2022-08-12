package org.jk.chat.client.domain;


import org.jk.chat.client.ports.TextReader;

import javax.enterprise.context.ApplicationScoped;
import java.util.Scanner;


@ApplicationScoped
public class ConsoleReader implements TextReader {


    public String readLine() {
       return new Scanner(System.in).nextLine();
    }

}
