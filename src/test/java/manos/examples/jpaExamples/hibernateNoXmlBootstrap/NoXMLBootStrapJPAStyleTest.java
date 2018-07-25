package manos.examples.jpaExamples.hibernateNoXmlBootstrap;

import org.hibernate.Session;
import org.junit.*;

import javax.persistence.*;
import java.util.List;

public class NoXMLBootStrapJPAStyleTest {
    @Entity(name = "SampleEntityClass")
    public class SampleEntityClass{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        @Column(name = "attribute")
        private String attribute;

        public SampleEntityClass(String attribute) {
            this.attribute = attribute;
        }

        public int getId() {
            return id;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        @Override
        public String toString() {
            return "SampleEntityClass{" +
                    "id=" + id +
                    ", attribute='" + attribute + '\'' +
                    '}';
        }
    }


    private static EntityManagerFactory factory;
    @BeforeClass
    public static void before(){
        factory = BootstrapH2InMemDatabase.createEntityManagerFactory(NoXMLBootStrapJPAStyleTest.class.getSimpleName(), new Class[]{SampleEntityClass.class});
    }

    @AfterClass
    public static void after(){
        if (factory != null && factory.isOpen())
            factory.close();
        factory = null;
    }

    @Test
    public void persistSingleEntity(){
        SampleEntityClass sampleEntityClass1 = new SampleEntityClass("an attribute1");
        SampleEntityClass sampleEntityClass2 = new SampleEntityClass("an attribute2");
        EntityManager em = factory.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(sampleEntityClass1);
            em.persist(sampleEntityClass2);

            em.flush();

            List<SampleEntityClass> result = em.unwrap(Session.class)
                                                .createQuery("select sample from SampleEntityClass sample", SampleEntityClass.class)
                                                .getResultList();

            Assert.assertEquals(2, result.size());
            Assert.assertEquals(1, result.get(0).getId());
            Assert.assertEquals("an attribute1", result.get(0).getAttribute());
            Assert.assertEquals(2, result.get(1).getId());
            Assert.assertEquals("an attribute2", result.get(1).getAttribute());
            et.commit();
        } catch (Exception e){
            if (et != null && et.isActive())
                et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
