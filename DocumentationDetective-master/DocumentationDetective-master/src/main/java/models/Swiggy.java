package models;

import annotations.ClassDocumentation;
import annotations.MethodDocumentation;

/**
 * This class is for Swiggy Shopping App
 */
@ClassDocumentation
public class Swiggy {
    int orders;

    public Swiggy(int orders) {
        this.orders = orders;
    }

    /**
     * This method returns the number of orders thet Swiggy shopping app has received yet
     * @return number of orders
     */
    @MethodDocumentation
    public int getNumOfSwiggyOrders() {
        return this.orders;
    }

    /**
     * This method places an order on the Swiggy App
     * @param orderName
     * @return total orders
     */
    @MethodDocumentation
    public int placeSwiggyOrder(String orderName) {
        System.out.println("Placing your Swiggy order" + orderName);

        return ++orders;
    }

}
