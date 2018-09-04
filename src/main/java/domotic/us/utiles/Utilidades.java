package domotic.us.utiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilidades {
	private static int numeroDeDispositivosTotales = 10; // 10 dispositivos de momento
	
	// Desde BA001 a BA010
	public static ArrayList<String> getDispositivos() {
		ArrayList<String> l = new ArrayList<>();
		for (int i = 1; i <= numeroDeDispositivosTotales; i++) {
			l.add("BA00" + i);
		}
		return new ArrayList<String>(l);
	}
	// Pares identificador de dispositivo, contraseña. Todos tienen contraseña 123 por ahora
	public static Map<String, String> getListaDispositivosConPasswords() {
		Map<String, String> mapa = new HashMap<>();
		for (String s : getDispositivos()) {
			mapa.put(s, "123");
		}
		return new HashMap<String, String>(mapa);
	}
	// Desde d0_ack hasta d7_ack.
	private static ArrayList<String> getTopicsMqttDeCadaDispositivo() {
		ArrayList<String> l = new ArrayList<>();
		for (int i = 1; i <= 7; i++) {
			l.add("/d" + String.valueOf(i) + "_ack");
		}
		return l;
	}
	// 11 topics por cada dispositivo para los websockets
	private static ArrayList<String> getTopicsWSDeCadaDispositivo() {
		ArrayList<String> l = new ArrayList<>();
		l.add("/valor");
		l.add("/pulsador1");
		l.add("/pulsador2");
		l.add("/cambioConfiguracion");
		for (int i = 1; i <= 7; i++) {
			//l.add("/" + String.valueOf(i) + "/nombre");
			//l.add("/" + String.valueOf(i) + "/activado");
			l.add("/" + String.valueOf(i) + "/estado");
			//l.add("/" + String.valueOf(i) + "/automatico");
			//l.add("/" + String.valueOf(i) + "/umbral");
			//l.add("/" + String.valueOf(i) + "/sentido");
		}
		return l;
	}
	// Lista final de todos los topics a los que hay que suscribirse para recibir confirmaciones desde
	// nuestro dispositivo
	public static Map<String, Integer> getTodosLosTopicsDeTodosLosIdentificadores() {
		List<String> lista1 = getDispositivos();
		List<String> lista2 = getTopicsMqttDeCadaDispositivo();
		List<String> lista3 = new ArrayList<>();
		Map<String, Integer> mapa = new HashMap<>();

		for (String i : lista1) { // TODO Pasar a Java8
			for (String j : lista2) {
				lista3.add(i + j);
			}
		}

		mapa = new HashMap<>();
		for (String s : lista3) { // TODO Pasar a Java8
			mapa.put(s, 1);
		}

		return new HashMap<String, Integer>(mapa);
	}
	// Lista final de los topics para los websockets
	public static List<String> getTopicsWSDeTodosLosIdentificadores() {
		List<String> lista1 = getDispositivos();
		List<String> lista2 = getTopicsWSDeCadaDispositivo();
		List<String> listaAux = new ArrayList<>();

		for (String i : lista1) {
			for (String j : lista2) {
				listaAux.add(i + j);
			}
		}
		System.out.println(listaAux);
		return new ArrayList<String>(listaAux);
	}
}
