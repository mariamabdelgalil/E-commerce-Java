package ecommerce;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create products
        Product cheese = new ExpirableProduct("Cheese", 100, 5, LocalDate.of(2025, 7, 20), 0.4);
        Product biscuits = new ExpirableProduct("Biscuits", 150, 8, LocalDate.of(2025, 7, 10), 0.7);
        Product tv = new NonExpirableProduct("TV", 300, 2, true, 5.0);
        Product scratchCard = new NonExpirableProduct("Scratch Card", 50, 10, false, 0.0);

        // Create customer
        Customer customer = new Customer("Ali", 5000);

        // Create cart and add items
        Cart cart = new Cart();
        try {
            cart.addProduct(cheese, 2);
            cart.addProduct(biscuits, 4);
            cart.addProduct(tv, 1);
            cart.addProduct(scratchCard, 1);
        } catch (Exception e) {
            System.out.println("Add to cart error: " + e.getMessage());
            return;
        }

        // Checkout
        try {
            checkout(customer, cart);
        } catch (Exception e) {
            System.out.println("Checkout error: " + e.getMessage());
        }
    }

    public static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        double subtotal = cart.getSubtotal();
        double shippingCost = 0.0;
        List<Shippable> itemsToShip = new ArrayList<>();

        // Validate all cart items
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            if (product.isExpired()) {
                throw new IllegalStateException(product.getName() + " is expired.");
            }

            if (item.getQuantity() > product.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for: " + product.getName());
            }

            if (product.requiresShipping() && product instanceof Shippable) {
                itemsToShip.add((Shippable) product);
                shippingCost += ((Shippable) product).getWeight() * 10; // Example: 10 EGP per kg
            }
        }

        double totalAmount = subtotal + shippingCost;

        if (customer.getBalance() < totalAmount) {
            throw new IllegalStateException("Insufficient balance.");
        }

        // Deduct quantity from products
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceQuantity(item.getQuantity());
        }

        // Deduct from customer balance
        customer.deduct(totalAmount);

        // Ship items
        if (!itemsToShip.isEmpty()) {
            ShippingService.shipItems(itemsToShip);
        }

        // Print receipt
        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            System.out.printf("%dx %s %.2f%n", item.getQuantity(), item.getProduct().getName(), item.getTotalPrice());
        }
        System.out.println("----------------------");
        System.out.printf("Subtotal %.2f%n", subtotal);
        System.out.printf("Shipping %.2f%n", shippingCost);
        System.out.printf("Amount %.2f%n", totalAmount);
        System.out.printf("Customer balance after payment: %.2f%n", customer.getBalance());
    }
}
