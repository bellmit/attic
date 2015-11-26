package com.loginbox.app.csrf.mybatis.providers;

import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.csrf.storage.CsrfRepository;
import org.apache.ibatis.session.SqlSession;

/**
 * Transactable repository for CSRF tokens, backed by an SqlSession.
 */
public class TransactedCsrfRepository implements CsrfRepository, AutoCloseable {
    private final SqlSession session;
    private final CsrfRepository mapper;

    public TransactedCsrfRepository(SqlSession session) {
        this.session = session;
        this.mapper = session.getMapper(CsrfRepository.class);
    }

    @Override
    public void insertCsrfToken(String sessionToken, CsrfToken csrfToken) {
        mapper.insertCsrfToken(sessionToken, csrfToken);
    }

    @Override
    public boolean consumeToken(String sessionToken, CsrfToken csrfToken) {
        return mapper.consumeToken(sessionToken, csrfToken);
    }

    @Override
    public void expireTokens() {
        mapper.expireTokens();
    }

    public void commit() {
        session.commit();
    }

    @Override
    public void close() {
        session.close();
    }
}
