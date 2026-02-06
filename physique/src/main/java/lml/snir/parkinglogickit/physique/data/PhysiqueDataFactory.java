package lml.snir.parkinglogickit.physique.data;

/**
 *
 * @author virgile
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
                //usrSrv = new UtilisateurDataServiceDBCImpl();
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
               //attributionSrv = new AssociateDataServiceImpl();
            } else {
                associateSrv = new AssociateDataServiceImpl(PU);
            }
        }

        return associateSrv;
    }

    private static VehicleDataService vehicleSrv = null;
    public static synchronized VehicleDataService getVehicleDataService() throws Exception {
        if (JDBC) {
                //localSrv = new VehicleDataServiceJDBCImpl();
            } else {
                vehicleSrv = new VehicleDataServiceJPAImpl(PU);
            }

        return vehicleSrv;
    }

    private static ParkingDataService parkingSrv = null;
    public static synchronized ParkingDataService getParkingDataService() throws Exception {
        if (JDBC) {
                //temperatureSrv = new ParkingDataServiceJDBCImpl();
            } else {
                parkingSrv = new ParkingDataServiceJPAImpl(PU);
            }

        return parkingSrv;
    }
    
}
