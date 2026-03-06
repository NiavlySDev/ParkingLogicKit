package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.physique.data.PlacesDataServiceJPAImpl;

/**
 *
 * @author Phily Seck
 */
public final class PlacesServiceImpl implements PlacesService {

//    private final PlacesDataServiceJPAImpl PlacesDataSrv;
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
      return null;
//        return this.PlacesDataSrv.add();
    }

    @Override
    public void remove(Places t) throws Exception {
//        return this.PlacesDataSrv.remove();
    }

    @Override
    public void update(Places t) throws Exception {
//        return this.PlacesDataSrv.update();
    }

    @Override
    public long getCount() throws Exception {
        return 0;
//        return this.PlacesDataSrv.getCount();
    }

    @Override
    public List<Places> getAll() throws Exception {
        return null;
//        return this.PlacesDataSrv.getAll();
    }

    @Override
    public List<Places> getAll(int begin, int count) throws Exception {
      return null;
//        return this.PlacesDataSrv.All(int begin, int count);
    }

}
