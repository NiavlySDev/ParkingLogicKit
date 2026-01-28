package lml.snir.parkinglogickit.physique.data;

/**
 * qxZEALmd-Cfux6V3 gestiontemperature
 *
 * @author fanou
 */
public final class PhysiqueDataFactory {
    private PhysiqueDataFactory() {
    }

    private static final String PU = "lml.snir.parkinglogickit_ParkingLogicKitCommon_jar_1.0PU";
    private static final boolean JDBC = false;

//    private static DriverDataService usrSrv = null;
//    public static synchronized DriverDataService getDriverDataService() throws Exception {
//        if (usrSrv == null) {
//            if (JDBC) {
//                //usrSrv = new UtilisateurDataServiceDBCImpl();
//            } else {
//                usrSrv = new DriverDataServiceJPAImpl(PU);
//            }
//        }
//
//        return usrSrv;
//    }
//
//    private static BadgeDataService badgeSrv = null;
//    public static synchronized BadgeDataService getBadgeDataService() throws Exception {
//        if (badgeSrv == null) {
//            if (JDBC) {
//                //badgeSrv = new BadgeDataServiceJDBCImpl();
//            } else {
//                badgeSrv = new BadgeDataServiceJPAImpl(PU);
//            }
//        }
//
//        return badgeSrv;
//    }
    /**
    private static AttributionDataService attributionSrv = null;
    public static synchronized AttributionDataService getAttributionDataService() throws Exception {
        if (attributionSrv == null) {
            if (JDBC) {
                //attributionSrv = new AttributionDataServiceJDBCImpl();
            } else {
                attributionSrv = new AttributionDataServiceJPAImpl(PU);
            }
        }

        return attributionSrv;
    }

    private static LocalDataService localSrv = null;
    public static synchronized LocalDataService getLocalDataService() throws Exception {
        if (JDBC) {
                //localSrv = new LocalDataServiceJDBCImpl();
            } else {
                localSrv = new LocalDataServiceJPAImpl(PU);
            }

        return localSrv;
    }

    private static TemperatureDataService temperatureSrv = null;
    public static synchronized TemperatureDataService getTemperatureDataService() throws Exception {
        if (JDBC) {
                //temperatureSrv = new TemperatureDataServiceJDBCImpl();
            } else {
                temperatureSrv = new TemperatureDataServiceJPAImpl(PU);
            }

        return temperatureSrv;
    }
    **/
}
