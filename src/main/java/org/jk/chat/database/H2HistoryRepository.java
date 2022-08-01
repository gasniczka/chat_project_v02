package org.jk.chat.database;

import lombok.AllArgsConstructor;
import org.jk.chat.ports.HistoryRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;


@Singleton
@AllArgsConstructor
public class H2HistoryRepository implements HistoryRepository {

    @Inject
    EntityManager entityManager;


    @Override
    @Transactional
    public HistoryObject save(HistoryObject historyObject) {
        entityManager.persist(historyObject);
        entityManager.flush();
        return historyObject;
    }

    @Override
    public Collection<HistoryObject> getAll() {

        List<HistoryObject> historyObjectList = entityManager
                .createQuery("from HistoryObject o order by o.id", HistoryObject.class)
                .getResultList();

        return historyObjectList;
    }

    @Override
    public Collection<HistoryObject> getAllFromRoomByClient(String clientId) {

        String nativeQuery = """
                select o.* 
                  from HistoryObject o 
                 where o.chatRoom in (select distinct r.chatRoom 
                                        from HistoryObject r
                                       where r.clientId = :clientId)
                 order by o.id                      
                """;

        List<HistoryObject> historyObjectList = entityManager
                .createNativeQuery(nativeQuery, HistoryObject.class)
//                .createQuery("from HistoryObject o where o.chatRoom = :room order by o.id", HistoryObject.class)
                .setParameter("clientId", clientId)
                .getResultList();

        return historyObjectList;
    }

}
