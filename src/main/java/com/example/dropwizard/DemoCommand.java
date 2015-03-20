package com.example.dropwizard;

import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoCommand extends io.dropwizard.cli.Command {
    public DemoCommand() {
        super("demo", "A demonstration command.");
    }

    @Override
    public void configure(Subparser subparser) {
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        System.out.println("Hello, world!");
    }
}
