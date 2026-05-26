package lml.snir.parkinglogickit.metier.mqtt;



import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.UUID;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import org.eclipse.paho.client.mqttv3.MqttException;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;


public class MqttProducer implements MqttCallback {
    
      
    private static final String TOPIC_IN  = "IR/in";
    private static final String BROKER_URI = "tcp://localhost:1883";
    private MqttClient client;

   
    public static void main(String[] args) {
        MqttProducer producer = new MqttProducer();

//     c'est des fausse plaque ect...
        producer.envoyerPlaque("TT-458-CC");     
        producer.envoyerRFID("RFID-001A");       
        producer.envoyerBadge("BADGE-DUPONT");   
        producer.envoyerPlaque("XX-000-ZZ");    
        producer.envoyerRFID("RFID-INCONNU");   
        producer.envoyerBadge("BADGE-INCONNU"); 
    }

    public void envoyerPlaque(String plaque) {
        String json = buildJson("plaque", plaque);
        publier(json);
    }

    public void envoyerRFID(String rfid) {
        String json = buildJson("rfid", rfid);
        publier(json);
    }

    public void envoyerBadge(String badge) {
        String json = buildJson("badge", badge);
        publier(json);
    }

 
    private String buildJson(String type, String valeur) {
        return String.format("{ \"type\": \"%s\", \"valeur\": \"%s\" }", type, valeur);
    }

  
    private void publier(String json) {
        try {
            String clientID = "producer-" + UUID.randomUUID();
            MemoryPersistence persistence = new MemoryPersistence();

            client = new MqttClient(BROKER_URI, clientID, persistence);
            client.setCallback(this);
            client.connect();

            MqttMessage message = new MqttMessage(json.getBytes());
            message.setQos(1);

            System.out.println(" Envoi sur [" + TOPIC_IN + "] : " + json);
            client.publish(MetierFactory.getTopic(), message);

            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

  
    @Override
    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("[" + topic + "] " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println(" Delivery complete.");
    }
}