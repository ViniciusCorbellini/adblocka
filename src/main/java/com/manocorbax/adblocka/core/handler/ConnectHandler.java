package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.tunnel.TcpTunnel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectHandler implements Handler{

    private String message;
    private Socket client;
    private Socket remote;

    public ConnectHandler(String message) {
        this.message = message;
    }

    public ConnectHandler(String message, Socket client) {
        this.message = message;
        this.client = client;
    }

    @Override
    public void handle() throws IOException {
        this.remote = decomposeConnect(this.message);

        sendOkResponse();

        new Thread(new TcpTunnel(client.getInputStream(), remote.getOutputStream())).start();
        new Thread(new TcpTunnel(remote.getInputStream(), client.getOutputStream())).start();
    }

    private void sendOkResponse() throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.print("HTTP/1.1 200 Connection Established\r\n\r\n");
        out.flush();
    }

    private Socket decomposeConnect(String message) throws IOException {
        //Connect example:
        // CONNECT www.google.com:443 HTTP/1.1
        // Proxy-Connection: keep-alive
        // Connection: keep-alive
        // Host: www.google.com:443
        String[] parts = message.split(" ");
        String target = parts[1]; // www.google.com:443

        String[] hostPort = target.split(":");
        String host = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);

        return new Socket(host, port);
    }
}
