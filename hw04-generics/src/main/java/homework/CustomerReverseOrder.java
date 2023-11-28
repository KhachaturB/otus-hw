package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> list = new LinkedList<>();

    public void add(Customer customer) {
        list.add(customer);
    }

    public Customer take() {
        return list.pollLast();
    }
}
