package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;
import lml.snir.parkinglogickit.physique.data.PlacesDataService;


/**
 *
 * @author Phily Seck
 */
public final class PlacesServiceImpl implements PlacesService {
    private final PlacesDataService PlacesDataSrv;

    public PlacesServiceImpl() throws Exception {
      this.PlacesDataSrv = PhysiqueDataFactory.getPlacesDataService();
    }
    
    @Override
    public Places getById(Long id) throws Exception {
         return this.PlacesDataSrv.getById(id);
    }

    @Override
    public Places getByIsOccuped(boolean attribue) throws Exception {
         return this.PlacesDataSrv.getByIsOccuped(attribue);
    }

    @Override
    public Places add(Places t) throws Exception {
          return this.PlacesDataSrv.add(t);
    }

    @Override
    public void remove(Places t) throws Exception {
        this.PlacesDataSrv.remove(t);
    }

    @Override
    public void update(Places t) throws Exception {
     this.PlacesDataSrv.update(t);
    }

    @Override
    public long getCount() throws Exception {
         return this.PlacesDataSrv.getCount();
    }

    @Override
    public List<Places> getAll() throws Exception {
         return this.PlacesDataSrv.getAll();
    }

    @Override
    public List<Places> getAll(int begin, int count) throws Exception {
           return this.PlacesDataSrv.getAll(begin, count);
    }

}
