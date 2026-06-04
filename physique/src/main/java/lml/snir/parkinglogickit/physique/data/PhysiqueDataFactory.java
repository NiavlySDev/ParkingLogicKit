package lml.snir.parkinglogickit.physique.data;

/**
 *
 * @author Virgile Alari
 */
public final class PhysiqueDataFactory {

    private PhysiqueDataFactory() {
    }

    private static final String PU = "lml.snir.ParkingLogicKitCommon_jar_1.0PU";

    private static DriverDataService usrSrv = null;

    public static synchronized DriverDataService getDriverDataService() throws Exception {
        if (usrSrv == null) {

            usrSrv = new DriverDataServiceJPAImpl(PU);
        }

        return usrSrv;
    }

    private static BadgeDataService badgeSrv = null;

    public static synchronized BadgeDataService getBadgeDataService() throws Exception {
        if (badgeSrv == null) {

            badgeSrv = new BadgeDataServiceJPAImpl(PU);
        }

        return badgeSrv;
    }

    private static AssociateDataService associateSrv = null;

    public static synchronized AssociateDataService getAssociateDataService() throws Exception {
        if (associateSrv == null) {

            associateSrv = new AssociateDataServiceJPAImpl(PU);
        }

        return associateSrv;
    }

    private static VehicleDataService vehicleSrv = null;

    public static synchronized VehicleDataService getVehicleDataService() throws Exception {
        if (vehicleSrv == null) {

            vehicleSrv = new VehicleDataServiceJPAImpl(PU);
        }

        return vehicleSrv;
    }

    private static ParkingDataService parkingSrv = null;

    public static synchronized ParkingDataService getParkingDataService() throws Exception {
        if (parkingSrv == null) {

            parkingSrv = new ParkingDataServiceJPAImpl(PU);
        }

        return parkingSrv;
    }

    private static AccessDataService AccessSrv = null;

    public static synchronized AccessDataService getAccessDataService() throws Exception {
        if (AccessSrv == null) {

            AccessSrv = new AccessDataServiceJPAImpl(PU);
        }

        return AccessSrv;
    }

}
