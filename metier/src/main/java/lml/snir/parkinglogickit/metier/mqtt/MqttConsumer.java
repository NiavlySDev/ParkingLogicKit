package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Set;
import java.util.UUID;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
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

    private static class AccessRequest {

        String type;
        String valeur;
    }

    private static final Set<String> PLAQUES_AUTORISEES = Set.of(
            "WW-238-PG",
            "AH-722-YK"
    );

    private static final Set<String> BADGES_AUTORISES = Set.of(
            "8310F2AA",
            "736188AB",
            "7307D8AA",
            "F3C790AB"
    );

    private static final String TOPIC_IN = "IR/in";
    private static final String TOPIC_OUT = "ER/out";
    private static final String BROKER_URI = "tcp://localhost:1883";

    private static final String REPONSE_AUTORISE = "AUTORISÉ";
    private static final String REPONSE_REFUSE = "REFUSÉ";

    private MqttClient client;

    public MqttConsumer( ) {
    }

    public static void main(String[] args) {
    new MqttConsumer().demarrer();
}

    public MqttClient getClient() {
        return client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public void demarrer() {
        try {
            String clientID = "consumer-" + UUID.randomUUID();
            MemoryPersistence persistence = new MemoryPersistence();

            System.out.println("MqttConsumer démarrage");
            System.out.println("Broker   : " + BROKER_URI);
            System.out.println("Topic IN : " + TOPIC_IN);
            System.out.println("Topic OUT: " + TOPIC_OUT);

            client = new MqttClient(BROKER_URI, clientID, persistence);
            client.setCallback(this);
            client.connect();

            client.subscribe(TOPIC_IN);
            System.out.println("Abonné à " + TOPIC_IN + " — en attente de messages...\n");

            while (client.isConnected()) {
                Thread.sleep(1000);
            }

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AccessRequest parseRequest(String json) {
        return gson.fromJson(json, AccessRequest.class);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload()).trim();

        Vehicle v = gson.fromJson(payload, Vehicle.class);
        Access a = gson.fromJson(payload, Access.class);

        System.out.println("ID     : " + v.getId());
      //System.out.println("Badge  : " + a.get());
        System.out.println("Driver : " + a.getDriver());
        System.out.println("─────────────────────────────────────");
        System.out.println("Message reçu sur [" + topic + "] : " + payload);

        AccessRequest request = parseRequest(payload);

        String type = request.type != null ? request.type.toLowerCase() : "inconnu";
        String valeur = request.valeur != null ? request.valeur : "";

        boolean autorise = verifierAutorisation(type, valeur);
        String reponse = autorise ? REPONSE_AUTORISE : REPONSE_REFUSE;

        System.out.println("Type     : " + type);
        System.out.println("Valeur   : " + valeur);
        System.out.println("Décision : " + reponse);

        publierlaReponse(reponse);
    }

    private boolean verifierAutorisation(String type, String valeur) {
        if (valeur.isEmpty()) {
            return false;
        }
        switch (type) {
            case "plaque":
                return PLAQUES_AUTORISEES.contains(valeur.toUpperCase());
            case "badge":
                return BADGES_AUTORISES.contains(valeur.toUpperCase());
            default:
                return false;
        }
    }

    private void publierlaReponse(String reponse) throws MqttException {
        MqttMessage msg = new MqttMessage(reponse.getBytes());
        msg.setQos(1);
        client.publish(TOPIC_OUT, msg);
        System.out.println("Réponse publiée sur [" + TOPIC_OUT + "] : " + reponse);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("Connexion perdue : " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete.");
    }
}
