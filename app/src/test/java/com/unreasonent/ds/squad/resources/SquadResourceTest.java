package com.unreasonent.ds.squad.resources;

import com.unreasonent.ds.auth.User;
import com.unreasonent.ds.squad.api.Squad;
import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import com.unreasonent.ds.squad.query.SquadQueries;
import com.unreasonent.ds.squad.query.SquadResult;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SquadResourceTest {
    private final DefaultCommandGateway commandGateway = mock(DefaultCommandGateway.class);
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);

    private final SqlSession sqlSession = mock(SqlSession.class);
    private final SquadQueries mapper = mock(SquadQueries.class);

    private final User user = new User("userid", new HashMap<>());

    private final SquadResource resource = new SquadResource(commandGateway, sqlSessionFactory);

    @Before
    public void wireMocks() {
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.getMapper(SquadQueries.class)).thenReturn(mapper);
    }

    @Test
    public void getNotFound() {
        when(mapper.getSquad("userid")).thenReturn(null);

        try {
            resource.squadForUser(user);
            fail();
        } catch (NotFoundException nfe) {
            /* expected case */
        }
    }

    @Test
    public void getFound() {
        SquadResult result = mock(SquadResult.class);
        when(mapper.getSquad("userid")).thenReturn(result);

        Squad squad = mock(Squad.class);
        when(result.toSquad()).thenReturn(squad);

        assertThat(
                resource.squadForUser(user),
                equalTo(squad)
        );
    }

    @Test
    public void postSaves() {
        Squad squad = mock(Squad.class);

        UpdateSquadCommand command = mock(UpdateSquadCommand.class);
        when(squad.updateCommand(user)).thenReturn(command);

        Response response = resource.updateSquad(user, squad);
        assertThat(response.getStatusInfo(), equalTo(Response.Status.NO_CONTENT));

        verify(commandGateway).sendAndWait(command);
    }
}
