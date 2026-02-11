package com.manocorbax.adblocka.core.tunnel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Tunnel {

    private final Socket client;
    private final Socket remote;

    public Tunnel(Socket client, Socket remote) {
        this.client = client;
        this.remote = remote;
    }

    public void start() {
        new Thread(() -> pipe(client, remote)).start();
        new Thread(() -> pipe(remote, client)).start();
    }

    private void pipe(Socket in, Socket out) {
        try {
            in.getInputStream().transferTo(out.getOutputStream());
        } catch (IOException ignored) {
        }
    }
}
