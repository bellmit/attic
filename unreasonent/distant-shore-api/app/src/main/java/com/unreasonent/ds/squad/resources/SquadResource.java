package com.unreasonent.ds.squad.resources;

import com.unreasonent.ds.auth.User;
import com.unreasonent.ds.mybatis.Transact;
import com.unreasonent.ds.squad.api.Squad;
import com.unreasonent.ds.squad.commands.UpdateSquadCommand;
import com.unreasonent.ds.squad.query.SquadQueries;
import com.unreasonent.ds.squad.query.SquadResult;
import io.dropwizard.auth.Auth;
import org.apache.ibatis.session.SqlSessionFactory;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.unreasonent.ds.mybatis.Transact.transacted;

@Path("/squad")
public class SquadResource {
    private final DefaultCommandGateway commandGateway;
    private final SqlSessionFactory sqlSessionFactory;

    public SquadResource(DefaultCommandGateway commandGateway, SqlSessionFactory sqlSessionFactory) {
        this.commandGateway = commandGateway;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @GET
    @Produces("application/json")
    public Squad squadForUser(@Auth User user) {
        return transacted(sqlSessionFactory, session -> {
            SquadQueries queries = session.getMapper(SquadQueries.class);
            SquadResult result = queries.getSquad(user.getUserId());
            if (result != null)
                return result.toSquad();
            throw new NotFoundException();
        });
    }

    @POST
    @Consumes("application/json")
    public Response updateSquad(@Auth User user, @Valid Squad squad) {
        UpdateSquadCommand command = squad.updateCommand(user);
        commandGateway.sendAndWait(command);

        return Response.noContent()
                .build();
    }
}
