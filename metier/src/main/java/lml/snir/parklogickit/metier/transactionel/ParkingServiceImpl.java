package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Parking;
//import lml.snir.parklogickit.data.ParkingDataService;


/**
 *
 * @author fanou
 */
public final class ParkingServiceImpl implements ParkingService {
//    private final ParkingDataService prkDataSrv;
//    
//    public ParkingServiceImpl() throws Exception {
//        this.prkDataSrv = PhysiqueDataFactory.getParkingDataService();
//    }

    @Override
    public Parking getById(long id) throws Exception {
        return null;
//       return this.prkDataSrv.getById(id);
    }

   
     
    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
        return null;
//      return this.prkDataSrv.getByIsFull();
    }

    @Override
    public Parking getByCountPlace(int contenu) throws Exception {
        return null;
//      return this.prkDataSrv.getByCountPlace();
    }

    @Override
    public Parking getByUsername(String contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Parking add(Parking t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Parking t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Parking t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Parking getById(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long getCount() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Parking> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Parking> getAll(int begin, int count) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    
  

    
}
