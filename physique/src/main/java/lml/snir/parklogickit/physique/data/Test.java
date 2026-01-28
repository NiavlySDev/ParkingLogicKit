package lml.snir.parklogickit.physique.data;

import lml.snir.parklogickit.metier.entity.Badge;
import lml.snir.parklogickit.metier.entity.Driver;
import lml.snir.parklogickit.metier.entity.Maintenance;

/**
 *
 * @author virgile
 */
public class Test {

    //private final TemperatureDataService tempSrv;
    //private final LocalDataService localSrv;
//    private final DriverDataService usrSrv;
//    private final BadgeDataService badgeSrv;
    //private final AttributionDataService attrSrv;

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.populate();   
//        test.testTemperature();
//        test.testAttribution();
    }

    private Test() throws Exception {
        //this.localSrv = PhysiqueDataFactory.getLocalDataService();
        //this.tempSrv = PhysiqueDataFactory.getTemperatureDataService();
//        this.usrSrv = PhysiqueDataFactory.getDriverDataService();
//        this.badgeSrv = PhysiqueDataFactory.getBadgeDataService();
        //this.attrSrv = PhysiqueDataFactory.getAttributionDataService();
    }

    private void populate() throws Exception {
    }

//    private void populateUtilisateurs() throws Exception {
//        Driver usr;
//
//        usr = new Maintenance();
//        usr.setFirstName("homer");
//        usr.setIsMale(true);
//        usr.setUsername("HS");
//        usr.setSurname("Simpson");
//        usr.setPassword("secret");
//        this.usrSrv.add(usr);
//
//        Badge b = new Badge();
//        b.setContenu("0014511054");
//        b = this.badgeSrv.add(b);
//
//        b = new Badge();
//        b.setContenu("0009803529");
//        b = this.badgeSrv.add(b);
//
//        b = new Badge();
//        b.setContenu("0009966230");
//        b = this.badgeSrv.add(b);
//
//        Associate attr = new Associate();
//        attr.setBadge(b);
//        attr.setUtilisateur(usr);
//        attr = this.attrSrv.add(attr);
//
//        usr = new Driver();
//        usr.setFirstName("Marge");
//        usr.setIsMale(false);
//        usr.setUsername("MS");
//        usr.setSurname("Simpson");
//        usr.setPassword("secret");
//        this.usrSrv.add(usr);
//    }
//
//    private void populateLocaux() throws Exception {
//        Local l = new Local();
//        l.setNumero(458L);
//        l = localSrv.add(l);
//        l = new Local();
//        l.setNumero(459L);
//        l = localSrv.add(l);
//        l = new Local();
//        l.setNumero(435L);
//        l = localSrv.add(l);
//    }
//
//    private void populateTemperature() throws Exception {
//        Temperature temp;
//        Local local;
//
//        temp = new Consigne();
//        temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
//        temp.setValue(19.5F);
//        local = this.localSrv.getById(458L);
//        temp.setLocal(local);
//        this.tempSrv.add(temp);
//
//        temp = new Consigne();
//        temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
//        temp.setValue(22.5F);
//        local = this.localSrv.getById(459L);
//        temp.setLocal(local);
//        this.tempSrv.add(temp);
//
//        temp = new Consigne();
//        temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
//        temp.setValue(15.0F);
//        local = this.localSrv.getById(435L);
//        temp.setLocal(local);
//        this.tempSrv.add(temp);
//
//        temp = new Mesure();
//        temp.setDate(DateConverter.parseTimeStamp("2025-01-01 08:30:00"));
//        temp.setValue(17.2F);
//        local = this.localSrv.getById(458L);
//        temp.setLocal(local);
//        this.tempSrv.add(temp);
//
//        temp = new Mesure();
//        temp.setDate(DateConverter.parseTimeStamp("2025-01-01 10:30:00"));
//        temp.setValue(19.2F);
//        local = this.localSrv.getById(458L);
//        temp.setLocal(local);
//        this.tempSrv.add(temp);
//    }
//
//    private void testTemperature() throws Exception {
//        System.out.println("this.tempSrv.getAll();");
//        List<Temperature> temps = this.tempSrv.getAll();
//        for (Temperature t : temps) {
//            System.out.println(t);
//        }
//
//        System.out.println("this.localSrv.getById(458);");
//        Local l = this.localSrv.getById(458L);
//        System.out.println(l);
//
//        Date d = DateConverter.parseDate("2025-01-01");
//
//        System.out.println("this.tempSrv.getConsigneByDate(2025-01-01);");
//        List<Consigne> consignes = this.tempSrv.getConsigneByDate(d);
//        for (Temperature t : consignes) {
//            System.out.println(t);
//        }
//
//        System.out.println("this.tempSrv.getConsigneByLocal(458);");
//        consignes = this.tempSrv.getConsigneByLocal(l);
//        for (Temperature t : consignes) {
//            System.out.println(t);
//        }
//
//        System.out.println("this.tempSrv.getConsigneByLocalAndDate(458, 2025-01-01);");
//        consignes = this.tempSrv.getConsigneByLocalAndDate(l, d);
//        for (Temperature t : consignes) {
//            System.out.println(t);
//        }
//
//        System.out.println("this.tempSrv.getMesureByLocal(458);");
//        List<Mesure> mesures = this.tempSrv.getMesureByLocal(l);
//        for (Temperature t : mesures) {
//            System.out.println(t);
//        }
//
//        System.out.println("this.tempSrv.getMesureByLocalAndDate(458, 2025-01-01);");
//        mesures = this.tempSrv.getMesureByLocalAndDate(l, d);
//        for (Temperature t : mesures) {
//            System.out.println(t);
//        }
//    }
//
//    private void testAttribution() throws Exception {
//        List<Badge> badges = this.badgeSrv.getAll();
//        System.out.println("this.badgeSrv.getAll()");
//        for(Badge b : badges) {
//            System.out.println(b.getContenu());
//        }
//        
//        badges = this.badgeSrv.getByAttribution(false);
//        System.out.println("this.badgeSrv.getByAttribution(false)");
//        for(Badge b : badges) {
//            System.out.println(b.getContenu());
//        }
//        
//        badges = this.badgeSrv.getByAttribution(true);
//        System.out.println("this.badgeSrv.getByAttribution(true)");
//        for(Badge b : badges) {
//            System.out.println(b.getContenu());
//        }
//    }
//    
//    private void testUtilisateur() throws Exception {
//        List<Utilisateur> users = this.usrSrv.getAll();
//        for (Utilisateur u : users) {
//            System.out.println(u);
//        }
//    }
}