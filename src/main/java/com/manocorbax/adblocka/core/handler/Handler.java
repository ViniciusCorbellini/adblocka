package com.manocorbax.adblocka.core.handler;

import java.io.IOException;
import java.net.Socket;

public interface Handler {

    void handle() throws IOException;

    static Handler getHandlerFor(String message, Socket client){
          if (message.startsWith("CONNECT")){
              return new ConnectHandler(message, client);
          }
          return new HttpHandler(message, client);
    }
}
