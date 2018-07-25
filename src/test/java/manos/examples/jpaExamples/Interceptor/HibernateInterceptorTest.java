package manos.examples.jpaExamples.Interceptor;

import manos.examples.jpaExamples.Interceptor.model.SampleEntityClass;
import manos.examples.jpaExamples.hibernateNoXmlBootstrap.BootstrapH2InMemDatabase;
import org.hibernate.EmptyInterceptor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.Map;

public class HibernateInterceptorTest {

    /**
     * The Hibernate Interceptor is an interface that allows us to react to certain events within Hibernate.
     * These interceptors are registered as callbacks and provide communication links between Hibernate’s session and application.
     * With such a callback, an application can intercept core Hibernate’s operations such as save, update, delete, etc.
     * There are two ways of defining interceptors:
     *      ->implementing the org.hibernate.Interceptor interface
     *      ->extending the org.hibernate.EmptyInterceptor class
     */

    /**
     * The EmptyInterceptor Class has the same implementation as the implemented interface below
     */
    private class CustomInterceptorFromEmpty extends EmptyInterceptor{

    }




    private static EntityManagerFactory factory;
    @BeforeClass
    public static void before(){
        Map<String, String> extraProps = new HashMap<>();
        extraProps.put("hibernate.ejb.interceptor", InterceptorImpl.class.getCanonicalName());
        extraProps.put("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        factory = BootstrapH2InMemDatabase.createEntityManagerFactory(HibernateInterceptorTest.class.getSimpleName(), new Class[]{SampleEntityClass.class}, extraProps);
    }

    @AfterClass
    public static void after(){
        if (factory != null && factory.isOpen())
            factory.close();
        factory = null;
    }

    private void persistOneUnmanagedEntity(){
        SampleEntityClass sampleEntityClass1 = new SampleEntityClass("I am a entity trying to persist");
        EntityManager em = factory.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(sampleEntityClass1);
            et.commit();
        } catch (Exception e){
            if (et != null && et.isActive())
                et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    @Test
    public void persistOne(){
        persistOneUnmanagedEntity();
        InterceptorImpl.entrySet().forEach(System.out::println);
    }

    private void selectAListOFEntities(){
        EntityManager em = factory.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.createQuery("select sec from SampleEntityClass sec").getResultList();
            et.commit();
        } catch (Exception e){
            if (et != null && et.isActive())
                et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    @Test
    public void selectEntity(){
        persistOneUnmanagedEntity();
        InterceptorImpl.clear();

        selectAListOFEntities();
        InterceptorImpl.entrySet().forEach(System.out::println);
    }

    private void findOneAndUpdate(){
        EntityManager em = factory.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.find(SampleEntityClass.class, 1)
            .setAttribute("A changed attribute");
            et.commit();
        } catch (Exception e){
            if (et != null && et.isActive())
                et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    @Test
    public void updateAnEntity(){
        persistOneUnmanagedEntity();
        InterceptorImpl.clear();

        findOneAndUpdate();
        InterceptorImpl.entrySet().forEach(System.out::println);
    }

    private void mergeExistingAndNewEntity(){
        EntityManager em = factory.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            SampleEntityClass existingSampleEntity = new SampleEntityClass();
            existingSampleEntity.setId(1);
            existingSampleEntity.setAttribute("A changed attribute for existing entity");
            SampleEntityClass newSampleEntity = new SampleEntityClass("A new entity with a new attribute");

            em.merge(existingSampleEntity);
            em.merge(newSampleEntity);
            et.commit();
        } catch (Exception e){
            if (et != null && et.isActive())
                et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Test
    public void mergeAndExistingAndANewEntity(){
        persistOneUnmanagedEntity();
        InterceptorImpl.clear();

        mergeExistingAndNewEntity();
        InterceptorImpl.entrySet().forEach(System.out::println);
    }
}
