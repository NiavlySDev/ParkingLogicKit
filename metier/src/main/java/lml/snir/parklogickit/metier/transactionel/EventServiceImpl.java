package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Event;
//import lml.snir.parklogickit.data.EventDataService;


/**
 *
 * @author fanou
 */
public class EventServiceImpl implements EventService {
//    private final EventDataService eventDataSrv;
//    
//    public EventServiceImpl() throws Exception {
//        this.eventDataSrv = PhysiqueDataFactory.getEventDataService();
//    }





    @Override
    public Event getById(long id) throws Exception {
        return null;
//         return this.eventDataSrv.getById(id);
    }
    @Override
    public Event getByIsEntered(boolean attribue) throws Exception {
        return null;
//      return this.eventDataSrv.getByEntered();
    }

    @Override
    public Event add(Event t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Event t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Event t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Event getById(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long getCount() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Event> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Event> getAll(int begin, int count) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


 
    
}
