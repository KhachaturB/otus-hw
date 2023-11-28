package homework;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    static final class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            var oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private final TreeMap<Customer, String> map = new TreeMap<>(this::compareCustomers);

    private int compareCustomers(Customer customer1, Customer customer2) {
        return Long.compare(customer1.getScores(), customer2.getScores());
    }

    private Entry<Customer, String> copyEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }

        var key = entry.getKey();
        var value = entry.getValue();
        return new Entry<>(new Customer(key.getId(), key.getName(), key.getScores()), value);
    }

    public Map.Entry<Customer, String> getSmallest() {
        return copyEntry(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyEntry(map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
