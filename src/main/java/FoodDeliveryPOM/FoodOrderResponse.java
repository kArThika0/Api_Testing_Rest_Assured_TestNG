package FoodDeliveryPOM;

import java.util.List;

public class FoodOrderResponse {

    private String orderId;
    private String status;
    private String restaurant;
    private String estimatedDelivery;
    private DeliveryPartner deliveryPartner;
    private Bill bill;
    private List<String> errors;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(String estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public static class DeliveryPartner {
        private String name;
        private String phone;
        private double rating;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }
    }

    public static class Bill {
        private double subtotal;
        private double promoDiscount;
        private double deliveryFee;
        private double taxes;
        private double total;

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }

        public double getPromoDiscount() {
            return promoDiscount;
        }

        public void setPromoDiscount(double promoDiscount) {
            this.promoDiscount = promoDiscount;
        }

        public double getDeliveryFee() {
            return deliveryFee;
        }

        public void setDeliveryFee(double deliveryFee) {
            this.deliveryFee = deliveryFee;
        }

        public double getTaxes() {
            return taxes;
        }

        public void setTaxes(double taxes) {
            this.taxes = taxes;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }
}

