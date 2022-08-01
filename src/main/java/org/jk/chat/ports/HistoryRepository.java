package org.jk.chat.ports;


import org.jk.chat.database.HistoryObject;

import java.util.Collection;


public interface HistoryRepository {

    HistoryObject save(HistoryObject historyObject);

    Collection<HistoryObject> getAll();

    Collection<HistoryObject> getAllFromRoomByClient(String room);

}
