package manos.examples.jpaExamples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import manos.examples.jpaExamples.model.Car;
import manos.examples.jpaExamples.model.EntityManagerUtil;
import manos.examples.jpaExamples.model.Owner;
import manos.examples.jpaExamples.model.OwnerLazy;


public class jpaLazyLoadingTests {
	/**
	 * The persist, merge, remove, and refresh methods must be invoked within a transaction context when an entity manager 
	 * with a transaction-scoped persistence context is used. 
	 * If there is no transaction context, the javax.persistence.
	 * TransactionRequiredException is thrown. Methods that specify a lock mode other than LockModeType.NONE 
	 * must be invoked within a transaction context. 
	 * If there is no transaction context, the javax.persistence.TransactionRe- quiredException is thrown.
	 * The find method (provided it is invoked without a lock or invoked with LockModeType.NONE) 
	 * and the getReference method are not required to be invoked within a transaction context. 
	 * If an entity manager with transaction-scoped persistence context is in use, the resulting entities will be detached; 
	 * if an entity manager with an extended persistence context is used, they will be managed. 
	 * See section 3.3 for entity manager use outside a transaction.
	 */
	static List<Car> persistedCars = new ArrayList<>();
	static List<Owner> persistedOwners = new ArrayList<>();
	@BeforeClass
	public static void beforeClass(){
		EntityManagerUtil.init("defaultPersistenceUnitForUnitTests");	
		EntityManager em = EntityManagerUtil.getEntityManager();
		em.getTransaction().begin();
		persistedCars.add(em.merge(new Car("car1")));
		persistedCars.add(em.merge(new Car("car2")));
		persistedCars.add(em.merge(new Car("car3")));
		persistedCars.add(em.merge(new Car("car4")));
		persistedCars.add(em.merge(new Car("car5")));
		
		Owner owner1 = new Owner("owner1", Arrays.asList(persistedCars.get(0)));
		Owner owner2 = new Owner("owner2", Arrays.asList(persistedCars.get(1)));
		Owner owner3 = new Owner("owner3", Arrays.asList(persistedCars.get(2)));
		Owner owner4 = new Owner("owner4", Arrays.asList(persistedCars.get(3)));
		Owner owner5 = new Owner("owner5", Arrays.asList(persistedCars.get(4)));
		
		persistedOwners.add(em.merge(owner1));
		persistedOwners.add(em.merge(owner2));
		persistedOwners.add(em.merge(owner3));
		persistedOwners.add(em.merge(owner4));
		persistedOwners.add(em.merge(owner5));
		
		em.flush();
		em.getTransaction().commit();
		em.clear();
	}
	@AfterClass
	public static void afterClass(){
		EntityManagerUtil.close();
	}

	@Test
	public void lazyGetTest(){
		EntityManager em = EntityManagerUtil.getEntityManager();
		OwnerLazy owner1 = em.find(OwnerLazy.class, 1);
		String name = owner1.getCars().get(0).getName();
		Assert.assertEquals("car1", name);
		Assert.assertEquals("owner1", owner1.getName());
		/**
		 * result SQL
		 * Hibernate: 
		    select
		        owner0_.id as id1_2_0_,
		        owner0_.name as name2_2_0_ 
		    from
		        owners owner0_ 
		    where
		        owner0_.id=?
			Hibernate: 
			    select
			        cars0_.Owner_id as Owner_id1_1_0_,
			        cars0_.cars_id as cars_id2_1_0_,
			        car1_.id as id1_0_1_,
			        car1_.name as name2_0_1_ 
			    from
			        owner_cars cars0_ 
			    inner join
			        cars car1_ 
			            on cars0_.cars_id=car1_.id 
			    where
			        cars0_.Owner_id=?
		 */
	}
	
	@Test
	public void eagerGetTest(){
		EntityManager em = EntityManagerUtil.getEntityManager();
		Owner owner1 = em.find(Owner.class, 1);
		String name = owner1.getCars().get(0).getName();
		Assert.assertEquals("car1", name);
		Assert.assertEquals("owner1", owner1.getName());
		/**
		 * Hibernate: 
			    select
			        owner0_.id as id1_2_0_,
			        owner0_.name as name2_2_0_,
			        cars1_.owner_id as owner_id1_1_1_,
			        car2_.id as car_id2_1_1_,
			        car2_.id as id1_0_2_,
			        car2_.name as name2_0_2_ 
			    from
			        owners owner0_ 
			    left outer join
			        owner_cars cars1_ 
			            on owner0_.id=cars1_.owner_id 
			    left outer join
			        cars car2_ 
			            on cars1_.car_id=car2_.id 
			    where
			        owner0_.id=?
		 */
	}
}
