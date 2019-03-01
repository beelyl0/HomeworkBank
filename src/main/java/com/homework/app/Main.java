package com.homework.app;

import org.eclipse.jetty.server.Server;

public final class Main {

    private final static int LISTEN_PORT = 8080;

    public static void main(String[] args) throws Exception {
        BankApplication app = new BankApplication();
        Server server = app.start(LISTEN_PORT);
        server.join();
    }

}
