package lml.snir.parkinglogickit.physique.data;

/**
 *
 * @author Virgile Alari
 */
public final class PhysiqueDataFactory {

    private PhysiqueDataFactory() {
    }

    private static final String PU = "lml.snir.ParkingLogicKitCommon_jar_1.0PU";
    private static final boolean JDBC = false;

    private static DriverDataService usrSrv = null;

    public static synchronized DriverDataService getDriverDataService() throws Exception {
        if (usrSrv == null) {
            if (JDBC) {
//                usrSrv = new UtilisateurDataServiceDBCImpl();
            } else {
                usrSrv = new DriverDataServiceJPAImpl(PU);
            }
        }

        return usrSrv;
    }

    private static BadgeDataService badgeSrv = null;

    public static synchronized BadgeDataService getBadgeDataService() throws Exception {
        if (badgeSrv == null) {
            if (JDBC) {
                //badgeSrv = new BadgeDataServiceJDBCImpl();
            } else {
                badgeSrv = new BadgeDataServiceJPAImpl(PU);
            }
        }

        return badgeSrv;
    }

    private static AssociateDataService associateSrv = null;

    public static synchronized AssociateDataService getAssociateDataService() throws Exception {
        if (associateSrv == null) {
            if (JDBC) {
                //associateSrv = new AssociateDataServiceJPAImpl();
            } else {
                associateSrv = new AssociateDataServiceJPAImpl(PU);
            }
        }

        return associateSrv;
    }

    private static VehicleDataService vehicleSrv = null;

    public static synchronized VehicleDataService getVehicleDataService() throws Exception {
        if (JDBC) {
            //vehicleSrv = new VehicleDataServiceJDBCImpl();
        } else {
            vehicleSrv = new VehicleDataServiceJPAImpl(PU);
        }

        return vehicleSrv;
    }

    private static ParkingDataService parkingSrv = null;

    public static synchronized ParkingDataService getParkingDataService() throws Exception {
        if (JDBC) {
            //parkingSrv = new ParkingDataServiceJDBCImpl();
        } else {
            parkingSrv = new ParkingDataServiceJPAImpl(PU);
        }

        return parkingSrv;
    }

    private static AccessDataService AccessSrv = null;

    public static synchronized AccessDataService getAccessDataService() throws Exception {
        if (JDBC) {
            //AccessSrv = new ParkingDataServiceJDBCImpl();
        } else {
            AccessSrv = new AccessDataServiceJPAImpl(PU);
        }

        return AccessSrv;
    }

    private static PlacesDataService PlacesSrv = null;

    public static synchronized PlacesDataService getPlacesDataService() throws Exception {
        if (JDBC) {
            //PlacesSrv = new PlacesDataServiceJDBCImpl();
        } else {
            PlacesSrv = new PlacesDataServiceJPAImpl(PU);
        }

        return PlacesSrv;
    }

    private static EventDataService EventSrv = null;

    public static synchronized EventDataService getEventDataService() throws Exception {
        if (JDBC) {
            //PlacesSrv = new EventDataServiceJDBCImpl();
        } else {
            EventSrv = new EventDataServiceJPAImpl(PU);
        }

        return EventSrv;
    }
}
