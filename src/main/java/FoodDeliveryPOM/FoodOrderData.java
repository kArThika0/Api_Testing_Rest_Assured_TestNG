package FoodDeliveryPOM;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderData {

   public FoodOrderRequest createSampleOrder() {
        FoodOrderRequest request = new FoodOrderRequest();
        request.setRestaurantId("REST-ZOM-9981");
        request.setCustomerId("USR-44512");

        // Create order items
        List<FoodOrderRequest.OrderItem> items = new ArrayList<>();

        FoodOrderRequest.OrderItem item1 = new FoodOrderRequest.OrderItem();
        item1.setItemId("ITEM-001");
        item1.setName("Paneer Butter Masala");
        item1.setQty(2);
        item1.setPrice(280);
        items.add(item1);

        FoodOrderRequest.OrderItem item2 = new FoodOrderRequest.OrderItem();
        item2.setItemId("ITEM-034");
        item2.setName("Garlic Naan");
        item2.setQty(4);
        item2.setPrice(60);
        items.add(item2);

        request.setItems(items);

        // Set delivery address
        FoodOrderRequest.DeliveryAddress address = new FoodOrderRequest.DeliveryAddress();
        address.setLat(12.9716);
        address.setLng(77.5946);
        address.setLabel("Home");
        address.setFullAddress("12 Indiranagar, Bangalore");
        request.setDeliveryAddress(address);

        // Set promo and payment
        request.setPromoCode("FLAT50");
        request.setPaymentMode("wallet");

        return request;
    }
}
