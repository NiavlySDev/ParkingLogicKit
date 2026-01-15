package lml.snir.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import lml.snir.persistence.CrudService;
/**
 *
 * @author (c) Stéphane ALONSO 2017
 * @param <T>
 */
public abstract class AbstracCrudServiceJPA<T> implements CrudService<T> {

    private String PU = "CRMGreenSpacePU";
    private EntityManagerFactory emf;
    protected EntityManager em;
    private EntityTransaction transac;
    private Class<T> clazz;
    private Throwable lastError;

    protected AbstracCrudServiceJPA(String PU) {
        this.PU = PU;
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        String cl = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        try {
            Class c = Class.forName(cl);
            this.clazz = (Class<T>) c;            
        } catch (Exception e) {
            this.lastError = e;
            System.out.println("Error in AbstracCrudServiceJPA.AbstracCrudServiceJPA() : " + e.getMessage());
        }
    }

    protected final void close() {
        this.em.close();
        this.emf.close();
    }

    protected final void open() {
        try {
            this.emf = Persistence.createEntityManagerFactory(this.PU);
            this.em = this.emf.createEntityManager();
            this.transac = this.em.getTransaction();
            this.transac.begin();
        } catch (Throwable t) {
            this.lastError = t;
            System.out.println("Error in AbstracCrudServiceJPA.open() : " + t.getMessage());
        }
    }

    @Override
    public final T add(T t) throws Exception {
        this.open();
        try {
            this.em.persist(t);
            this.transac.commit();
        } catch (Exception ex) {
            this.lastError = ex;
            throw new Exception("Error in AbstracCrudServiceJPA.add : " + ex.getMessage());
        } finally {
            this.close();
        }
        return t;
    }

    @Override
    public final void update(T t) throws Exception {
        this.open();
        try {
            this.em.merge(t);
            this.transac.commit();
        } catch (Exception ex) {
            this.lastError = ex;
            throw new Exception("Error in AbstracCrudServiceJPA.update : " + ex.getMessage());
        } finally {
            this.close();
        }
    }

    @Override
    public final void remove(T t) throws Exception {
        this.open();
        try {
            t = this.em.merge(t);
            this.em.remove(t);
            this.transac.commit();
        } catch (Exception ex) {
            this.lastError = ex;
            throw new Exception("Error in AbstracCrudServiceJPA.remove : " + ex.getMessage());
        } finally {
            this.close();
        }
    }

    @Override
    public final T getById(Long id) throws Exception {
        T t = null;
        this.open();
        try {
            t = this.em.find(this.clazz, id);
        } catch (Exception ex) {
            this.lastError = ex;
            throw new Exception("Error in AbstracCrudServiceJPA.getById : " + ex.getMessage());
        } finally {
            this.close();
        }

        return t;
    }

    @Override
    public final long getCount() throws Exception {
        Long count = -1l;
        try {
            this.open();
            String str = "SELECT COUNT(obj) FROM " + this.clazz.getSimpleName() + " obj";
            Query query = em.createQuery(str);
            count = ((Long) query.getSingleResult()).longValue();
        } catch (Exception ex) {
            this.lastError = ex;
            throw new Exception("Error in AbstracCrudServiceJPA.getCount : " + ex.getMessage());
        } finally {
            this.close();
        }
        return count.intValue();
    }

    @Override
    public final List<T> getAll(int begin, int count) throws Exception {
        List<T> t = null;
        try {
            this.open();
            String str = "SELECT obj FROM " + this.clazz.getSimpleName() + " obj";
            Query query = em.createQuery(str);
            query.setFirstResult(begin);
            query.setMaxResults(count);
            t = query.getResultList();
        } catch (Exception ex) {
            this.lastError = ex;
            throw new Exception("Error in AbstracCrudServiceJPA.getAll : " + ex.getMessage());
        } finally {
            this.close();
        }
        return t;
    }

    @Override
    public final List<T> getAll() throws Exception {
        List<T> t = null;
        try {
            this.open();
            String str = "SELECT obj FROM " + this.clazz.getSimpleName() + " obj";
            Query query = em.createQuery(str);
            t = query.getResultList();
        } catch (Exception ex) {
            this.lastError = ex;
            System.err.println("List<"+ this.clazz.getSimpleName()+"> AbstracCrudServiceJPA.getAll() : "+ex.getMessage());
            throw new Exception("Error in AbstracCrudServiceJPA.getAll : " + ex.getMessage());
        } finally {
            this.close();
        }
        return t;
    }
    
    public final Throwable getLastError() {
        return this.lastError;
    }

}
