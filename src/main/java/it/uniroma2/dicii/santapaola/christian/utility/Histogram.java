package it.uniroma2.dicii.santapaola.christian.utility;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * A it.uniroma2.dicii.santapaola.christian.utility.Histogram is a Data Structure that given a Type K that must implements equals and Comparable interface
 * Given an Object K, it tells how many Objects J so that J == K were inserted inside the it.uniroma2.dicii.santapaola.christian.utility.Histogram.
 * @param <K> Type K that must implements equals and Comparable interface
 */
public class Histogram<K> {
    private Map<K, Integer> integerMap;

    /**
     * Constructor of a it.uniroma2.dicii.santapaola.christian.utility.Bucket.
     */
    public Histogram() {
        integerMap = new TreeMap<>();
    }

    /**
     * insert key inside the bucket.
     * if the key was inserted before, add the value associated with it by 1.
     * if the key was not inserted before, create a new pair (key, 1).
     * @param key
     */
    public void insert(K key) {
        if (integerMap.containsKey(key)) {
            int value = integerMap.get(key);
            integerMap.put(key, value + 1);
        } else {
            integerMap.put(key, 1);
        }
    }

    /**
     * retrieve the number of time key was inserted inside the bucket.
     * @param key
     * @return
     */
    public int retrieve(K key) {
        if (integerMap.containsKey(key)) {
            return integerMap.get(key);
        } else {
            return 0;
        }
    }

    /**
     * return a set containg all key inserted inside the bucket.
     * @return
     */
    public Set<K> getKeys() {
        return integerMap.keySet();
    }

}
