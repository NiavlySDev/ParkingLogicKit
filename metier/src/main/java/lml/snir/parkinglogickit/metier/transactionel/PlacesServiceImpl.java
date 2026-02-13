package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Places;
//import lml.snir.parkinglogickit.data.PlacesDataService;

/**
 *
 * @author Phily Seck
 */
public final class PlacesServiceImpl implements PlacesService {

//    private final PlacesDataService PlacesDataSrv;
//
//    public PlacesServiceImpl() throws Exception {
//        this.PlacesDataSrv = PhysiqueDataFactory.getPlacesDataService();
//    }
    @Override
    public Places getById(Long id) throws Exception {
        return null;
//         return this.PlacesDataSrv.getById(id);
    }

    @Override
    public Places getByIsOccuped(boolean attribue) throws Exception {
        return null;
//        return this.PlacesDataSrv.getByIsOccuped();
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
