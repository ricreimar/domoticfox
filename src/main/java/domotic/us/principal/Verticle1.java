package domotic.us.principal;

import java.util.HashSet;
import java.util.Set;

import io.vertx.ext.auth.mongo.HashAlgorithm;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.auth.mongo.impl.MongoAuthImpl;
import io.vertx.ext.mongo.MongoClient;
import domotic.us.webSocket.WebSockets;
import domotic.us.controladores.ControladorEstadoESP8266;
import domotic.us.controladores.ControladorUsuario;
import domotic.us.utiles.Configuracion;
import domotic.us.utiles.Utilidades;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.json.JsonObject;

public class Verticle1 extends AbstractVerticle {

	private static EventBus e;
	public static MongoClient mongoClient;
	private static MongoAuth authProvider;

	public void start() {
		vertx = getVertx();
		e = vertx.eventBus();
		JsonObject js = new JsonObject();
		mongoClient = MongoClient.createShared(vertx, js);
		JsonObject authProperties = new JsonObject();
		authProvider = new MongoAuthImpl(mongoClient, authProperties);
		authProvider.setHashAlgorithm(HashAlgorithm.PBKDF2).setCollectionName("usuario");

		Router router = Router.router(getVertx());
		Router router2 = Router.router(getVertx());
		Router router3 = Router.router(getVertx());

		Set<String> allowedHeaders = new HashSet<>();
		allowedHeaders.add("x-requested-with");
		allowedHeaders.add("Access-Control-Allow-Origin");
		allowedHeaders.add("origin");
		allowedHeaders.add("Content-Type");
		//allowedHeaders.add("accept");
		//allowedHeaders.add("X-PINGARUNER");

		Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.DELETE);
		allowedMethods.add(HttpMethod.PUT);

		router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

		vertx.createHttpServer().requestHandler(router::accept).listen(Configuracion.puertoAPIREST);
		vertx.createHttpServer().requestHandler(router2::accept).listen(Configuracion.puertoWebSocket);
		vertx.createHttpServer().requestHandler(router3::accept).listen(80);

		router2.route("/eventbus/*").handler(WebSockets.eventBusHandler(vertx.eventBus(), vertx));

		router.route("/publico/*").handler(BodyHandler.create());
		router.post("/publico/:identificadorSerieId").handler(this::registrarESP8266);

		router.route("/api/esp/esp8266/*").handler(BodyHandler.create());
		router.route("/api/esp/accion/*").handler(BodyHandler.create());
		router.route("/api/datosSensores/*").handler(BodyHandler.create());
		router.route("/api/usuario/*").handler(BodyHandler.create());
		router.route("/api/familia/*").handler(BodyHandler.create());
		
		router.get("/api/esp/esp8266/:identificadorSerieId").handler(this::getEstadoESP8266);
		router.post("/api/esp/esp8266/:identificadorSerieId").handler(this::registrarESP8266);
		router.post("/api/esp/esp8266/eliminar/:identificadorSerieId").handler(this::deleteESP8266);
		router.get("/api/esp/esp8266/tipo/:identificadorSerieId").handler(this::getTipoAnalogicoDePlaca); 
		
		router.post("/api/esp/accion/estado/:identificadorSerieId/:gpioId").handler(this::encenderOApagar); 												
		router.post("/api/esp/accion/nombre/:identificadorSerieId/:gpioId/:nombreId").handler(this::nombre);
		router.post("/api/esp/accion/pulsador1/:identificadorSerieId/:pulsador1Id").handler(this::pulsador1);
		router.post("/api/esp/accion/pulsador2/:identificadorSerieId/:pulsador2Id").handler(this::pulsador2);
		router.post("/api/esp/accion/activado/:identificadorSerieId/:gpioId/:activacionId").handler(this::activado); 																	
		router.post("/api/esp/accion/automatico/:identificadorSerieId/:gpioId/:automaticoId").handler(this::automatico); 																				
		router.post("/api/esp/accion/umbral/:identificadorSerieId/:gpioId/:umbralId").handler(this::umbral); 																
		router.post("/api/esp/accion/sentido/:identificadorSerieId/:gpioId/:sentidoId").handler(this::sentido); 

		router.put("/api/datosSensores/").handler(this::addDatosSensores); 
		router.post("/api/datosSensores/:identificadorSerieId").handler(this::getDatosSensores);
		router.post("/api/datosSensores/borrar/:identificadorSerieId").handler(this::borrarDatosSensores);

		router.post("/api/usuario/register").handler(this::registrarUsuario);
		router.post("/api/usuario/login").handler(this::logearUsuario);
		router.put("/api/usuario/:elementid").handler(this::actualizarUsuario);
		router.delete("/api/usuario/:elementid").handler(this::borrarUsuario);
		
		router3.route().handler(StaticHandler.create());

	}
	

	private void registrarUsuario(RoutingContext routingContext) {
		ControladorUsuario.registrarUsuario(routingContext, vertx, mongoClient, authProvider);
	}

	private void actualizarUsuario(RoutingContext r) {
		authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
				.put("password", r.getBodyAsJson().getString("password")), res -> {
					if (res.succeeded()) {
						ControladorUsuario.actualizarUsuario(r, e, mongoClient);
					} else {
						r.response().setStatusCode(404).end("Usuario no autenticado");
					}
				});
	}

	private void borrarUsuario(RoutingContext r) {

		authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
				.put("password", r.getBodyAsJson().getString("password")), res -> {
					if (res.succeeded()) {
						ControladorUsuario.borrarUsuario(r, e, mongoClient);
					} else {
						r.response().setStatusCode(404).end("Usuario no autenticado");
					}
				});
	}

	private void logearUsuario(RoutingContext routingContext) {
		ControladorUsuario.loginUser(routingContext, vertx, mongoClient, authProvider);
	}

	private void getTipoAnalogicoDePlaca(RoutingContext routingContext) {
		ControladorEstadoESP8266.getTipoAnalogicoDePlaca(routingContext, e, mongoClient);
	}

	private void getEstadoESP8266(RoutingContext routingContext) {
		System.out.println("llega getEstado");
		ControladorEstadoESP8266.getEstadoESP8266(routingContext, e, mongoClient);
	}

	private void deleteESP8266(RoutingContext r) {
		System.out.println("Usuario: "+ r.getBodyAsJson().getString("username"));
		System.out.println("Contraseña: "+ r.getBodyAsJson().getString("password"));
		
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		  authProvider.authenticate(new JsonObject().put("username",
		  r.getBodyAsJson().getString("username")) .put("password",
		  r.getBodyAsJson().getString("password")), res ->{ if(res.succeeded()) {
		  JsonObject json = res.result().principal(); if(json.getValue("permiso") !=
		  null && json.getValue("permiso").equals(identificadorSerieId)) {
		  System.out.println("Tiene Permiso");
		  ControladorEstadoESP8266.deleteESP8266(r,e,mongoClient); }else {
		  r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
		  } }else { r.response().setStatusCode(404).end("Usuario no autenticado"); }
		  });

	}

	private void getDatosSensores(RoutingContext r) {
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
				.put("password", r.getBodyAsJson().getString("password")), res -> {
					if (res.succeeded()) {
						JsonObject json = res.result().principal();
						if (json.getValue("permiso") != null && json.getValue("permiso").equals(identificadorSerieId)) {
							System.out.println("Tiene Permiso");
							ControladorEstadoESP8266.getDatosSensores(r, e, mongoClient);
						} else {
							r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
						}
					} else {
						r.response().setStatusCode(404).end("Usuario no autenticado");
					}
				});
	}
	
	private void borrarDatosSensores(RoutingContext r) {
		System.out.println("llega");
		
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
				.put("password", r.getBodyAsJson().getString("password")), res -> {
					if (res.succeeded()) {
						JsonObject json = res.result().principal();
						if (json.getValue("permiso") != null && json.getValue("permiso").equals(identificadorSerieId)) {
							System.out.println("Tiene Permiso");
							ControladorEstadoESP8266.borrarDatosSensores(r, e, mongoClient);
						} else {
							r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
						}
					} else {
						r.response().setStatusCode(404).end("Usuario no autenticado");
					}
				});
	}

	private void registrarESP8266(RoutingContext r) {

		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		String passwordPlaca = r.getBodyAsJson().getString("passwordPlaca");

		authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
				.put("password", r.getBodyAsJson().getString("password")), res -> {
					if (res.succeeded()) {
						r.setUser(res.result());
						JsonObject jsonUsuario = res.result().principal();
						System.out.println(jsonUsuario);
						if (Utilidades.getListaDispositivosConPasswords().containsKey(identificadorSerieId)) {
							String contrasenaMap = Utilidades.getListaDispositivosConPasswords()
									.get(identificadorSerieId);
							if (passwordPlaca.equals(contrasenaMap)) {
								ControladorEstadoESP8266.registrarESP8266(r, vertx, mongoClient, authProvider);
							} else {

								r.response().setStatusCode(401).end(new JsonObject()
										.put("mensaje", "Constraseña de dispositivo no valida").encodePrettily());
							}
						} else {
							r.response().setStatusCode(402).end(new JsonObject()
									.put("mensaje", "Numero de serie de dispositivo incorrecto").encodePrettily());
						}
					} else {
						r.response().setStatusCode(404).end("Usuario no autenticado");
					}
				});
	}

	private void automatico(RoutingContext r) {
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		System.out.println(identificadorSerieId);
		if (gpioValor >= 1 && gpioValor <= 7) {
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.automatico(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
		} else {
			r.response().setStatusCode(404).end("GPIO no valido");
		}
	}

	private void activado(RoutingContext r) {

		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		System.out.println(identificadorSerieId);
		if (gpioValor >= 1 && gpioValor <= 7) {
			System.out.println(r.getBodyAsJson().getString("username"));
			System.out.println(r.getBodyAsJson().getString("password"));
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.activado(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							System.out.println("No autenticado");
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
		} else {
			r.response().setStatusCode(404).end("GPIO no valido");
		}

	}

	private void umbral(RoutingContext r) {
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		System.out.println(identificadorSerieId);
		if (gpioValor >= 1 && gpioValor <= 7) {
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.umbral(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
		} else {
			r.response().setStatusCode(404).end("GPIO no valido");
		}

	}

	private void nombre(RoutingContext r) {
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		System.out.println(identificadorSerieId);
		if (gpioValor >= 1 && gpioValor <= 7) {
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.nombre(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
		} else {
			r.response().setStatusCode(404).end("GPIO no valido");
		}

	}
	private void pulsador1(RoutingContext r) {
		
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		String pulsador1Id = r.request().getParam("pulsador1Id");
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								System.out.println("llega p1");
								ControladorEstadoESP8266.pulsador1(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
	}
	
	private void pulsador2(RoutingContext r) {
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.pulsador2(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
	}

	private void sentido(RoutingContext r) {
		System.out.println("Cambio configuracionnnnn");
		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		if (gpioValor >= 1 && gpioValor <= 7) {
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.sentido(r, vertx, mongoClient, e);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
		} else {
			r.response().setStatusCode(404).end("GPIO no valido");
		}

	}

	private void encenderOApagar(RoutingContext r) {

		String identificadorSerieId = r.request().getParam("identificadorSerieId");
		Integer gpioValor = Integer.parseInt(r.request().getParam("gpioId"));
		System.out.println(identificadorSerieId);
		if (gpioValor >= 1 && gpioValor <= 7) {
			authProvider.authenticate(new JsonObject().put("username", r.getBodyAsJson().getString("username"))
					.put("password", r.getBodyAsJson().getString("password")), res -> {
						if (res.succeeded()) {
							JsonObject json = res.result().principal();
							System.out.println(json.getValue("permiso"));
							if (json.getValue("permiso") != null
									&& json.getValue("permiso").equals(identificadorSerieId)) {
								System.out.println("Tiene Permiso");
								ControladorEstadoESP8266.encenderOApagar(r, vertx, mongoClient);
							} else {
								r.response().setStatusCode(404).end("Usuario autenticado, pero sin permiso");
							}
						} else {
							r.response().setStatusCode(404).end("Usuario no autenticado");
						}
					});
		} else {
			r.response().setStatusCode(404).end("GPIO no valido");
		}

	}

	private void addDatosSensores(RoutingContext routingContext) {
		ControladorEstadoESP8266.addDatosSensores(routingContext, e, mongoClient);

	}

}