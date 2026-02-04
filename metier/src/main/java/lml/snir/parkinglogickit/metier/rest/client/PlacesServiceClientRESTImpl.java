package lml.snir.parkinglogickit.metier.rest.client;


import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.metier.transactionel.PlacesService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author fanou
 */
public class PlacesServiceClientRESTImpl extends ClientRest<Places> implements PlacesService {

    public PlacesServiceClientRESTImpl() {
        super.init("PlacesService", new RestServerLocalConfiguration());
    }

    @Override
    public Places getByIsOccuped(boolean attribue) throws Exception {
       super.setPath("getByIsOccuped" );
        return super.getEntity();
    }
    @Override
    public Places add(Places t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Places t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Places t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Places getById(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long getCount() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Places> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Places> getAll(int begin, int count) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
