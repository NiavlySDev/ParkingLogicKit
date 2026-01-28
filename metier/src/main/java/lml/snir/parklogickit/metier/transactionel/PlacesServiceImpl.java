package lml.snir.parklogickit.metier.transactionel;

import java.util.List;

import lml.snir.parklogickit.metier.entity.Places;
import lml.snir.parklogickit.data.PlacesDataService;


/**
 *
 * @author fanou
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
        return this.PlacesDataSrv.getByIsOccuped();
    }




    }
