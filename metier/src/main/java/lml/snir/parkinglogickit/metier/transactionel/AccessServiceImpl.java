package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Access;
//import lml.snir.parklogickit.physique.data.AccessDataService;

/**
 *
 * @author Phily Seck
 */
public class AccessServiceImpl implements AccessService {

//    private final AccessDataService accessDataSrv;
//    public AccessServiceImpl() throws Exception {
//        this.accessDataSrv = PhysiqueDataFactory.getAccessDataService();
//    }
    @Override
    public Access getById(long id) throws Exception {
        return null;
//        return this.accessDataSrv.getById(long id);
    }

    @Override
    public Access getByDriver(String contenu) throws Exception {
        return null;
//       return this.accessDataSrv.getByDriver(contenu);

    }

    @Override
    public Access getByDateTime(String contenu) throws Exception {
        return null;
//     return this.accessDataSrv.getByDateTime(contenu);
    }
    
      @Override
    public Access getByBadge(String contenu) throws Exception {
        return null;
 //     return this.accessDataSrv.getByBadge(contenu);
    }

    @Override
    public Access add(Access t) throws Exception {
        return null;
//        return this.accessDataSrv.add();
    }

    @Override
    public void remove(Access t) throws Exception {
//        return this.accessDataSrv.remove();
    }

    @Override
    public void update(Access t) throws Exception {
//        return this.accessDataSrv.update();
    }

    @Override
    public Access getById(Long id) throws Exception {
       return null;
//        return this.accessDataSrv.getById(Long id);
    }

    @Override
    public long getCount() throws Exception {
        return 0;
//        return this.accessDataSrv.getCount(Count);
    }

    @Override
    public List<Access> getAll() throws Exception {
        return null;
//        return this.accessDataSrv.add();
    }

    @Override
    public List<Access> getAll(int begin, int count) throws Exception {
        return null;
//        return this.accessDataSrv.add();
    }

    @Override
    public Access getByContenu(String contenu) throws Exception {
       return null;
//        return this.accessDataSrv.getByContenu(contenu);
    }

}
