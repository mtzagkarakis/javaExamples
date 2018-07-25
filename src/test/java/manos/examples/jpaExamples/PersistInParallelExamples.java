package manos.examples.jpaExamples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

public class PersistInParallelExamples {
	private static final Logger logger = LogManager.getLogger();
	static String nameWithInvalidLength = Stream.generate(()->"!").limit(CarNameWithMaxLength.nameMaxLength+1).reduce((str0, str1)->str0+str1).orElse("");
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
	
	private class MyThread extends Thread{
		private final String var;
		public MyThread(String var){
			this.var = var;
		}
		@Override
		public void run(){
			EntityManagerUtil.doInJPA(em->{
				logger.debug("merge_entity_in_parallel, name attr: " + nameWithInvalidLength);
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
					carNameWithMaxLength.setName(var);//ok
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
		}
	}
	
	/**
	 * Each Thread will persist a new carNameWithMaxLength
	 * then it will change the name to MyThread.var;
	 */
	@Test
	public void persist_entities_in_parallel(){
		List<Thread> threads = new ArrayList<>();
		for (int i=0; i<3; i++){
			threads.add(new MyThread("agent-"+i));
			threads.get(i).start();
		}
		
		for (int i=0; i<3; i++){
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		EntityManagerUtil.doInJPA(em->{
			List<CarNameWithMaxLength> list = em.createQuery("select carML from CarNameWithMaxLength carML", CarNameWithMaxLength.class)
					.getResultList();
			Assert.assertEquals(3, list.size());
			Set<String> varsAfterPersist = list.stream().map(CarNameWithMaxLength::getName).collect(Collectors.toCollection(HashSet::new));
			logger.debug("varsAfterPersist: " + varsAfterPersist.toString());
			Assert.assertTrue(varsAfterPersist.contains("agent-0"));
			Assert.assertTrue(varsAfterPersist.contains("agent-1"));
			Assert.assertTrue(varsAfterPersist.contains("agent-2"));
		});
		
	}
}
