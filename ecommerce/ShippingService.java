package ecommerce;

import java.util.List;

public class ShippingService {
    public static void shipItems(List<Shippable> items) {
        if (items.isEmpty()) {
            System.out.println("No items to ship.");
            return;
        }

        System.out.println("** Shipment notice **");
        double totalWeight = 0.0;

        for (Shippable item : items) {
            System.out.println(item.getName() + " " + item.getWeight() + "kg");
            totalWeight += item.getWeight();
        }

        System.out.printf("Total package weight %.2fkg%n", totalWeight);
    }
}
