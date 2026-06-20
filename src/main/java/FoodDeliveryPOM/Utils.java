package FoodDeliveryPOM;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    /**
     * Generate a unique order ID in the format FDO-YYYYMMDD-XXXXX
     * @return Order ID
     */
    public static String generateOrderId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(new Date());
        int randomNum = (int) (Math.random() * 90000) + 10000; // Generates 5-digit random number
        return "FDO-" + dateStr + "-" + randomNum;
    }

    /**
     * Validate if delivery address is within a specified radius
     * Uses Haversine formula for distance calculation
     * @param lat1 Restaurant latitude
     * @param lon1 Restaurant longitude
     * @param lat2 Delivery latitude
     * @param lon2 Delivery longitude
     * @param radiusInKm Maximum delivery radius in kilometers
     * @return true if delivery address is within radius
     */
    public static boolean isWithinDeliveryRadius(double lat1, double lon1, double lat2, double lon2, double radiusInKm) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= radiusInKm;
    }

    /**
     * Calculate distance between two coordinates using Haversine formula
     * @param lat1 Latitude 1
     * @param lon1 Longitude 1
     * @param lat2 Latitude 2
     * @param lon2 Longitude 2
     * @return Distance in kilometers
     */
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in km
    }

    /**
     * Calculate bill total from components
     * @param subtotal Subtotal amount
     * @param promoDiscount Promo discount amount
     * @param deliveryFee Delivery fee
     * @param taxes Tax amount
     * @return Total bill amount
     */
    public static double calculateBillTotal(double subtotal, double promoDiscount, double deliveryFee, double taxes) {
        return subtotal + deliveryFee + taxes - promoDiscount;
    }

    /**
     * Validate order ID format
     * @param orderId Order ID to validate
     * @return true if order ID matches pattern FDO-YYYYMMDD-XXXXX
     */
    public static boolean isValidOrderId(String orderId) {
        return orderId != null && orderId.matches("^FDO-\\d{8}-\\d{5}$");
    }

    /**
     * Validate phone number format
     * @param phone Phone number to validate
     * @return true if phone number matches pattern +91-10 digits
     */
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\+91-\\d{10}$");
    }
}
