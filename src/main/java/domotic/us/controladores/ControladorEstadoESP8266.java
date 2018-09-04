package domotic.us.controladores;

import java.util.Calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domotic.us.modelos.DatoSensor;
import domotic.us.modelos.EstadoESP8266;
import domotic.us.modelos.Gpio;
import domotic.us.modelos.TipoAnalogico;
import domotic.us.mqtt.VerticleMqtt;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mqtt.MqttClient;

public class ControladorEstadoESP8266 {

	public static void getTipoAnalogicoDePlaca(RoutingContext r, EventBus e, MongoClient mongoClient) {

		String identificadorPlaca = r.request().getParam("identificadorSerieId");
		JsonObject query = new JsonObject().put("identificador", identificadorPlaca);
		mongoClient.findOne("estadoesp8266", query, new JsonObject().put("_id", false), res -> {
			if (res.succeeded()) {
				if (res.result() != null) {
					EstadoESP8266 esp = Json.decodeValue(Json.encode(res.result()), EstadoESP8266.class);
					JsonObject j = new JsonObject();
					j.put("tipoAnalogico", esp.getTipoAnalogico());
					r.response().setStatusCode(200).end(j.encodePrettily());
				} else {
					r.response().setStatusCode(404).end("El dispositivo no está registrado en la BD");
				}
			} else {
				r.response().setStatusCode(200).end("Error en la BD");
			}
		});
	}

	public static void getDatosUsuario(RoutingContext r, EventBus e, MongoClient mongoClient) {

		String usuario = r.request().getParam("elementid");
		JsonObject query = new JsonObject().put("username", usuario);
		mongoClient.findOne("usuario", query, null, res -> {
			if (res.succeeded()) {
				if (res.result() != null) {
					JsonObject j = new JsonObject().put("name", res.result().getString("name"))
							.put("username", res.result().getString("username"))
							.put("surname", res.result().getString("surname"))
							.put("email", res.result().getString("email"));
					r.response().setStatusCode(200).end(j.encodePrettily());
				} else {
					r.response().setStatusCode(404).end("El usuario no estÃ¡ registrado en la BD");
				}
			} else {
				r.response().setStatusCode(200).end("no");
			}
		});
	}

	public static void getEstadoESP8266(RoutingContext r, EventBus e, MongoClient mongoClient) {

		String identificadorPlaca = r.request().getParam("identificadorSerieId");

		JsonObject query = new JsonObject().put("identificador", identificadorPlaca);
		mongoClient.findOne("estadoesp8266", query, null, res -> {
			if (res.succeeded()) {
				if (res.result() != null) {
					r.response().setStatusCode(200).end(res.result().encodePrettily());
				} else {
					r.response().setStatusCode(404).end("El dispositivo no está registrado en la BD");
				}
			} else {
				r.response().setStatusCode(400).end("Error en la BD");
			}
		});
	}

	public static void deleteESP8266(RoutingContext r, EventBus e, MongoClient mongoClient) {

		String identificadorPlaca = r.request().getParam("identificadorSerieId");

		JsonObject query = new JsonObject().put("identificador", identificadorPlaca);
		mongoClient.removeDocuments("estadoesp8266", query, res -> {
			if (res.succeeded()) {
				if (res.result() != null) { // Quitar permisos de todos los usuarios que tuvieran este dispositivo asociado
					JsonObject query2 = new JsonObject().put("permiso", identificadorPlaca);
					JsonObject update2 = new JsonObject().put("$set", new JsonObject().put("permiso", ""));
					UpdateOptions options = new UpdateOptions().setMulti(true);
					mongoClient.updateCollectionWithOptions("usuario", query2, update2, options, res2 -> {
						if (res2.succeeded()) {
							r.response().setStatusCode(200)
									.end("Dispositivo eliminado con exito y permisos restaurados");
						} else {
							r.response().setStatusCode(400).end("Problema con la BD");
						}
					});

				} else {
					r.response().setStatusCode(404).end("No se ha podido eliminar.");
				}
			} else {
				r.response().setStatusCode(400).end(res.cause().toString());
			}
		});
	}

	public static void getDatosSensores(RoutingContext r, EventBus e, MongoClient mongoClient) {

		String identificadorPlaca = r.request().getParam("identificadorSerieId");
		JsonObject query = new JsonObject().put("identificadorSerie", identificadorPlaca);
		mongoClient.find("datosSensores", query, res -> {
			if (res.succeeded()) {
				if (res.result() != null) {
					String s = "[";
					for (JsonObject json : res.result()) {
						s += json + ",";
					}
					s = s.substring(0, s.length() - 1) + "]";
					System.out.println(s);
					r.response().setStatusCode(200).end(s);
				} else {
					r.response().setStatusCode(404).end("El dispositivo no está registrado en la BD");
				}
			} else {
				r.response().setStatusCode(200).end("Error en la BD");
			}
		});
	}

	public static void borrarDatosSensores(RoutingContext r, EventBus e, MongoClient mongoClient) {

		String identificadorPlaca = r.request().getParam("identificadorSerieId");
		JsonObject query = new JsonObject().put("identificadorSerie", identificadorPlaca);
		mongoClient.removeDocuments("datosSensores", query, res -> {
			if (res.succeeded()) {
				r.response().setStatusCode(200).end("Todos los datos de los sensores han sido borrados");
			} else {
				r.response().setStatusCode(404).end("Algo ha ido mal");
			}
		});
	}

	public static void encenderOApagar(RoutingContext r, Vertx vertx, MongoClient mongoClient) {

		String identificadorESP = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));

		mongoClient.findOne("estadoesp8266", new JsonObject().put("identificador", identificadorESP),
				new JsonObject().put("_id", false).put("gpio" + gpioValor + ".estado", true), res -> {
					if (res.succeeded()) {
						if (res.result() != null) {
							EstadoESP8266 e = Json.decodeValue(Json.encode(res.result()), EstadoESP8266.class);
							Gpio g = e.getGpio(gpioValor);
							if (g.isEstado() && (g.getNumero() >= 0 && g.getNumero() <= 7)) {
								mandarOrdenESP8266(identificadorESP, gpioValor, "OFF");
								r.response().setStatusCode(200).end(new JsonObject()
										.put("mensaje", "gpio" + gpioValor + " se va a apagar").encodePrettily());
							} else if (g.getNumero() >= 0 && g.getNumero() <= 7) {
								mandarOrdenESP8266(identificadorESP, gpioValor, "ON");
								r.response().setStatusCode(200).end(new JsonObject()
										.put("mensaje", "gpio" + gpioValor + " se va a encender").encodePrettily());
							}
						} else {
							r.response().setStatusCode(400).end(new JsonObject()
									.put("mensaje", "Error. Debido a un dispositivo no registrado").encodePrettily());
						}

					} else {
						r.response().setStatusCode(400).end(res.result().encodePrettily());
					}

				});
	}

	private static void mandarOrdenESP8266(String destinatario, Integer gpio, String mensaje) {
		
		MqttClient clienteMqtt = VerticleMqtt.getClientMqtt();
		String topic = destinatario + "/d" + gpio.toString();
		clienteMqtt.publish(topic, Buffer.buffer(mensaje), MqttQoS.AT_LEAST_ONCE, false, false);
		
	}

	public static void automatico(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		String automaticoValor = r.request().getParam("automaticoId");
		domotic.us.mongoDB.MetodosBD.guardarAutomaticoGpio(identificadorESP, gpioValor, automaticoValor, mongoClient, r, e);
	}

	public static void activado(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		String activacionValor = r.request().getParam("activacionId");
		domotic.us.mongoDB.MetodosBD.guardarActivadoGpio(identificadorESP, gpioValor, activacionValor, mongoClient, r, e);
	}

	public static void umbral(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		Double umbralValor = Double.parseDouble(r.request().getParam("umbralId"));
		System.out.println("Prueba umbral: " + identificadorESP + "," + gpioValor + "," + umbralValor);
		domotic.us.mongoDB.MetodosBD.guardarUmbralGpio(identificadorESP, gpioValor, umbralValor, mongoClient, r, e);
	}

	public static void nombre(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		String nombre = r.request().getParam("nombreId");
		System.out.println("Prueba texto: " + identificadorESP + "," + gpioValor + "," + nombre);
		domotic.us.mongoDB.MetodosBD.guardarTextoGpio(identificadorESP, gpioValor, nombre, mongoClient, r, e);
	}
	public static void pulsador1(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		String nombre = r.request().getParam("pulsador1Id");
		System.out.println("Prueba texto: " + identificadorESP + "," + nombre);
		domotic.us.mongoDB.MetodosBD.guardarTextoPulsador1(identificadorESP,  nombre, mongoClient, r, e);
	}
	public static void pulsador2(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		String nombre = r.request().getParam("pulsador2Id");
		System.out.println("Prueba texto: " + identificadorESP + "," + nombre);
		domotic.us.mongoDB.MetodosBD.guardarTextoPulsador2(identificadorESP,  nombre, mongoClient, r, e);
	}

	public static void sentido(RoutingContext r, Vertx vertx, MongoClient mongoClient, EventBus e) {
		String identificadorESP = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		String sentido = r.request().getParam("sentidoId");
		domotic.us.mongoDB.MetodosBD.guardarSentidoGpio(identificadorESP, gpioValor, sentido, mongoClient, r, e);
	}

	public static void addDatosSensores(RoutingContext routingContext, EventBus e, MongoClient mongoClient) {

		DatoSensor d = Json.decodeValue(routingContext.getBodyAsString(), DatoSensor.class);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		d.setYear(year);
		d.setMonth(month);
		d.setDay(day);

		tratamientoDato(d.getIdentificadorSerie(), d.getValor(), mongoClient);
		System.out.println("Publicado en: " + d.getIdentificadorSerie() + "/valor");
		e.publish(d.getIdentificadorSerie() + "/valor", d.getValor()); // WEBSOCKETTTTTT
		e.publish(d.getIdentificadorSerie() + "/pulsador1", d.getPulsador1());
		e.publish(d.getIdentificadorSerie() + "/pulsador2", d.getPulsador2());
		JsonObject jo = new JsonObject();
		try {
			jo = new JsonObject(new ObjectMapper().writeValueAsString(d));
		} catch (JsonProcessingException ex) {
			System.out.println("Error al convertir Json");
		}
		mongoClient.save("datosSensores", jo, res -> {
			if (res.succeeded()) {
				System.out.println("Guardado dato en BD: " + d.toString());
			} else {
				res.cause().printStackTrace();
			}
		});
	}

	public static void registrarESP8266(RoutingContext r, Vertx vertx, MongoClient mongoClient, MongoAuth auth) {

		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		TipoAnalogico tipoAnalogico = TipoAnalogico.valueOf(r.getBodyAsJson().getString("tipoAnalogico"));
		EstadoESP8266 e = new EstadoESP8266(identificadorSerieId, tipoAnalogico, "Texto alerta Pulsador1", "Texto alerta Pulsador2");

		JsonObject j = new JsonObject();
		JsonObject usuario = r.user().principal();
		try {
			j = new JsonObject(new ObjectMapper().writeValueAsString(e));
			j.put("pulsador1", "Texto alerta Pulsador1");
			j.put("pulsador2", "Texto alerta Pulsador2");
		} catch (JsonProcessingException ex) {
			System.out.println("Error al convertir Json");
		}

		if (!j.isEmpty()) {
			mongoClient.save("estadoesp8266", j, res -> { // Guardo en la BD un nuevo dispositivo
				if (res.succeeded()) {
					System.out.println(identificadorSerieId);
					mongoClient.updateCollection("usuario", new JsonObject().put("_id", usuario.getValue("_id")),
							new JsonObject().put("$set", new JsonObject().put("permiso", identificadorSerieId)),
							res2 -> {// Primer dispositivo registrado, el rol del usuario es propietario
								if (res2.succeeded()) {
									System.out.println(r.user().principal().toString());
									r.response().setStatusCode(200)
									.end(r.user().principal().put("permiso", identificadorSerieId)
														     .put("password", r.getBodyAsJson().getString("password"))
											.encodePrettily());
								} else {
									res2.cause().printStackTrace();
								}
							});
				} else {
					mongoClient.updateCollection("usuario", new JsonObject().put("_id", usuario.getValue("_id")),
							new JsonObject().put("$set",
									new JsonObject().put("rol", "copropietario").put("permiso", identificadorSerieId)),
							res2 -> {// No es el primero dispositivo registrado, el rol pasa a ser copropietario
								if (res2.succeeded()) {
									System.out.println("Book updated !");
									JsonObject respuesta = new JsonObject()
											.put("mensaje", "Nuevo dispositivo registrado correctamente")
											.put("permiso", identificadorSerieId);
									r.response().setStatusCode(200).end(respuesta.encodePrettily());
									System.out.println("Book updated !");
								} else {
									res2.cause().printStackTrace();
								}
							});
				}
			});
		} else {
			r.response().setStatusCode(404).end("Error");
		}
	}

	private static void tratamientoDato(String identificadorESP, String valorString, MongoClient mongoClient) {
		double valor = Double.parseDouble(valorString);

		mongoClient.findOne("estadoesp8266", new JsonObject().put("identificador", identificadorESP),
				new JsonObject().put("_id", false), res -> {
					if (res.succeeded()) {

						if (res.result() != null) {
							EstadoESP8266 e = Json.decodeValue(Json.encode(res.result()), EstadoESP8266.class);
							for (int i = 1; i <= 7; i++) {
								Gpio gpio = e.getGpio(i);
								if (gpio.isAutomatico()) {
									Boolean estado = gpio.isEstado();
									Boolean sentido = gpio.isSentido();
									double umbral = gpio.getUmbral();
									if (sentido) {
										if (valor < umbral && estado == true) {// apagar si estuviese encendido
											mandarOrdenESP8266(identificadorESP, i, "OFF");
										} else if (valor >= umbral && estado == false) {
											mandarOrdenESP8266(identificadorESP, i, "ON");
										}
									} else {
										if (valor < umbral && estado == false) {// encender si estuviese apagado
											mandarOrdenESP8266(identificadorESP, i, "ON");
										} else if (valor >= umbral && estado == true) {
											mandarOrdenESP8266(identificadorESP, i, "OFF");
										}
									}
								}
							}
						} else {
						}
					}
				});
	}

}
