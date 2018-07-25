package manos.examples.db;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

public final class DBUtils {
    private DBUtils(){}
    private static final Logger logger = LogManager.getLogger(DBUtils.class);
    public static EntityManagerFactory initFactory(String persistenceUnitName, Map<String, String> properties){
        try {
            return Persistence.createEntityManagerFactory(persistenceUnitName, properties);
        } catch (Exception e){
            logger.error("Error while creating EntityManagerFactory", e);
        }
        return null;
    }
    public static EntityManager getEntityManagerFromFactory(EntityManagerFactory factory){
        if (factory == null){
            return null;
        }
        if (!factory.isOpen()){
            return null;
        }
        return factory.createEntityManager();
    }
    private static final String SESSION_ERROR_MESSAGE="Exception Occurs during Session";
    public static void doInJPA(EntityManager em, Consumer<EntityManager> function){
        try{
            function.accept(em);
        } catch(Exception e){
            logger.warn(SESSION_ERROR_MESSAGE, e);
        } finally{
            em.close();
        }
    }
    public static <T extends Optional> T doInJPA(EntityManager em, Function<EntityManager, T> function){
        try{
            return function.apply(em);
        } catch(Exception e){
            logger.warn(SESSION_ERROR_MESSAGE, e);
        } finally{
            em.close();
        }
        return (T)Optional.empty();
    }
    public static void  doInTransaction(EntityManager em, Runnable runnable){
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            runnable.run();
            et.commit();
        } catch (Throwable e){
            if (et != null && et.isActive())
                et.rollback();
            throw e;
        }
    }

}
