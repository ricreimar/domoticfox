package domotic.us.webSocket;

import java.util.ArrayList;
import java.util.List;

import domotic.us.utiles.Utilidades;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class WebSockets {
	
    public static SockJSHandler eventBusHandler(EventBus eb, Vertx v) {
    	
    	List<PermittedOptions> listaOut = new ArrayList<>();

    	
    	
		for(String s: Utilidades.getTopicsWSDeTodosLosIdentificadores()) {
			listaOut.add(new PermittedOptions().setAddressRegex(s));
		}
    	
		System.out.println(Utilidades.getTopicsWSDeTodosLosIdentificadores());
    	BridgeOptions options = new BridgeOptions();
    	options.setOutboundPermitted(listaOut);
        ManejadorWebSocket wb = new ManejadorWebSocket();
        SockJSHandler sockJSHandler = SockJSHandler.create(v);

        return sockJSHandler.bridge(options, wb);
    }

}
