package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.net.tunnel.Tunnel;

import java.io.OutputStream;
import java.net.Socket;


public class ConnectHandler implements RequestHandler{

    @Override
    public boolean supports(RequestContext context) {
        return "CONNECT".equalsIgnoreCase(context.method());
    }

    @Override
    public void handle(RequestContext context) throws Exception {

        // easy debugging
        // System.out.println(context.toString());

        Socket client = context.clientSocket();
        Socket remote = new Socket(context.host(), context.port());

        // 200 OK answer
        sendOkAnswer(client);
        Tunnel tunnel = new Tunnel(client, remote);
        tunnel.start();
    }

    private void sendOkAnswer(Socket client) throws Exception{
        OutputStream clientOut = client.getOutputStream();
        clientOut.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
        clientOut.flush();
    }
}
