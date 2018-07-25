package manos.examples.customLRUCache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

//from https://stackoverflow.com/questions/221525/how-would-you-implement-an-lru-cache-in-java/1953516#1953516
public class LRUCache<K, L> extends LinkedHashMap<K, L> {

    private final int maxEntries;

    private LRUCache(final int maxEntries){
        super(maxEntries + 1, 1.0f, true);
        this.maxEntries = maxEntries;
    }

    public static <K, L> Map getInstance(int maxEntries){
        return Collections.synchronizedMap(new LRUCache<K, L>(maxEntries));
    }

    /**
     * Returns <tt>true</tt> if this <code>LruCache</code> has more entries than the maximum specified when it was
     * created.
     *
     * <p>
     * This method <em>does not</em> modify the underlying <code>Map</code>; it relies on the implementation of
     * <code>LinkedHashMap</code> to do that, but that behavior is documented in the JavaDoc for
     * <code>LinkedHashMap</code>.
     * </p>
     *
     * @param eldest
     *            the <code>Entry</code> in question; this implementation doesn't care what it is, since the
     *            implementation is only dependent on the size of the cache
     * @return <tt>true</tt> if the oldest
     * @see java.util.LinkedHashMap#removeEldestEntry(Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, L> eldest) {
        return super.size() > maxEntries;
    }
}
