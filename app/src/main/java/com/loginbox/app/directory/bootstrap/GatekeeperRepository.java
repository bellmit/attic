package com.loginbox.app.directory.bootstrap;

public interface GatekeeperRepository {
    public void bootstrapCompleted();

    public boolean isBootstrapped();
}
