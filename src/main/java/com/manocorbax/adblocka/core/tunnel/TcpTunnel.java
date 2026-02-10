package com.manocorbax.adblocka.core.tunnel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO:
// in the future, i will create a root level certificate so
// i can decrypt TLS and manipulate https req...
// for now, we only filter http
public class TcpTunnel implements Runnable{

    private InputStream in;
    private OutputStream out;

    public TcpTunnel(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[8192];
        int read;

        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();

        } catch (IOException ignored) {}
    }
}
