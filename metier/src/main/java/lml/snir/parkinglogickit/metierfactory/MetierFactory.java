package lml.snir.parkinglogickit.metierfactory;

//import lml.snir.gestiontemperature.metier.mqtt.MqttConsumer;
import lml.snir.parkinglogickit.metier.rest.client.AccessServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.AdminServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.AssociateServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.BadgeServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.ParkingServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.EventServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.DriverServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.MaintenanceServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.PlacesServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.rest.client.VehicleServiceClientRESTImpl;
import lml.snir.parkinglogickit.metier.transactionel.AccessService;
import lml.snir.parkinglogickit.metier.transactionel.AccessServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.AdminService;
import lml.snir.parkinglogickit.metier.transactionel.AdminServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.parkinglogickit.metier.transactionel.AssociateServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.BadgeService;
import lml.snir.parkinglogickit.metier.transactionel.BadgeServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.VehicleService;
import lml.snir.parkinglogickit.metier.transactionel.VehicleServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.EventServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.EventService;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.parkinglogickit.metier.transactionel.DriverServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.MaintenanceService;
import lml.snir.parkinglogickit.metier.transactionel.MaintenanceServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.ParkingService;
import lml.snir.parkinglogickit.metier.transactionel.ParkingServiceImpl;
import lml.snir.parkinglogickit.metier.transactionel.PlacesService;
import lml.snir.parkinglogickit.metier.transactionel.PlacesServiceImpl;
import lml.snir.tools.ConfigReader;

public class MetierFactory {
   
    private MetierFactory() {
    }

    private static synchronized boolean readLocalState() {
        boolean local = true;
        try {
            String str = ConfigReader.getInstance("./rest.properties").getProperty("local");
            local = ("true".equalsIgnoreCase(str));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return local;
    }

    private static final String TOPIC = "MESURE_TEMPERATURE";

    public static String getTopic() {
        return TOPIC;
    }

//    private static MqttConsumer consumer = null;

//    public static synchronized void launchMqttConsumer() {
//        if (consumer == null) {
//            consumer = new MqttConsumer(TOPIC);
//            consumer.doDemo();
//        }
//    }

    private static DriverService DriverSrv = null;

    public static synchronized DriverService getDriverService() throws Exception {
        if (DriverSrv == null) {
            if (readLocalState()) {
                DriverSrv = new DriverServiceImpl();
            } else {
                DriverSrv = new DriverServiceClientRESTImpl();
            }
        }

        return DriverSrv;
    }

    private static EventService EventSrv = null;

    public static synchronized EventService getEventService() throws Exception {
        if (EventSrv == null) {
            if (readLocalState()) {
                EventSrv = new EventServiceImpl();
            } else {
               EventSrv = new EventServiceClientRESTImpl();
            }            
        }

        return EventSrv;
    }

    private static MaintenanceService MaintenanceSrv = null;

    public static synchronized MaintenanceService getMaintenanceService() throws Exception {
        if (MaintenanceSrv == null) {
            if (readLocalState()) {
                MaintenanceSrv = new MaintenanceServiceImpl();
            } else {
                MaintenanceSrv = new MaintenanceServiceClientRESTImpl();
            }            
        }

        return MaintenanceSrv;
    }

    private static BadgeService badgeSrv = null;

    public static BadgeService getBadgeService() throws Exception {
        if (badgeSrv == null) {
            if (readLocalState()) {
                badgeSrv = new BadgeServiceImpl();
            } else {
                badgeSrv = new BadgeServiceClientRESTImpl();
            }
        }

        return badgeSrv;
    }

    private static AccessService AccessSrv = null;

    public static AccessService getAccessService() throws Exception {
        if (AccessSrv == null) {
            if (readLocalState()) {
                AccessSrv = new AccessServiceImpl();
            } else {
               AccessSrv = new AccessServiceClientRESTImpl();
            }
        }

        return AccessSrv;
    }
    private static ParkingService ParkingSrv = null;

    public static ParkingService getParkingService() throws Exception {
        if (ParkingSrv == null) {
            if (readLocalState()) {
               ParkingSrv = new ParkingServiceImpl();
            } else {
                ParkingSrv = new ParkingServiceClientRESTImpl();
            }
        }

        return ParkingSrv;
    }
       private static AdminService AdminSrv = null;

    public static AdminService getAdminService() throws Exception {
        if (AdminSrv == null) {
            if (readLocalState()) {
                AdminSrv = new AdminServiceImpl();
            } else {
                AdminSrv = new AdminServiceClientRESTImpl();
            }
        }

        return AdminSrv;
    }

      private static PlacesService PlacesSrv = null;

    public static PlacesService getPlacesService() throws Exception {
        if (PlacesSrv == null) {
            if (readLocalState()) {
                PlacesSrv = new PlacesServiceImpl();
            } else {
                PlacesSrv = new PlacesServiceClientRESTImpl();
            }
        }

        return PlacesSrv;
    }
    
    private static VehicleService VehicleSrv = null;
    
     public static VehicleService getVehicleService() throws Exception {
        if (VehicleSrv == null) {
            if (readLocalState()) {
                VehicleSrv = new VehicleServiceImpl();
            } else {
               VehicleSrv = new VehicleServiceClientRESTImpl();
            }
        }

        return VehicleSrv;

     }

 private static AssociateService AssociateSrv = null;

    public static AssociateService getAssociateService() throws Exception {
        if (AssociateSrv == null) {
            if (readLocalState()) {
               AssociateSrv = new AssociateServiceImpl();
            } else {
              AssociateSrv = new AssociateServiceClientRESTImpl();
            }
        }

        return AssociateSrv;
    }
}