package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Parking;
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
//        try {
            String uri = "tcp://localhost:1883";
            String clientID = UUID.randomUUID().toString();
            MemoryPersistence persistence = new MemoryPersistence();
            System.out.println("*** uri = " + uri);
            System.out.println("*** UUID = " + clientID);
//            client = new MqttClient(uri, clientID, persistence);

//            client.connect();
            client.setCallback(this);

            MqttMessage message = new MqttMessage();
}

//            Local l = new Local();
//            l.setNumero(458L);
//
//            Temperature temp = new Mesure();
//            //temp.setDate(new Date());
//            temp.setLocal(l);
//            temp.setValue(17.8F);
            
//            String json = gson.toJson(temp);

//            message.setPayload(json.getBytes());
//            System.out.println("*** msgId = " + message.getId());
//            client.publish(MetierFactory.getTopic(), message);
//
//            client.disconnect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
 }
    


