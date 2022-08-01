package org.jk;

import lombok.extern.java.Log;
//import org.jk.example.domain.MyEntity;
//import org.jk.example.event.MessageProducer;
//import org.jk.example.ports.HistoryRepository;
//
//import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Log
@Path("/hello")
public class ExampleResource {

//    @Inject
//    private MessageProducer messageProducer;
//
//    @Inject
//    private org.jk.chat.messaging.MessageProducer jmsMessProd;
//
//    @Inject
//    HistoryRepository historyRepository;


//    @Inject
//    PriceProducer priceProducer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

//        messageProducer.sedMessage("test z rest");

//        MyEntity newEntity = new MyEntity();
//        newEntity.setField("test fieeeeld");
//
//        historyRepository.save(newEntity);
//        System.out.println("------------------");
//        historyRepository.getAll().stream().map(MyEntity::getField).forEach(s -> log.info(s));
//        System.out.println("------------------");
//        historyRepository.getAllByRoom("test fieeeeld").stream().map(MyEntity::getField).forEach(s -> log.warning(s));

//        priceProducer.run();

//        jmsMessProd.sendMessage("test from server");

        return "Hello RESTEasy";
    }

}