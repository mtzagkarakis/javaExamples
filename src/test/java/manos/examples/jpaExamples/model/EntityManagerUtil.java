package manos.examples.jpaExamples.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class EntityManagerUtil {
	private static final Logger logger = LogManager.getLogger();
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;
	
	public static void init(String persistenceUnitName){
		if (entityManagerFactory != null){
			entityManagerFactory.close();
		}
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		} catch (Throwable e){
			e.printStackTrace();
		}
	}
	public static EntityManager getEntityManager(){
		if (entityManagerFactory == null){
			return null;
		}
		if (!entityManagerFactory.isOpen()){
			return null;
		}
		if (entityManager == null || !entityManager.isOpen()){
			try {
				entityManager = entityManagerFactory.createEntityManager();
			} catch(Exception e){
				e.printStackTrace();
				entityManager = null;
			}
			return entityManager;
		} else {
			return entityManager;
		}
	}
	
	public static void doInJPA(Consumer<EntityManager> function){
		EntityManager em = EntityManagerUtil.getEntityManager();
		logger.debug("Entity manager open");
		try{
			function.accept(em);
		} catch(Throwable e){
			e.printStackTrace();
		} finally{
			em.close();
			logger.debug("EntityManager closed");
		}
	}
	
	public static void doInJPAInTransaction(Consumer<EntityManager> function){
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction et = null;
		logger.debug("Entity manager open");
		try{
			et = em.getTransaction();
			et.begin();
			function.accept(em);
			et.commit();
		} catch(Throwable e){
			e.printStackTrace();
			if (et != null && et.isActive()){
				et.rollback();
			}
		} finally{
			em.close();
			logger.debug("EntityManager closed");
		}
	}
	public static void close(){
		if (entityManager != null && entityManager.isOpen()){
			entityManager.clear();
			entityManager.close();
		}
		entityManagerFactory.close();
		entityManager = null;
		entityManagerFactory = null;
	}
}

