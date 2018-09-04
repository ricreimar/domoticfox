package domotic.us.webSocket;

import io.vertx.core.Handler;


import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;


public class ManejadorWebSocket implements Handler<BridgeEvent> {


    public void handle(BridgeEvent event) {   	
        if (event.type() == BridgeEventType.SOCKET_CREATED ) {
        	System.out.println("---- Se ha creado un socket ----");
        }else if (event.type() == BridgeEventType.REGISTER) { 
            //System.out.println("---- Se ha registrado un manejador desde el cliente del navegador ----");
        }else if (event.type() == BridgeEventType.RECEIVE) { 
            //System.out.println("---- Se ha enviado un dato desde el servidor al cliente del navegador ----");
        }else if (event.type() == BridgeEventType.SOCKET_CLOSED) { 
            System.out.println("---- Se ha cerrado un socket ----");
        }else if (event.type() == BridgeEventType.SOCKET_PING) { 
            //System.out.println("---- PING ----");
        }
        else {
        	System.out.println("---- OTRO ----");
        }
        event.complete(true);
    }
}
