package lml.snir.parkinglogickit.physique.data.test;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.Maintenance;
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
 * @author virgile
 */
public class Test {

    //private final TemperatureDataService tempSrv;
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
        //this.tempSrv = PhysiqueDataFactory.getTemperatureDataService();
        this.usrSrv = PhysiqueDataFactory.getDriverDataService();
        this.badgeSrv = PhysiqueDataFactory.getBadgeDataService();
        this.assoSrv = PhysiqueDataFactory.getAssociateDataService();
        this.parkingSrv = PhysiqueDataFactory.getParkingDataService();
    }

    private void populate() throws Exception {
        this.populateDriver();
        this.populateLocaux();
    }

    private void populateDriver() throws Exception {
        Driver drv;

        drv = new Admin();
        drv.setAge(45);
        drv.setFirstName("homer");
        drv.setIsMale(true);
        drv.setUsername("HS");
        drv.setLastname("Simpson");
        drv.setPassword("secret");
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

        drv = new Driver();
        drv.setAge(25);
        drv.setFirstName("Marge");
        drv.setIsMale(false);
        drv.setUsername("MS");
        drv.setLastname("Simpson");
        drv.setPassword("secret");
        this.usrSrv.add(drv);

        drv = new Maintenance();
        drv.setAge(33);
        drv.setFirstName("Test");
        drv.setIsMale(false);
        drv.setUsername("Test");
        drv.setLastname("hh");
        drv.setPassword("secret");
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

        Associate asso = new Associate();
        asso.setBadge(b);
        asso.setVehicle(l);
        asso.setDriver(drv);
        this.assoSrv.add(asso);
    }

    private void populateLocaux() throws Exception {
        
        Parking p = new Parking();
        p.setPlaceCount(60);
        p.setTotalPlace(60);
        p.setHandicapCount(2);
        p.setTotalHandicap(4);
        p.setIsFull(false);
        this.parkingSrv.add(p);
    }

    /**
     * private void populateTemperature() throws Exception { Temperature temp;
     * Local local;
     *
     * temp = new Consigne();
     * temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
     * temp.setValue(19.5F); local = this.localSrv.getById(458L);
     * temp.setLocal(local); this.tempSrv.add(temp);
     *
     * temp = new Consigne();
     * temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
     * temp.setValue(22.5F); local = this.localSrv.getById(459L);
     * temp.setLocal(local); this.tempSrv.add(temp);
     *
     * temp = new Consigne();
     * temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
     * temp.setValue(15.0F); local = this.localSrv.getById(435L);
     * temp.setLocal(local); this.tempSrv.add(temp);
     *
     * temp = new Mesure();
     * temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
     * temp.setValue(17.2F); local = this.localSrv.getById(458L);
     * temp.setLocal(local); this.tempSrv.add(temp);
     *
     * temp = new Mesure();
     * temp.setDate(DateConverter.parseTimeStamp("2025-01-01 10:30:00"));
     * temp.setValue(19.2F); local = this.localSrv.getById(458L);
     * temp.setLocal(local); this.tempSrv.add(temp); }
     *
     * private void testTemperature() throws Exception {
     * System.out.println("this.tempSrv.getAll();"); List<Temperature> temps =
     * this.tempSrv.getAll(); for (Temperature t : temps) {
     * System.out.println(t); }
     *
     * System.out.println("this.localSrv.getById(458);"); Local l =
     * this.localSrv.getById(458L); System.out.println(l);
     *
     * Date d = DateConverter.parseDate("2025-01-01");
     *
     * System.out.println("this.tempSrv.getConsigneByDate(2025-01-01);");
     * List<Consigne> consignes = this.tempSrv.getConsigneByDate(d); for
     * (Temperature t : consignes) { System.out.println(t); }
     *
     * System.out.println("this.tempSrv.getConsigneByLocal(458);"); consignes =
     * this.tempSrv.getConsigneByLocal(l); for (Temperature t : consignes) {
     * System.out.println(t); }
     *
     * System.out.println("this.tempSrv.getConsigneByLocalAndDate(458,
     * 2025-01-01);"); consignes = this.tempSrv.getConsigneByLocalAndDate(l, d);
     * for (Temperature t : consignes) { System.out.println(t); }
     *
     * System.out.println("this.tempSrv.getMesureByLocal(458);"); List<Mesure>
     * mesures = this.tempSrv.getMesureByLocal(l); for (Temperature t : mesures)
     * { System.out.println(t); }
     *
     * System.out.println("this.tempSrv.getMesureByLocalAndDate(458,
     * 2025-01-01);"); mesures = this.tempSrv.getMesureByLocalAndDate(l, d); for
     * (Temperature t : mesures) { System.out.println(t); } }
     *
     * private void testAttribution() throws Exception { List<Badge> badges =
     * this.badgeSrv.getAll(); System.out.println("this.badgeSrv.getAll()");
     * for(Badge b : badges) { System.out.println(b.getContenu()); }
     *
     * badges = this.badgeSrv.getByAttribution(false);
     * System.out.println("this.badgeSrv.getByAttribution(false)"); for(Badge b
     * : badges) { System.out.println(b.getContenu()); }
     *
     * badges = this.badgeSrv.getByAttribution(true);
     * System.out.println("this.badgeSrv.getByAttribution(true)"); for(Badge b :
     * badges) { System.out.println(b.getContenu()); } }
     *
     */
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
