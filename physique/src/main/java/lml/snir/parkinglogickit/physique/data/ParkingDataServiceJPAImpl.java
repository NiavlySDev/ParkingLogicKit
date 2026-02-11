package lml.snir.parkinglogickit.physique.data;

import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile Alari
 */
public class ParkingDataServiceJPAImpl extends AbstracCrudServiceJPA<Parking> implements ParkingDataService {

    public ParkingDataServiceJPAImpl(String PU) {
        super(PU);
    }

    @Override
    public Parking getById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Parking getByUsername(String contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Parking getByCountPlace(int contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
