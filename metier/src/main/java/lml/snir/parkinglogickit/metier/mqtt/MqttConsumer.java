 package lml.snir.parkinglogickit.metier.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Set;
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
    public MqttConsumer(String TOPIC) {
    }

//   test de fausse plaque avec rfid et badge
    private static final Set<String> PLAQUES_AUTORISEES = Set.of(
            "TT-458-CC",
            "AB-123-CD",
            "ZZ-999-AA"
    );

    private static final Set<String> RFID_AUTORISES = Set.of(
            "RFID-001A",
            "RFID-002B",
            "RFID-003C"
    );

    private static final Set<String> BADGES_AUTORISES = Set.of(
            "BADGE-DUPONT",
            "BADGE-MARTIN",
            "BADGE-DURAND"
    );

    
    private static final String TOPIC_IN  = "IR/in";
    private static final String TOPIC_OUT = "ER/out";
    private static final String BROKER_URI = "tcp://localhost:1883";

    private static final String REPONSE_AUTORISE = "AUTORISÉ";
    private static final String REPONSE_REFUSE   = "REFUSÉ";

    private MqttClient client;

    public static void main(String[] args) {
        String TOPIC = null;
        new MqttConsumer(TOPIC).demarrer();
    }

    public void demarrer() {
        try {
            String clientID = "consumer-" + UUID.randomUUID();
            MemoryPersistence persistence = new MemoryPersistence();

            System.out.println("MqttConsumer démarrage ");
            System.out.println("Broker  : " + BROKER_URI);
            System.out.println("Topic IN : " + TOPIC_IN);
            System.out.println("Topic OUT: " + TOPIC_OUT);

            client = new MqttClient(BROKER_URI, clientID, persistence);
            client.setCallback(this);
            client.connect();

            client.subscribe(TOPIC_IN);
            System.out.println(" Abonné à " + TOPIC_IN + " — en attente de messages...\n");

         
            while (client.isConnected()) {
                Thread.sleep(1000);
            }

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

  
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload()).trim();
        Vehicle v = gson.fromJson(message.toString(), Vehicle.class);

        Access a = gson.fromJson(message.toString(), Access.class);

        System.out.println("ID: " + v.getId());

        System.out.println("Badge: " + a.getBadge());

        System.out.println("Driver: " + a.getDriver());

        System.out.println("─────────────────────────────────────");
        System.out.println(" Message reçu sur [" + topic + "] : " + payload);

        
        String valeur = extraireValeur(payload);
        String type   = detecterType(payload);
        boolean autorise = verifierAutorisation(type, valeur);

        String reponse = autorise ? REPONSE_AUTORISE : REPONSE_REFUSE;
        System.out.println("Type     : " + type);
        System.out.println(" Valeur   : " + valeur);
        System.out.println(" Décision : " + reponse);

        publierReponse(reponse);
    }

   
    private String detecterType(String json) {
        if (json.contains("\"type\"")) {
            if (json.toLowerCase().contains("plaque")) return "plaque";
            if (json.toLowerCase().contains("rfid"))   return "rfid";
            if (json.toLowerCase().contains("badge"))  return "badge";
        }
        return "inconnu";
    }

 
    private String extraireValeur(String json) {
        try {
            
            int debut = json.indexOf("\"valeur\"");
            if (debut == -1) return "";
            int guillemet1 = json.indexOf("\"", debut + 8);
            int guillemet2 = json.indexOf("\"", guillemet1 + 1);
            if (guillemet1 == -1 || guillemet2 == -1) return "";
            return json.substring(guillemet1 + 1, guillemet2).trim();
        } catch (Exception e) {
            return "";
        }
    }

   
    private boolean verifierAutorisation(String type, String valeur) {
        if (valeur.isEmpty()) return false;
        switch (type) {
            case "plaque" : return PLAQUES_AUTORISEES.contains(valeur.toUpperCase());
            case "rfid"   : return RFID_AUTORISES.contains(valeur.toUpperCase());
            case "badge"  : return BADGES_AUTORISES.contains(valeur.toUpperCase());
            default       : return false;
        }
    }

    
    private void publierReponse(String reponse) throws MqttException {
        MqttMessage msg = new MqttMessage(reponse.getBytes());
        msg.setQos(1);
        client.publish(TOPIC_OUT, msg);
        System.out.println(" Réponse publiée sur [" + TOPIC_OUT + "] : " + reponse);
    }

  
    @Override
    public void connectionLost(Throwable cause) {
        System.err.println(" Connexion perdue : " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println(" Delivery complete.");
    }

    public void doDemo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}