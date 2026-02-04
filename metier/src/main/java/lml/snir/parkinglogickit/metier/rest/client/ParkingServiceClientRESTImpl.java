package lml.snir.parkinglogickit.metier.rest.client;


import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.transactionel.ParkingService;
import lml.snir.rest.client.ClientRest;


/**
 *
 * @author fanou
 */
public class ParkingServiceClientRESTImpl extends ClientRest<Parking> implements ParkingService {

    public ParkingServiceClientRESTImpl() {
        super.init("ParkingService", new RestServerLocalConfiguration());
    }

    @Override
    public Parking getById(long id) throws Exception {
        super.setPath("getByCountPLace" );
        return super.getEntity();
    }

    @Override
    public Parking getByUsername(String contenu) throws Exception {
    super.setPath("getByUsername" );
        return super.getEntity();
    }

    @Override
    public Parking getByCountPlace(int contenu) throws Exception {
      super.setPath("getByCountPLace" );
        return super.getEntity();
    }

    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
        super.setPath("getByIsFull" );
        return super.getEntity();
    }

    @Override
    public Parking add(Parking t) throws Exception {
        super.setPath("" );
        return super.getEntity();
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