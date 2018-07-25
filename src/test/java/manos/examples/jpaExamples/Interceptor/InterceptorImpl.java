package manos.examples.jpaExamples.Interceptor;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InterceptorImpl implements Interceptor, Serializable{
    private static final ConcurrentMap<Long, String> messageMap = new ConcurrentHashMap<>();
    private static final AtomicLong counter = new AtomicLong(0);
    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Load." +
                "\n\tObject: " + entity +
                "\n\tid: " + id +
                "\n\tstate: " + Arrays.stream(state).map(Object::toString).collect(Collectors.joining(",")) +
                "\n\tproperty names: " + Arrays.stream(propertyNames).collect(Collectors.joining(",")) +
                "\n\ttypes: " + Arrays.stream(types).map(Type::getName).collect(Collectors.joining(",")));
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Flush Dirty." +
                "\n\tObject: " + entity +
                "\n\tid: " + id +
                "\n\tcurrent state: " + Arrays.stream(currentState).map(Object::toString).collect(Collectors.joining(",")) +
                "\n\tprevious state: " + Arrays.stream(previousState).map(Object::toString).collect(Collectors.joining(",")) +
                "\n\tproperty names: " + Arrays.stream(propertyNames).collect(Collectors.joining(",")) +
                "\n\ttypes: " + Arrays.stream(types).map(Type::getName).collect(Collectors.joining(",")));
        return false;
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        messageMap.put(counter.getAndAdd(1),"Find Dirty." +
                "\n\tObject: " + entity +
                "\n\tid: " + id +
                "\n\tcurrent state: " + Arrays.stream(currentState).map(Object::toString).collect(Collectors.joining(",")) +
                "\n\tprevious state: " + Arrays.stream(previousState).map(Object::toString).collect(Collectors.joining(",")) +
                "\n\tproperty names: " + Arrays.stream(propertyNames).collect(Collectors.joining(",")) +
                "\n\ttypes: " + Arrays.stream(types).map(Type::getName).collect(Collectors.joining(",")));
        return null;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Save." +
                "\n\tObject: " + entity +
                "\n\tid: " + id +
                "\n\tstate: " + Arrays.stream(state).map(Object::toString).collect(Collectors.joining(",")) +
                "\n\tproperty names: " + Arrays.stream(propertyNames).collect(Collectors.joining(",")) +
                "\n\ttypes: " + Arrays.stream(types).map(Type::getName).collect(Collectors.joining(",")));
        return false;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Delete");
    }

    @Override
    public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Collection Recreate");
    }

    @Override
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Collection Remove");
    }

    @Override
    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"On Collection Update");
    }

    @Override
    public void preFlush(Iterator entities) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"Pre Flush.");
    }

    @Override
    public void postFlush(Iterator entities) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"Post Flush");
    }

    @Override
    public Boolean isTransient(Object entity) {
        messageMap.put(counter.getAndAdd(1),"Is Transient object: " + entity.toString());
        return null;
    }

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"Instantiate entity with name: " + entityName + " mode: " + entityMode + " and id: "+ id);
        return null;
    }

    @Override
    public String getEntityName(Object object) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"Get Entity Name for Object: " + object);
        return null;
    }

    @Override
    public Object getEntity(String entityName, Serializable id) throws CallbackException {
        messageMap.put(counter.getAndAdd(1),"Get Entity for Entity with Name: " + entityName + " and id " + id);
        return null;
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        messageMap.put(counter.getAndAdd(1),"After Transaction Begin");
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        messageMap.put(counter.getAndAdd(1),"Before Transaction Complete");
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        messageMap.put(counter.getAndAdd(1),"After Transaction Complete");
    }

    @Override
    public String onPrepareStatement(String sql) {
        messageMap.put(counter.getAndAdd(1),"On prepare Statement. SQL: " + sql);
        return null;
    }
    public static void clear(){
        counter.set(0);
        messageMap.clear();
    }
    public static Set<Map.Entry<Long, String>> entrySet(){
        return messageMap.entrySet();
    }
}
