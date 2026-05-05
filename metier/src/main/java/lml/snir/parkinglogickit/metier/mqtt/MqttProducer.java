package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttProducer implements MqttCallback {

    private final GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();

    private MqttClient client;

    public MqttProducer() {
    }

    public static void main(String[] args) {
        new MqttProducer().doDemo();
    }

    public void doDemo() {
        try {
            String uri = "tcp://localhost:1883";
            String clientID = UUID.randomUUID().toString();
            MemoryPersistence persistence = new MemoryPersistence();
            System.out.println("*** uri = " + uri);
            System.out.println("*** UUID = " + clientID);
            client = new MqttClient(uri, clientID, persistence);
            client.connect();
            client.setCallback(this);

            MqttMessage message = new MqttMessage();

            String json = "{ \"id\":1, \"brand\":\"Citroen\", \"numberPlate\":\"TT-458-CC\", \"type\":1 }";

            message.setPayload(json.getBytes());
            System.out.println("*** message envoyé : " + json);
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
        System.out.println("Delivery complete...");
    }

}
