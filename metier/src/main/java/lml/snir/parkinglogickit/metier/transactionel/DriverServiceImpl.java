package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.physique.data.DriverDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
 */
public final class DriverServiceImpl implements DriverService {
    private final DriverDataService drvDataSrv;

    public DriverServiceImpl() throws Exception {
        this.drvDataSrv = PhysiqueDataFactory.getDriverDataService();
    }

    public Driver getByLogin(String login) throws Exception {
        return this.drvDataSrv.getByUsername(login);
    }

    @Override
    public Driver getById(Long id) throws Exception {
        return this.drvDataSrv.getById(id);
    }

    @Override
    public Driver add(Driver t) throws Exception {
        return this.drvDataSrv.add(t);
    }

    @Override
    public void remove(Driver t) throws Exception {
        this.drvDataSrv.remove(t);

    }

    @Override
    public void update(Driver t) throws Exception {
        this.drvDataSrv.update(t);

    }

    @Override
    public long getCount() throws Exception {
        return this.drvDataSrv.getCount();

    }

    @Override
    public List<Driver> getAll() throws Exception {
        return this.drvDataSrv.getAll();
    }

    @Override
    public List<Driver> getAll(int begin, int count) throws Exception {
        return this.drvDataSrv.getAll(begin, count);
    }

    @Override
    public Driver getByUsername(String usr) throws Exception {
        return this.drvDataSrv.getByUsername(usr);
    }

}
