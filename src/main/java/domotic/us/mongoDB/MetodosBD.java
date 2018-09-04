package domotic.us.mongoDB;

import java.util.ArrayList;
import java.util.List;

import domotic.us.modelos.TipoAnalogico;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class MetodosBD {
	
	public static List<String> getAllESPRegistrados(MongoClient mongoClient){
		return new ArrayList<String>();
	}
	
	//Obtener todos los ESP8266
	public static String getTipoSensor(MongoClient mongoClient, String id){
		List<String> lista = new ArrayList<>();
		JsonObject query = new JsonObject().put("identificadorSerie", id);
		mongoClient.find("ESP8266", query, res -> {
			if(res.succeeded()) {
				for(JsonObject json : res.result()) {
					String identificadorSerie = json.getString("identificadorSerie");
					boolean registrado = Boolean.valueOf(json.getString("registrado"));
					TipoAnalogico tipoAnalogico = TipoAnalogico.valueOf(json.getString("tipoSensor"));
					ESP8266 e = new ESP8266 (identificadorSerie, registrado,tipoAnalogico);
				}
			}else {
			res.cause().printStackTrace();
			}
		});
		return lista.get(0);
	}
	
	public static void guardarEstadoGpioyEnviarWBEstado(String identificador, Integer numeroGpio, String mensaje, MongoClient mongoClient, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);
	    JsonObject update = new JsonObject();
	    if(mensaje.equals("1")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".estado",true));
	    	e.publish(identificador+"/"+numeroGpio+"/estado", "true");
	    }else if(mensaje.equals("0")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".estado",false));
	    	e.publish(identificador+"/"+numeroGpio+"/estado", "false");
	    }
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    	}else {
	    	     res.cause().printStackTrace();
	    	}
	    });
	}
	
	public static void guardarAutomaticoGpio(String identificador, Integer numeroGpio, 
			String mensaje, MongoClient mongoClient, RoutingContext r, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	    JsonObject update = new JsonObject();
	    		    if(mensaje.equals("true")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".automatico",true));
	    }else if(mensaje.equals("false")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".automatico",false));
	    }

	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		String modoRegistrado ;
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		modoRegistrado = (mensaje.equals("true")) ? "automatico": "manual";
	    		r.response().setStatusCode(200).end(new JsonObject().put("mensaje",modoRegistrado).encodePrettily());
	    	}else {
	    		r.response().setStatusCode(404).end();
	    	}
	    });
	}
	
	public static void guardarActivadoGpio(String identificador, Integer numeroGpio, 
			String mensaje, MongoClient mongoClient, RoutingContext r, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	    JsonObject update = new JsonObject();
	    if(numeroGpio >=1 && numeroGpio <=7) {
	    	if(mensaje.equals("true")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".activado",true));
	    }else if(mensaje.equals("false")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".activado",false));
	    }
	    }
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		String modoActivado ;
	    		if(mensaje.equals("true")) {
	    			modoActivado = numeroGpio+" activado";
	    			r.response().setStatusCode(200).end(new JsonObject().put("mensaje",modoActivado).encodePrettily());
	    		}else {
	    			modoActivado = numeroGpio+" desactivado";
	    			r.response().setStatusCode(200).end(new JsonObject().put("mensaje",modoActivado).encodePrettily());
	    		}
	    		
	    	}else {
	    		r.response().setStatusCode(404).end();
	    	}
	    });
	}

	
	public static void guardarUmbralGpio(String identificador, 
			Integer numeroGpio, Double umbral, MongoClient mongoClient, RoutingContext r, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	    JsonObject update = new JsonObject();
	    update.put("$set", new JsonObject().put("gpio"+numeroGpio+".umbral",umbral));
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		System.out.println("Manda por websocket: "+numeroGpio);
	    		r.response().setStatusCode(200).end(new JsonObject()
	    				.put("mensaje", "Umbral actualizado").encodePrettily());
	    	}else {
	    		r.response().setStatusCode(404).end();
	    	}
	    });
	}
	public static void guardarTextoGpio(String identificador, 
			Integer numeroGpio, String nombre, MongoClient mongoClient, RoutingContext r, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	    JsonObject update = new JsonObject();
	    update.put("$set", new JsonObject().put("gpio"+numeroGpio+".nombre",nombre));
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		System.out.println("Manda por websocket: "+numeroGpio);
	    		r.response().setStatusCode(200).end(new JsonObject()
	    				.put("mensaje", "Nombre actualizado").encodePrettily());
	    	}else {
	    		r.response().setStatusCode(404).end();
	    	}
	    });
	}
	public static void guardarTextoPulsador1(String identificador, String nombre, 
			MongoClient mongoClient, RoutingContext r, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	    JsonObject update = new JsonObject();
	    update.put("$set", new JsonObject().put("pulsador1",nombre));
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		r.response().setStatusCode(200).end(new JsonObject()
	    				.put("mensaje", "Pulsador1 actualizado").encodePrettily());
	    	}else {
	    		r.response().setStatusCode(404).end();
	    	}
	    });
	}
	public static void guardarTextoPulsador2(String identificador, 
			String nombre, MongoClient mongoClient, RoutingContext r, EventBus e) {
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	   
	        
	    JsonObject update = new JsonObject();
	    update.put("$set", new JsonObject().put("pulsador2",nombre));
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		r.response().setStatusCode(200).end(new JsonObject()
	    				.put("mensaje", "Nombre actualizado").encodePrettily());
	    	}else {
	    		r.response().setStatusCode(404).end();
	    	}
	    });
	}
	

	public static void guardarSentidoGpio(String identificador, 
			Integer numeroGpio, String mensaje, 
			MongoClient mongoClient, RoutingContext r, EventBus e) {
		
	    JsonObject query = new JsonObject().put("identificador", identificador);	    
	    
	    JsonObject update = new JsonObject();
	    if(mensaje.equals("true")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".sentido",true));
	    }else if(mensaje.equals("false")) {
	    	update.put("$set", new JsonObject().put("gpio"+numeroGpio+".sentido",false));
	    }
	    
	    mongoClient.updateCollection("estadoesp8266", query, update, res -> {
	    	if(res.succeeded()) {
	    		//e.publish(identificador+"/"+numeroGpio+"/"+"sentido", mensaje);
	    		System.out.println(identificador+"/cambioConfiguracion");
	    		e.publish(identificador+"/cambioConfiguracion", "");
	    		r.response().setStatusCode(200).end(new JsonObject()
	    				.put("mensaje", "Sentido actualizado").encodePrettily());
	    	}else {
	    	     res.cause().printStackTrace();
	    	}
	    });
	}
	
}