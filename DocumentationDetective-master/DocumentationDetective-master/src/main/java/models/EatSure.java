package models;

import annotations.ClassDocumentation;
import annotations.MethodDocumentation;

/**
 * This class is for EatSure Shopping App
 */
@ClassDocumentation
public class EatSure {
    int orders;

    public EatSure(int orders) {
        this.orders = orders;
    }

    @MethodDocumentation
    public int getNumOfEatSureOrders() {
        return this.orders;
    }

    @MethodDocumentation(documentation = "This method places an order on the EatSure App")
    public int placeMyntraOrder(String orderName) {
        System.out.println("Placing your EatSure order" + orderName);
        return ++orders;
    }

    /**
     * This method searches the EatSure databases for products that match your item
     * @param photo
     */
    @MethodDocumentation(documentation = "This method scans a photo to find matching items from the EatSure")
    public void searchWithCamera(String photo) {
        System.out.println("Trying to search our database for products that match your item");
    }
}