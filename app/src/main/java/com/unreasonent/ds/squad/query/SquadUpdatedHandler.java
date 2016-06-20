package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.events.SquadUpdatedEvent;
import org.apache.ibatis.session.SqlSessionFactory;
import org.axonframework.eventhandling.annotation.EventHandler;

import static com.unreasonent.ds.mybatis.Transact.transact;

public class SquadUpdatedHandler {
    private final SqlSessionFactory sqlSessionFactory;

    public SquadUpdatedHandler(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @EventHandler
    public void squadUpdated(SquadUpdatedEvent event) {
        transact(sqlSessionFactory, session -> {
            SquadQueries queries = session.getMapper(SquadQueries.class);
            queries.deleteSquad(event.getUserId());
            event.getSquad().getCharacters().forEach(character -> queries.insertCharacter(event.getUserId(), character));
        });
    }
}
