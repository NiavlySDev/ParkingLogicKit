package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author Stéphane Alonso
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

////    @Override
//    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.out.println("[" + topic + "] " + message);
//        Temperature temp = gson.fromJson(message.toString(), Mesure.class);
//        temp.setDate(new Date());
//        PhysiqueDataFactory.getTemperatureDataService().add(temp);
//    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete...");
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
