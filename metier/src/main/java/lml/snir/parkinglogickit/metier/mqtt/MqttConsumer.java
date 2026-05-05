package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.UUID;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author phily
 */
public class MqttConsumer implements MqttCallback {

    private final GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();

    private MqttClient client;
    private final String topic;

    public MqttConsumer(String topic) {
        this.topic = topic;
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
            client.subscribe(this.topic);
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

        Vehicle v = gson.fromJson(message.toString(), Vehicle.class);

        Access a = gson.fromJson(message.toString(), Access.class);

        System.out.println("ID: " + v.getId());
        System.out.println("Badge: " + a.getBadge());
        System.out.println("Driver: " + a.getDriver());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        System.out.println("Delivery complete...");
    }

}
