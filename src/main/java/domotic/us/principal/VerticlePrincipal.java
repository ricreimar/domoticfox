package domotic.us.principal;

import domotic.us.mqtt.VerticleMqtt;
import io.vertx.core.AbstractVerticle;

public class VerticlePrincipal extends AbstractVerticle {

	public void start() {
		
		vertx.deployVerticle(Verticle1.class.getName(), 
				res -> {
					if (res.succeeded()) {
						System.out.println("Verticle1 lanzado correctamente");
					}else {
						System.out.println("Error en el lanzamiento del Verticle1");					
					}
				});
		vertx.deployVerticle(Verticle1.class.getName(), 
				res -> {
					if (res.succeeded()) {
						System.out.println("Verticle1 lanzado correctamente");
					}else {
						System.out.println("Error en el lanzamiento del Verticle1");					
					}
				});
		vertx.deployVerticle(Verticle1.class.getName(), 
				res -> {
					if (res.succeeded()) {
						System.out.println("Verticle1 lanzado correctamente");
					}else {
						System.out.println("Error en el lanzamiento del Verticle1");					
					}
				});
		vertx.deployVerticle(VerticleMqtt.class.getName(), 
				res -> {
					if (res.succeeded()) {
						System.out.println("Verticle Mqtt lanzado correctamente");
					}else {
						System.out.println("Error en el lanzamiento del Verticle servidor Mqtt");					
					}
				});

		}
}