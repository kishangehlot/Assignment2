package models;

/**
 * This class is for Zomato app
 */
public class Zomato {
    int orders;


    public Zomato(int orders) {
        this.orders = orders;
    }

    /**
     *
     * @returns the number of orders on Zomato App
     */
    @annotations.MethodDocumentation
    public int getNumOfZomatoOrders() {
        return this.orders;
    }

    /**
     *
     * @param orderName
     * @return total after placing an order on Zomato
     */
    @annotations.MethodDocumentation
    public int placeZomatoOrder(String orderName) {
        System.out.println("Placing your Zomato order");

        return ++orders;
    }

    /**
     *
     * @param groceries
     */
    public void buyGroceries(String[] groceries) {
        System.out.println("You're about to buy" + groceries.length);
    }
}
