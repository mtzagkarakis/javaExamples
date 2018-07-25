package manos.examples.jpaExamples;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import manos.examples.jpaExamples.model.CarNameWithMaxLength;
import manos.examples.jpaExamples.model.EntityManagerUtil;



public class PersistExamples {
	private static final Logger logger = LogManager.getLogger();
	EntityManager em;
	@BeforeClass
	public static void beforeClass(){
		EntityManagerUtil.init("defaultPersistenceUnitForUnitTests");	
	}
	@AfterClass
	public static void afterClass(){
		EntityManagerUtil.close();
	}
	@Before
	public void before(){
		EntityManagerUtil.doInJPAInTransaction(em->{
			em.createQuery("delete from CarNameWithMaxLength").executeUpdate();
		});
	}
	
	/**
	 * When merging an entity without a transaction IT WILL NOT THROW and exception
	 * and the entity will not persist. NO SQL statement is executed
	 */
	@Test
	public void merge_entity_without_transaction(){
		EntityManagerUtil.doInJPA(em->{
			logger.debug("merge_entity_without_transaction");
			em.merge(new CarNameWithMaxLength("ok_length"));
		});
		
		EntityManagerUtil.doInJPA(em->{
			List<CarNameWithMaxLength> list = em.createQuery("select carML from CarNameWithMaxLength carML where carML.name=:name_attr", CarNameWithMaxLength.class)
					.setParameter("name_attr", "ok_length")
					.getResultList();
			Assert.assertEquals(0, list.size());
		});
	}
	
	/**
	 * When merge in transaction the value will be persisted as expected
	 */
	@Test
	public void merge_entity_with_transaction(){
		EntityManagerUtil.doInJPAInTransaction(em->{
			logger.debug("merge_entity_with_transaction");
			em.merge(new CarNameWithMaxLength("ok_length"));
		});
		
		EntityManagerUtil.doInJPA(em->{
			List<CarNameWithMaxLength> list = em.createQuery("select carML from CarNameWithMaxLength carML where carML.name=:name_attr", CarNameWithMaxLength.class)
					.setParameter("name_attr", "ok_length")
					.getResultList();
			Assert.assertEquals(1, list.size());
		});
	}
	
	@Test
	public void merge_entity_with_error(){
		EntityManagerUtil.doInJPA(em->{
			String name = Stream.generate(()->"!").limit(CarNameWithMaxLength.nameMaxLength+1).reduce((str0, str1)->str0+str1).orElse("");
			logger.debug("merge_entity_with_error, name attr: " + name);
			EntityTransaction et = null;
			try{
				et = em.getTransaction();
				et.begin();
				em.merge(new CarNameWithMaxLength(name));
				et.commit();
			} catch (Throwable e) {
				if (et != null && et.isActive())
					et.rollback();
			}
		});
		
		EntityManagerUtil.doInJPA(em->{
			List<CarNameWithMaxLength> list = em.createQuery("select carML from CarNameWithMaxLength carML", CarNameWithMaxLength.class)
					.getResultList();
			Assert.assertEquals(0, list.size());
		});
	}
	
	@Test
	public void merge_entity_in_the_same_entityManager_after_error(){
		EntityManagerUtil.doInJPA(em->{
			String nameWithInvalidLength = Stream.generate(()->"!").limit(CarNameWithMaxLength.nameMaxLength+1).reduce((str0, str1)->str0+str1).orElse("");
			logger.debug("merge_entity_in_the_same_entityManager_after_error, name attr: " + nameWithInvalidLength);
			EntityTransaction et = null;
			try{
				et = em.getTransaction();
				et.begin();
				em.merge(new CarNameWithMaxLength(nameWithInvalidLength));
				et.commit();
			} catch (Throwable e) {
				if (et != null && et.isActive())
					et.rollback();
			}
			//second transaction with the same entity manager after transaction error
			//the entity will persist
			et = null;
			CarNameWithMaxLength carNameWithMaxLength = null;
			try{
				et = em.getTransaction();
				et.begin();
				carNameWithMaxLength = em.merge(new CarNameWithMaxLength("ok_length"));
				et.commit();
			} catch (Throwable e) {
				if (et != null && et.isActive())
					et.rollback();
			}
			
			et = null;
			try{
				et = em.getTransaction();
				et.begin();
				carNameWithMaxLength.setName("ok_length_2");//ok
				carNameWithMaxLength.setName("ok_length_3");//ok
				carNameWithMaxLength.setName("ok_length_4");//ok
				et.commit();
			} catch (Throwable e) {
				if (et != null && et.isActive())
					et.rollback();
			}
			
			et = null;
			try{
				et = em.getTransaction();
				et.begin();
				carNameWithMaxLength.setName("ok_length_5");//ok
				carNameWithMaxLength.setName("ok_length_6");//ok
				carNameWithMaxLength.setName(nameWithInvalidLength);//not_ok
				et.commit();
			} catch (Throwable e) {
				if (et != null && et.isActive())
					et.rollback();
			}
		});
		
		EntityManagerUtil.doInJPA(em->{
			List<CarNameWithMaxLength> list = em.createQuery("select carML from CarNameWithMaxLength carML", CarNameWithMaxLength.class)
					.getResultList();
			Assert.assertEquals(1, list.size());
			logger.debug("Name: " + list.get(0).getName());
			Assert.assertEquals("ok_length_4", list.get(0).getName());
		});
	}
}
