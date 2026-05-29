package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttProducer implements MqttCallback {

    private static final String TOPIC_IN = "IR/in";
    private static final String BROKER_URI = "tcp://localhost:1883";

    private MqttClient client;
    private final Gson gson = new Gson();

    private static class AccessRequest {


        AccessRequest(String type, String valeur) {
        }
    }
    public static void main(String[] args) {
        MqttProducer producer = new MqttProducer();
        producer.envoyerlaPlaque("WW-238-PG");
        producer.envoyerleBadge ("8310F2AA");
        producer.envoyerlaPlaque("AH-722-YK");
        producer.envoyerleBadge("736188AB");
        producer.envoyerleBadge("7307D8AA");
        producer.envoyerleBadge("F3C790AB");
    }

    public void envoyerlaPlaque(String plaque) {
        publier(buildJson("plaque", plaque));
    }

    public void envoyerleBadge(String badge) {
        publier(buildJson("badge", badge));
    }

    private String buildJson(String type, String valeur) {
        AccessRequest req = new AccessRequest(type, valeur);
        return gson.toJson(req);
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
            System.out.println("Envoi sur [" + TOPIC_IN + "] : " + json);
            client.publish(TOPIC_IN, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getClient() {
        return client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
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
        System.out.println("Delivery complete.");
    }
}
