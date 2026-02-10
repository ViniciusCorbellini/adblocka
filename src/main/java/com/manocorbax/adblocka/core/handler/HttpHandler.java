package com.manocorbax.adblocka.core.handler;

import java.net.Socket;

public class HttpHandler implements Handler{

    String message;
    Socket client;

    public HttpHandler(String message, Socket client) {
        this.message = message;
        this.client = client;
    }

    @Override
    public void handle() {

    }
}
