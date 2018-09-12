package cree.mvp.util.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/20  16:15
 *
 * @author cree
 * @version 1.0
 */
public class MapUtil<K, V> implements Cloneable {

    private final HashMap<K, V> hashMap;


    public MapUtil() {
        hashMap = new HashMap<>();
    }

    public static <T, E> MapUtil<T, E> getMap() {
        return new MapUtil<>();
    }

    public MapUtil<K, V> put(K key, V value) {
        hashMap.put(key, value);
        return this;
    }

    public HashMap<K, V> map() {
        return hashMap;
    }


    public V get(K k) {
        return hashMap.get(k);
    }

    public void clear() {
        hashMap.clear();
    }

    public int size() {
        return hashMap.size();
    }

    public int keySize() {
        return hashMap.keySet().size();
    }


    @Override
    public MapUtil<K, V> clone() {
        Set<K> ks = hashMap.keySet();
        Iterator<K> iterator = ks.iterator();

        MapUtil<K, V> kvMapUtil = MapUtil.getMap();
        while (iterator.hasNext()) {
            K k = iterator.next();
            V v = hashMap.get(k);
            kvMapUtil.put(k, v);
        }
        return kvMapUtil;
    }

    @Override
    public String toString() {
        return hashMap.toString();

    }
}
