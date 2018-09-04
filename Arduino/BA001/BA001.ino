#include <Arduino.h>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <AsyncMqttClient.h>

// Datos conexión wifi///////////////////////////////////////////////////////////////////

const char* ssid = "MIWIFI_2G_vCHj";
const char* password = "PECwVSsH";

// Datos únicos para cada dispositivo NODEMCU ESP-12E/////////////////////////////////////////

const char* IDENTIFICADORSERIE = "BA001";
const char* RUTAGETTIPOSENSOR = "http://52.136.253.127:8080/api/esp/esp8266/tipo/BA001";
const char* RUTAPUTDATOANALOGICO = "http://52.136.253.127:8080/api/datosSensores";

#define D0_ESP "BA001/d1"
#define D1_ESP "BA001/d2"
#define D2_ESP "BA001/d3"
#define D3_ESP "BA001/d4"
#define D4_ESP "BA001/d5"
#define D5_ESP "BA001/d6"
#define D6_ESP "BA001/d7"

#define ANALOGICO "BA001/analogico"

#define D0_ESP_ACK "BA001/d1_ack"
#define D1_ESP_ACK "BA001/d2_ack"
#define D2_ESP_ACK "BA001/d3_ack"
#define D3_ESP_ACK "BA001/d4_ack"
#define D4_ESP_ACK "BA001/d5_ack"
#define D5_ESP_ACK "BA001/d6_ack"
#define D6_ESP_ACK "BA001/d7_ack"

// Atención, se debe cambiar la línea 312 si se cambia dirección del Broker Mosquitto en Azure

///////////////////////////////////////////////////////////////////////////////////

// Definicion de PINES

#define PIN_D0 16 
#define PIN_D1 5  
#define PIN_D2 4  
#define PIN_D3 0
#define PIN_D4 2
#define PIN_D5 14 
#define PIN_D6 12 
#define PIN_D7 13 
#define PIN_D8 15 


AsyncMqttClient mqttClient;

// Variables usadas en el loop

unsigned long milisegundosPasados = 0;
const long intervalo = 6000; // 6 segundos

void onMqttConnect(bool sessionPresent) {
  Serial.println("------ Conectado al Broker");

  mqttClient.subscribe(D0_ESP, 1);
  mqttClient.subscribe(D1_ESP, 1);
  mqttClient.subscribe(D2_ESP, 1);
  mqttClient.subscribe(D3_ESP, 1);
  mqttClient.subscribe(D4_ESP, 1);
  mqttClient.subscribe(D5_ESP, 1);
  mqttClient.subscribe(D6_ESP, 1);
}

void onMqttDisconnect(AsyncMqttClientDisconnectReason reason) {
  Serial.println("------ Desconectado del Broker");
  Serial.println("------ Reconectando al Broker");
  mqttClient.connect();
}

void onMqttSubscribe(uint16_t packetId, uint8_t qos) {
  Serial.print("------ Subscripcion registrada");
  Serial.print(" packetId: ");
  Serial.print(packetId);
  Serial.print(" qos: ");
  Serial.println(qos);
}

void onMqttUnsubscribe(uint16_t packetId) {
  Serial.println("------ Cancelación subscripción registrada");
  Serial.print("  packetId: ");
  Serial.println(packetId);
}

void onMqttMessage(char* topic, char* payload, AsyncMqttClientMessageProperties properties, size_t len, size_t index, size_t total) {
  Serial.println("------ Mensaje recibido");
  Serial.print("  topico: ");
  Serial.print(topic);
  Serial.print("  qos: ");
  Serial.print(properties.qos);
  Serial.print("  dup: ");
  Serial.print(properties.dup);
  Serial.print("  retain: ");
  Serial.print(properties.retain);
  Serial.print("  len: ");
  Serial.print(len);
  Serial.print("  index: ");
  Serial.print(index);
  Serial.print("  payload: ");
  Serial.println(payload);

  // Acciones sobre los actuadores.

  if (strcmp(topic, D0_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D0, HIGH);
      mqttClient.publish(D0_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D0, LOW);
      mqttClient.publish(D0_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   } else if (strcmp(topic, D1_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D1, HIGH);
      mqttClient.publish(D1_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D1, LOW);
      mqttClient.publish(D1_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   } else if (strcmp(topic, D2_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D2, HIGH);
      mqttClient.publish(D2_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D2, LOW);
      mqttClient.publish(D2_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   }else if (strcmp(topic, D3_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D3, HIGH);
      mqttClient.publish(D3_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D3, LOW);
      mqttClient.publish(D3_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   }else if (strcmp(topic, D4_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D4, HIGH);
      mqttClient.publish(D4_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D4, LOW);
      mqttClient.publish(D4_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   }else if (strcmp(topic, D5_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D5, HIGH);
      mqttClient.publish(D5_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D5, LOW);
      mqttClient.publish(D5_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   }else if (strcmp(topic, D6_ESP) == 0)  {
    String s = payload;
    if(s.substring(0,2) == "ON"){
      digitalWrite(PIN_D6, HIGH);
      mqttClient.publish(D6_ESP_ACK, 1, false, "1");  // Envio ACK del topic
    }else if(s.substring(0,3) == "OFF"){
      digitalWrite(PIN_D6, LOW);
      mqttClient.publish(D6_ESP_ACK, 1, false, "0");  // Envio ACK del topic
    }
   }
 }

void onMqttPublish(uint16_t packetId) {
  Serial.print("** Paquete publicado");
}

String leerSensor(){
 int lecturaSensor = analogRead(A0); // Voltaje del sensor
 String l = String(lecturaSensor);
 return l;
}

String leerTemperatura(){                   
  int lecturaSensor1 = analogRead(A0);      
  int lecturaSensor2 = analogRead(A0);      
  int lecturaSensor3 = analogRead(A0);
  int lecturaSensor4 = analogRead(A0);
  int lecturaSensor5 = analogRead(A0);
  int lecturaSensor6 = analogRead(A0);
  float lecturaSensorMedia = (lecturaSensor1+lecturaSensor2+lecturaSensor3+lecturaSensor4+lecturaSensor5+lecturaSensor6)/6.0;
  float f = (((lecturaSensorMedia-100)*(5.0/1024.0))*100.0)-50.0;
  return String(f);
}

String leerLuminosidad(){
  const long A = 1000;     //Resistencia en oscuridad en KΩ
  const int B = 15;        //Resistencia a la luz (10 Lux) en KΩ
  const int Rc = 10;       //Resistencia calibracion en KΩ
  int V;
  int lecturaSensor1 = analogRead(A0);
  int lecturaSensor2 = analogRead(A0);
  int lecturaSensor3= analogRead(A0);
  int lecturaSensor4 = analogRead(A0);
  int lecturaSensor5 = analogRead(A0);
  int lecturaSensor6 = analogRead(A0);
  V = (lecturaSensor1+lecturaSensor2+lecturaSensor3+lecturaSensor4+lecturaSensor5+lecturaSensor6)/6;
  int ilum = ((long)V*A)/((long)B*Rc*(1024-V));
  return String(ilum);
}

void funcionP(){
   if (WiFi.status() == WL_CONNECTED) { // Comprueba el estado de la conexion WIFI
    HTTPClient http;  // Declara objeto de la clase HTTPClient
    HTTPClient http2;
    StaticJsonBuffer<200> jsonBuffer;
    
    http.begin(RUTAGETTIPOSENSOR); // Especifica el destino de la peticion
    
    int httpCode2 = http.GET();                                  // Enviar la peticion
    JsonObject& root = jsonBuffer.parseObject(http.getString()); // Obtengo el json de la respuesta del servidor, 
                                                                  // donde solo habrá un campo tipoSensor con 3 valores 
                                                                  // posibles: TEMPERATURA, HUMEDAD O LUMINOSIDAD

    
      if(!root.success()){
        Serial.println("Fallo al parsear o Servidor no está funcionando");
        return;
      }
    

    http.end();   // Cerramos http
    
    const char* sensor = root["tipoAnalogico"];

    Serial.print(sensor);
    const char* temperatura = "Temperatura";
    const char* luminosidad = "Luminosidad";
    

    String body = "{\"identificadorSerie\":\"";
    body += IDENTIFICADORSERIE;
    body += "\",\"tipoAnalogico\":";
    body += "\"";
    body += sensor;
    body += "\",\"pulsador1\":";
    body += "\"";
    body += digitalRead(PIN_D7);
    body += "\",\"pulsador2\":";
    body += "\"";
    body += digitalRead(PIN_D8);
    body+= "\",\"valor\":";
    
    if(strcmp(sensor,temperatura)==0){
      body+= leerTemperatura();
    }else if(strcmp(sensor,luminosidad)==0){
      body+=leerLuminosidad();
    }else{
      body+=leerSensor();
    }
    body+= "}\r\n";

    http2.begin(RUTAPUTDATOANALOGICO); // Especifica el destino de la peticion
    
    int http2Code = http2.PUT(body);  // Manda peticion
 
    if (http2Code > 0) { // Comprueba el código devuelto
      String payload = http2.getString();            //Obtiene la respuesta
      Serial.println(payload);                     // Imprime la respuesta
    }
 
    http2.end();   // Cerramos
 
  }
}

void setup() {
 
  Serial.begin(115200);         // Solución a un error que me daba del reseteo del watchdog "rst cause:4, boot mode:(1,6)   wdt reset"
  Serial.println("");           // que me dejaba el dispositivo bloqueado y que solo solucionaba reseteando manualmente
  ESP.wdtDisable();             // Añadiendo estas dos líneas en el setup y "ESP.wdtFeed()" en el loop se soluciona de  
  ESP.wdtEnable(WDTO_8S);       // momento al cambiar la configuracion del watchdog timer
  // Configurar pines
  pinMode(PIN_D0, OUTPUT);
  pinMode(PIN_D1, OUTPUT);
  pinMode(PIN_D2, OUTPUT);
  pinMode(PIN_D3, OUTPUT);
  pinMode(PIN_D4, OUTPUT);
  pinMode(PIN_D5, OUTPUT); 
  pinMode(PIN_D6, OUTPUT); 
  pinMode(PIN_D7, INPUT); 
  pinMode(PIN_D8, INPUT); 
  
  digitalWrite(PIN_D0, LOW);
  digitalWrite(PIN_D1, LOW);
  digitalWrite(PIN_D2, LOW);
  digitalWrite(PIN_D3, LOW);
  digitalWrite(PIN_D4, LOW);
  digitalWrite(PIN_D5, LOW);
  digitalWrite(PIN_D6, LOW);

  WiFi.persistent(false);

  WiFi.mode(WIFI_STA);
  Serial.print("Conectando a la red WIFI");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println(".");
  }

  Serial.println("OK");

  mqttClient.onConnect(onMqttConnect);       // Conectar con el Broker
  mqttClient.onDisconnect(onMqttDisconnect); 
  mqttClient.onMessage(onMqttMessage);
  mqttClient.onPublish(onMqttPublish);
  //mqttClient.setServer(IPAddress(52, 136, 253, 127), 1883); // IP Broker Mosquitto en domoticfox.cloudapp.net, es 52.136.253.127
  mqttClient.setServer("domoticfox.cloudapp.net", 1883);
  mqttClient.setKeepAlive(2).setCleanSession(true).setWill("fallo", 1, false, "Fallo de Hardware").setClientId(IDENTIFICADORSERIE);
  Serial.println("Conectando al servidor MQTT...");
  mqttClient.connect();
}

void loop() {

  unsigned long milisegundos = millis();                // Cada 6 segundos se ejecuta función que: pide al servidor el tipo de dato analogico que debe mandar, dependiendo de la respuesta
                                                        // manda json etiquetando el tipo de sensor y los valores digitales (0 o 1) de la lectura de 2 pulsadores
  if(milisegundos - milisegundosPasados >= intervalo){
    milisegundosPasados = milisegundos;
    funcionP();
  }
ESP.wdtFeed();
}

