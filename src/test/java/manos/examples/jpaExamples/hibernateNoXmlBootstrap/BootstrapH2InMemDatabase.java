package manos.examples.jpaExamples.hibernateNoXmlBootstrap;

import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class BootstrapH2InMemDatabase {
    private static Properties configureProperties(Map<String, String> extraProps){
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:testDB;MODE=PostgreSQL");
        properties.put("javax.persistence.jdbc.username", "");
        properties.put("javax.persistence.jdbc.password", "");
        properties.put("hibernate.connection.autocommit", "false");
        properties.putAll(extraProps);
        return properties;
    }

    private static java.util.List<String> getEntitiesClassNames(Class[] classes){
        return Arrays.asList(classes)
                .stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    private static PersistenceUnitInfo getPersistenceUnitInfoImpl(String name, Class[] entityClasses, Map<String, String> extraProps){
        return new PersistenceUnitInfoImpl(name, getEntitiesClassNames(entityClasses), configureProperties(extraProps));
    }

    public static EntityManagerFactory createEntityManagerFactory(String name, Class[] entityClasses){
        return createEntityManagerFactory(name, entityClasses, new HashMap<>());
    }

    public static EntityManagerFactory createEntityManagerFactory(String name, Class[] entityClasses, Map<String, String> extraProperties){
        PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfoImpl(name, entityClasses, extraProperties);
        Map<String, Object> configurationMap = new HashMap<>();
        //configurationMap.put(AvailableSettings.INTERCEPTOR, )
        return new EntityManagerFactoryBuilderImpl(
                new PersistenceUnitInfoDescriptor(persistenceUnitInfo),
                configurationMap)
                .build();
    }
}
