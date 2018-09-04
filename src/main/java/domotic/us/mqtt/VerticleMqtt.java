package domotic.us.mqtt;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

import java.io.UnsupportedEncodingException;

import domotic.us.principal.Verticle1;
import domotic.us.utiles.Configuracion;
import domotic.us.utiles.Utilidades;

public class VerticleMqtt extends AbstractVerticle {

	private static MqttClient clienteMqtt;
	private static MongoClient mongoClient;
	
  @Override
  public void start() {
	  
    MqttClientOptions options = new MqttClientOptions();
    MqttClient client = MqttClient.create(vertx, options);
    clienteMqtt = client.connect(1883,Configuracion.ServidorMosquittoIP,s -> {});
    mongoClient = Verticle1.mongoClient;
    
    client.publishHandler(s -> {
      try {
        String mensaje = new String(s.payload().getBytes(), "UTF-8");
        String topic = s.topicName();        
		String [] cadena = topic.split("/");
		String identificador = cadena[0];
		String gpio = cadena[1];
		Integer numeroGpio = Integer.parseInt(gpio.substring(1,2));
		domotic.us.mongoDB.MetodosBD.guardarEstadoGpioyEnviarWBEstado(identificador, numeroGpio, mensaje, mongoClient, vertx.eventBus());
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    });

    client.connect(1883,Configuracion.ServidorMosquittoIP,s -> {
      // Suscribirse a todos los topics
      client.subscribe(Utilidades.getTodosLosTopicsDeTodosLosIdentificadores());
    });
  }
  
  public static MqttClient getClientMqtt() {
	  return clienteMqtt;
  }
  
}