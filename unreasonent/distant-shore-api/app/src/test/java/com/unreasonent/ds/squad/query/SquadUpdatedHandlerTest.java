package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.api.Character;
import com.unreasonent.ds.squad.api.Squad;
import com.unreasonent.ds.squad.events.SquadUpdatedEvent;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class SquadUpdatedHandlerTest {
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);

    private final SqlSession sqlSession = mock(SqlSession.class);
    private final SquadQueries mapper = mock(SquadQueries.class);

    private final SquadUpdatedHandler handler = new SquadUpdatedHandler(sqlSessionFactory);

    @Before
    public void wireMocks() {
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.getMapper(SquadQueries.class)).thenReturn(mapper);
    }

    @Test
    public void savesSquads() {
        String userId = "userId";
        Character character = mock(Character.class);
        Squad squad = new Squad(Arrays.asList(character));

        SquadUpdatedEvent event = new SquadUpdatedEvent(userId, squad);
        handler.squadUpdated(event);

        InOrder order = inOrder(mapper, sqlSession);
        order.verify(mapper).deleteSquad("userId");
        order.verify(mapper).insertCharacter("userId", character);
        order.verify(sqlSession).commit();
        order.verify(sqlSession).close();
    }
}