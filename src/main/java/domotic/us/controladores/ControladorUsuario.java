package domotic.us.controladores;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class ControladorUsuario {
	
	public static void registrarUsuario(RoutingContext r, Vertx vertx, MongoClient mongoClient, MongoAuth auth) {
		String name = r.getBodyAsJson().getString("name");
		String username = r.getBodyAsJson().getString("username");
		String password = r.getBodyAsJson().getString("password");
		String surname = r.getBodyAsJson().getString("surname");
		String email = r.getBodyAsJson().getString("email");
		String rol = r.getBodyAsJson().getString("rol");
		if(name != null && username != null && password != null && surname != null &&
				email != null && rol != null && !rol.isEmpty()) {
			List<String> roles = new ArrayList<>();
			roles.add(rol);
			List<String> permisos = new ArrayList<>();
			auth.insertUser(username,password, roles, permisos, res -> {
				if(res.succeeded()) {
					JsonObject query = new JsonObject().put("_id", res.result());
					JsonObject update2 = new JsonObject()
								.put("name", name)
								.put("surname", surname)
								.put("email", email)
								.put("contrasena", password)
								.put("permiso", "")
								.put("rol", rol);
				    JsonObject update = new JsonObject().put("$set", update2);
					mongoClient.updateCollection("usuario", query, update, res2 -> {
						if(res2.succeeded()) {
							r.response()
								.putHeader("content-type", "application/json; charset=utf-8")
								.setStatusCode(200)
								.end(Json.encodePrettily(new JsonObject().put("_id", res.result())));
						}else {
							r.response().setStatusCode(404).end();
						}
					});
				}else {
					
					r.response().setStatusCode(404).end("No se puede registrar al usuario. Ya existe en la BD");
				}
			});
		}else {
			r.response().setStatusCode(404).end("Rellena todos los campos");
		}
	}

	public static void loginUser(RoutingContext r, Vertx vertx, MongoClient mongoClient, MongoAuth auth) {
		
		
		String username = r.getBodyAsJson().getString("username");
		String password = r.getBodyAsJson().getString("password");
		System.out.println(username);
		//String salt;
		
		if(username != null && password!= null) {
		JsonObject parUsernamePassword = new JsonObject()
			    .put("username", username)
			    .put("password", password);
			auth.authenticate(parUsernamePassword, res -> {
				JsonObject jsonConDatosUserYUsuario = new JsonObject();
			  if (res.succeeded()) {
				  jsonConDatosUserYUsuario.put("salt", res.result().principal().getValue("salt"))
				  .put("roles", res.result().principal().getValue("roles"))
				  .put("permissions", res.result().principal().getValue("permissions"));
			    	mongoClient.findOne("usuario", new JsonObject().put("username", username), null,res2 -> {
			            if (res2.succeeded()) {
			            	if(res2.result() != null) {
			            		jsonConDatosUserYUsuario.put("name", res2.result().getString("name"))
			            			.put("username", res2.result().getString("username"))
			            			.put("surname", res2.result().getString("surname"))
			            			.put("email", res2.result().getString("email"))
			            			.put("_id", res.result().principal().getValue("_id"))
			            			.put("password", password)
			            			.put("rol", res.result().principal().getValue("rol"))
			            			.put("permiso",res.result().principal().getValue("permiso"));
			            		System.out.println(jsonConDatosUserYUsuario.encodePrettily());
			            	r.response().setStatusCode(200).end(jsonConDatosUserYUsuario.encodePrettily());
			            	}else {
			            		r.response().setStatusCode(404).end("El usuario no está registrado en la BD");
			            	}
			              } else {
			            	  r.response().setStatusCode(200).end("no");
			              }});

			  } else {
				  r.response().setStatusCode(404).end();
			  }
			});	
		}else {
			r.response().setStatusCode(404).end("El username o contraseña no pueden ser vacías");
		}
	}
    	
    	public static void actualizarUsuario(RoutingContext r, EventBus e, MongoClient mongoClient) {
    		String _id = r.request().getParam("elementId");
    		String name = r.getBodyAsJson().getString("name");
    		String surname = r.getBodyAsJson().getString("surname");
    		String email = r.getBodyAsJson().getString("email");
    		JsonObject query = new JsonObject().put("_id", _id);
    		JsonObject updateAuxiliar = new JsonObject().put("name", name)
    													.put("surname", surname )
    													.put("email", email );
    		JsonObject update = new JsonObject().put("$set", updateAuxiliar);
    		if(_id != null && name != null && surname != null && email != null) {
    		mongoClient.updateCollection("usuario", query, update, res -> {
    			if(res.succeeded()) {
    				r.response().setStatusCode(500).end((new JsonObject().put("estado", "exito")).encodePrettily());
    			}else {
    				r.response().setStatusCode(400).end((new JsonObject().put("mensaje", "Error al actualizar usuario")).encodePrettily());
    			}
    		});
    		}else {
    			r.response().setStatusCode(400).end((new JsonObject().put("mensaje", "Error al actualizar usuario")).encodePrettily());
    		}
    	}
    	
    	public static void borrarUsuario(RoutingContext r, EventBus e, MongoClient mongoClient) {
    		String _id = r.request().getParam("elementId");
    		JsonObject query = new JsonObject().put("_id", _id);
    		if(_id != null) {
    		mongoClient.removeDocuments("usuario", query, res -> {
    			if(res.succeeded()) {
    				r.response().setStatusCode(500).end((new JsonObject().put("estado", "exito")).encodePrettily());
    			}else {
    				r.response().setStatusCode(400).end((new JsonObject().put("mensaje", "Error al borrar usuario")).encodePrettily());
    			}
    		});
    		}else {
    			r.response().setStatusCode(400).end((new JsonObject().put("mensaje", "Error al borrar usuario")).encodePrettily());
    		}
    	}
    	
}
