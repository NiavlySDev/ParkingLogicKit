package lml.snir.parkinglogickit.physique.data.test;

import java.text.SimpleDateFormat;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.entity.VehicleType;
import lml.snir.parkinglogickit.physique.data.AccessDataService;
import lml.snir.parkinglogickit.physique.data.AssociateDataService;
import lml.snir.parkinglogickit.physique.data.BadgeDataService;
import lml.snir.parkinglogickit.physique.data.DriverDataService;
import lml.snir.parkinglogickit.physique.data.ParkingDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;
import lml.snir.parkinglogickit.physique.data.VehicleDataService;

/**
 *
 * @author Virgile Alari
 */
public class Test {

    private final VehicleDataService vehicleSrv;
    private final DriverDataService usrSrv;
    private final BadgeDataService badgeSrv;
    private final AssociateDataService assoSrv;
    private final ParkingDataService parkingSrv;
    private final AccessDataService accessSrv;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.populateUtilisateur();
        test.populateBadge();
        test.populateVehicule();
        test.populateDriver();
        test.populateParking();
        test.populateAccess();
        test.testGetter();
    }

    private Test() throws Exception {
        this.vehicleSrv = PhysiqueDataFactory.getVehicleDataService();
        this.usrSrv = PhysiqueDataFactory.getDriverDataService();
        this.badgeSrv = PhysiqueDataFactory.getBadgeDataService();
        this.assoSrv = PhysiqueDataFactory.getAssociateDataService();
        this.parkingSrv = PhysiqueDataFactory.getParkingDataService();
        this.accessSrv = PhysiqueDataFactory.getAccessDataService();
    }

    private void populateParking() throws Exception {

        Parking p = new Parking();
        p.setPlaceCount(48);
        p.setTotalPlace(60);
        p.setIsFull(false);
        this.parkingSrv.add(p);
    }

    private void populateUtilisateur() throws Exception {

        Driver drv;

        drv = new Admin();
        drv.setAge(45);
        drv.setFirstName("homer");
        drv.setIsMale(true);
        drv.setUsername("HS");
        drv.setLastName("Simpson");
        drv.setPassword("secret");
        this.usrSrv.add(drv);

        drv = new Admin();
        drv.setAge(20);
        drv.setFirstName("Virgile");
        drv.setIsMale(true);
        drv.setUsername("VA");
        drv.setLastName("Alar");
        drv.setPassword("vivi");
        this.usrSrv.add(drv);

        drv = new Driver();
        drv.setAge(20);
        drv.setFirstName("LEV");
        drv.setIsMale(false);
        drv.setUsername("LE");
        drv.setLastName("ELV");
        drv.setPassword("levivi");
        this.usrSrv.add(drv);

        List<Driver> drivers = this.usrSrv.getAll();
        for (Driver d : drivers) {
            System.out.println(d);
        }
    }

    private void populateBadge() throws Exception {

        Badge b = new Badge();
        b.setContent("0014511054");
        this.badgeSrv.add(b);

        b = new Badge();
        b.setContent("0009803529");
        this.badgeSrv.add(b);

        b = new Badge();
        b.setContent("0009966230");
        this.badgeSrv.add(b);

        b = new Badge();
        b.setContent("0009966120");
        this.badgeSrv.add(b);

        b = new Badge();
        b.setContent("0009969730");
        this.badgeSrv.add(b);

        b = new Badge();
        b.setContent("0009966370");
        this.badgeSrv.add(b);

        b = new Badge();
        b.setContent("0009960730");
        this.badgeSrv.add(b);

        List<Badge> badges = this.badgeSrv.getAll();
        for (Badge ba : badges) {
            System.out.println(ba);
        }
    }

    private void populateVehicule() throws Exception {

        Vehicle l = new Vehicle();
        l.setNumberPlate("TT-458-CC");
        l.setBrand("Citroen");
        l.setType(VehicleType.Voiture);
        this.vehicleSrv.add(l);

        l = new Vehicle();
        l.setNumberPlate("TT-459-CC");
        l.setBrand("Citroen");
        l.setType(VehicleType.Camion);
        this.vehicleSrv.add(l);

        l = new Vehicle();
        l.setNumberPlate("TT-435-CC");
        l.setBrand("Citroen");
        l.setType(VehicleType.Moto);
        this.vehicleSrv.add(l);

        l = new Vehicle();
        l.setNumberPlate("TT-859-CC");
        l.setBrand("Citroen");
        l.setType(VehicleType.Moto);
        this.vehicleSrv.add(l);

        l = new Vehicle();
        l.setNumberPlate("WW-411-TT");
        l.setBrand("Renault");
        l.setType(VehicleType.Camionnette);
        this.vehicleSrv.add(l);

        l = new Vehicle();
        l.setNumberPlate("WW-265-TT");
        l.setBrand("Renault");
        l.setType(VehicleType.Voiture);
        this.vehicleSrv.add(l);

        List<Vehicle> vehicles = this.vehicleSrv.getAll();
        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    private void populateDriver() throws Exception {

        Badge b = new Badge();
        b = new Badge();
        b.setContent("0009966294");
        this.badgeSrv.add(b);

        Vehicle l = new Vehicle();
        l = new Vehicle();
        l.setNumberPlate("WW-715-TT");
        l.setBrand("Renault");
        l.setType(VehicleType.Voiture);
        this.vehicleSrv.add(l);

        Driver drv;
        drv = new Driver();
        drv.setAge(25);
        drv.setFirstName("Marge");
        drv.setIsMale(false);
        drv.setUsername("MS");
        drv.setLastName("Simpson");
        drv.setPassword("secret");
        this.usrSrv.add(drv);

        Associate asso = new Associate();
        asso.setBadge(b);
        asso.setVehicle(l);
        asso.setDriver(drv);
        this.assoSrv.add(asso);

        List<Associate> associates = this.assoSrv.getAll();
        for (Associate a : associates) {
            System.out.println(a);
        }
    }

    private void populateAccess() throws Exception {
        Driver drv;
        drv = new Driver();
        drv.setAge(50);
        drv.setFirstName("Test");
        drv.setIsMale(true);
        drv.setUsername("TT");
        drv.setLastName("test");
        drv.setPassword("test");
        this.usrSrv.add(drv);

        Access a = new Access();
        a.setBadge(true);
        a.setPlate(true);
        a.setDigicode(false);
        a.setDriver(drv);
        a.setIsOpen(true);
        a.setDate((this.sdf.parse("2026-06-03-10-56-12")));
        this.accessSrv.add(a);

        a.setBadge(false);
        a.setPlate(true);
        a.setDigicode(true);
        a.setDriver(drv);
        a.setIsOpen(false);
        a.setDate((this.sdf.parse("2026-06-03-11-36-12")));
        this.accessSrv.add(a);

        a.setBadge(true);
        a.setPlate(true);
        a.setDigicode(false);
        a.setDriver(drv);
        a.setIsOpen(true);
        a.setDate((this.sdf.parse("2026-06-03-11-38-12")));
        this.accessSrv.add(a);

        List<Access> access = this.accessSrv.getAll();
        for (Access ac : access) {
            System.out.println(ac);
        }
    }

    private void testGetter() throws Exception {

        System.out.println("========== Test getter ==========");
        List<Access> ByDriver = this.accessSrv.getByDriver((long) 5);
        System.out.println("Test getByDriver : \n" + ByDriver);
        List<Access> IsOpen = this.accessSrv.getByIsOpen(true);
        System.out.println("Test getByIsOpen : \n" + IsOpen);
        List<Access> access = this.accessSrv.getByDate("2026-06-03 11:36:12");
        System.out.println("Test getByDate : \n" + access);

        Badge badge = this.badgeSrv.getByContent("0009966230");
        System.out.println("Test getByContent : \n" + badge);

        Driver driver = this.usrSrv.getByUsername("HS");
        System.out.println("Test getByUsername : \n" + driver);

        Parking parking = this.parkingSrv.getByIsFull(false);
        System.out.println("Test getByIsFull : \n" + parking);

        Vehicle vehicle = this.vehicleSrv.getByContent("TT-458-CC");
        System.out.println("Test getByContent : \n" + vehicle);
        List<Vehicle> vehicleAssociate = this.vehicleSrv.getByAssociate(true);
        System.out.println("Test getByAssociate : \n" + vehicleAssociate);
    }

}
