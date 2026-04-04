package lml.snir.parkinglogickit.physique.data.test;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.entity.VehicleType;
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

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.populate();
        test.testUtilisateur();
        test.testBadge();
        test.testVehicule();
        test.testAssociate();
    }

    private Test() throws Exception {
        this.vehicleSrv = PhysiqueDataFactory.getVehicleDataService();
        this.usrSrv = PhysiqueDataFactory.getDriverDataService();
        this.badgeSrv = PhysiqueDataFactory.getBadgeDataService();
        this.assoSrv = PhysiqueDataFactory.getAssociateDataService();
        this.parkingSrv = PhysiqueDataFactory.getParkingDataService();
    }

    private void populate() throws Exception {
        this.deleteDatabase();
        this.populateDriver();
        this.populateParking();
    }

    private void deleteDatabase() throws Exception {
        List<Driver> drivers = this.usrSrv.getAll();
        for (Driver d : drivers) {
            this.usrSrv.remove(d);
        }
    }

    private void populateDriver() throws Exception {
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

        drv = new Admin();
        drv.setAge(50);
        drv.setFirstName("Test");
        drv.setIsMale(true);
        drv.setUsername("TT");
        drv.setLastName("test");
        drv.setPassword("test");
        this.usrSrv.add(drv);

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

        b = new Badge();
        b.setContent("0009966294");
        this.badgeSrv.add(b);

        drv = new Driver();
        drv.setAge(25);
        drv.setFirstName("Marge");
        drv.setIsMale(false);
        drv.setUsername("MS");
        drv.setLastName("Simpson");
        drv.setPassword("secret");
        this.usrSrv.add(drv);

        drv = new Driver();
        drv.setAge(20);
        drv.setFirstName("LEV");
        drv.setIsMale(false);
        drv.setUsername("LE");
        drv.setLastName("ELV");
        drv.setPassword("levivi");
        this.usrSrv.add(drv);

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

        l = new Vehicle();
        l.setNumberPlate("WW-715-TT");
        l.setBrand("Renault");
        l.setType(VehicleType.Voiture);
        this.vehicleSrv.add(l);

        Associate asso = new Associate();
        asso.setBadge(b);
        asso.setVehicle(l);
        asso.setDriver(drv);
        this.assoSrv.add(asso);

    }

    private void populateParking() throws Exception {

        Parking p = new Parking();
        p.setPlaceCount(48);
        p.setTotalPlace(60);
        p.setIsFull(false);
        this.parkingSrv.add(p);
    }

    private void testUtilisateur() throws Exception {
        List<Driver> drivers = this.usrSrv.getAll();
        for (Driver d : drivers) {
            System.out.println(d);
        }
    }

    private void testBadge() throws Exception {
        List<Badge> badges = this.badgeSrv.getAll();
        for (Badge b : badges) {
            System.out.println(b);
        }
    }

    private void testVehicule() throws Exception {
        List<Vehicle> vehicles = this.vehicleSrv.getAll();
        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    private void testAssociate() throws Exception {
        List<Associate> associates = this.assoSrv.getAll();
        for (Associate a : associates) {
            System.out.println(a);
        }
    }
}
