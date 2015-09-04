package com.loginbox.app.directory.bootstrap;

import com.loginbox.transactor.Transactor;
import com.loginbox.transactor.transactable.Action;
import com.loginbox.transactor.transactable.Query;
import org.apache.ibatis.session.SqlSession;

import static com.loginbox.app.transactor.mybatis.MybatisAdapters.mapper;

public class Gatekeeper {
    private final Transactor<? extends SqlSession> transactor;

    public Gatekeeper(Transactor<? extends SqlSession> transactor) {
        this.transactor = transactor;
    }

    public Action<SqlSession> bootstrapCompletedAction() {
        return mapper(GatekeeperRepository.class)
                .around(GatekeeperRepository::bootstrapCompleted);
    }

    public boolean isBootstrapped() throws Exception {
        return transactor.fetch(isBootstrappedQuery());
    }

    public Query<SqlSession, Boolean> isBootstrappedQuery() {
        return mapper(GatekeeperRepository.class)
                .around(GatekeeperRepository::isBootstrapped);
    }
}
