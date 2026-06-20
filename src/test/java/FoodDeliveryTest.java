import FoodDeliveryPOM.FoodOrderData;
import FoodDeliveryPOM.FoodOrderRequest;
import FoodDeliveryPOM.FoodOrderResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FoodDeliveryTest extends FoodDeliveryBaseTest {

    /*
     * Test Scenario: Food Delivery Order API
     * POST /api/v1/food-orders
     * Validates:
     * 1. Restaurant availability
     * 2. Item existence
     * 3. Delivery radius
     * 4. Delivery partner assignment
     */

    //Instantiating test data class
    FoodOrderData data = new FoodOrderData();
    // ============== POSITIVE TEST CASES ==============

    @Test(groups = {"positive"})
    public void testSuccessfulFoodOrderCreation() {
        // Setup stub for successful order creation
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": "FDO-20260609-77123",
                                          "status": "ACCEPTED",
                                          "restaurant": "Punjabi Tadka",
                                          "estimatedDelivery": "35 mins",
                                          "deliveryPartner": {
                                            "name": "Suresh M",
                                            "phone": "+91-9900123456",
                                            "rating": 4.8
                                          },
                                          "bill": {
                                            "subtotal": 800,
                                            "promoDiscount": 50,
                                            "deliveryFee": 30,
                                            "taxes": 42,
                                            "total": 822
                                          },
                                          "errors": []
                                        }
                                        """)
                        )
        );


        FoodOrderRequest requestData = data.createSampleOrder();

        // Send request and validate
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestData)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/foodDelivery.json"))
                .log().all()
                .extract()
                .response();

        // Parse response
        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);

        // Validate response fields
        Assert.assertEquals(orderResponse.getStatus(), "ACCEPTED", "Order status should be ACCEPTED");
        Assert.assertEquals(orderResponse.getRestaurant(), "Punjabi Tadka", "Restaurant name mismatch");
        Assert.assertNotNull(orderResponse.getOrderId(), "Order ID should not be null");
        Assert.assertTrue(orderResponse.getOrderId().startsWith("FDO-"), "Order ID should start with FDO-");

        // Validate delivery partner assignment
        Assert.assertNotNull(orderResponse.getDeliveryPartner(), "Delivery partner should be assigned");
        Assert.assertEquals(orderResponse.getDeliveryPartner().getName(), "Suresh M", "Delivery partner name mismatch");
        Assert.assertTrue(orderResponse.getDeliveryPartner().getRating() >= 4.0, "Delivery partner rating should be >= 4.0");

        // Validate bill calculation
        Assert.assertEquals(orderResponse.getBill().getSubtotal(), 800, "Subtotal mismatch");
        Assert.assertEquals(orderResponse.getBill().getPromoDiscount(), 50, "Promo discount mismatch");
        double expectedTotal = 800 + 30 + 42 - 50; // subtotal + delivery + taxes - discount
        Assert.assertEquals(orderResponse.getBill().getTotal(), expectedTotal, "Total bill calculation mismatch");
    }

    @Test(groups = {"positive"})
    public void testOrderCreationWithoutPromoCode() {
        // Setup stub for order without promo code
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": "FDO-20260610-45678",
                                          "status": "ACCEPTED",
                                          "restaurant": "Biryani House",
                                          "estimatedDelivery": "40 mins",
                                          "deliveryPartner": {
                                            "name": "Ramesh K",
                                            "phone": "+91-9876543210",
                                            "rating": 4.5
                                          },
                                          "bill": {
                                            "subtotal": 500,
                                            "promoDiscount": 0,
                                            "deliveryFee": 40,
                                            "taxes": 25,
                                            "total": 565
                                          },
                                          "errors": []
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        request.setPromoCode(null); // No promo code

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(201)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertEquals(orderResponse.getBill().getPromoDiscount(), 0, "Promo discount should be 0");
        Assert.assertEquals(orderResponse.getStatus(), "ACCEPTED", "Order should be accepted without promo");
    }

    @Test(groups = {"positive"})
    public void testOrderWithMultipleItems() {
        // Setup stub for order with multiple items
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": "FDO-20260611-99876",
                                          "status": "ACCEPTED",
                                          "restaurant": "Multi Cuisine",
                                          "estimatedDelivery": "45 mins",
                                          "deliveryPartner": {
                                            "name": "Priya S",
                                            "phone": "+91-8899776655",
                                            "rating": 4.9
                                          },
                                          "bill": {
                                            "subtotal": 1500,
                                            "promoDiscount": 150,
                                            "deliveryFee": 50,
                                            "taxes": 75,
                                            "total": 1475
                                          },
                                          "errors": []
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        FoodOrderRequest.OrderItem item3 = new FoodOrderRequest.OrderItem();
        item3.setItemId("ITEM-045");
        item3.setName("Mango Lassi");
        item3.setQty(2);
        item3.setPrice(100);
        request.getItems().add(item3);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(201)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertEquals(orderResponse.getStatus(), "ACCEPTED", "Order with multiple items should be accepted");
        Assert.assertTrue(orderResponse.getBill().getSubtotal() >= 1500, "Subtotal should reflect all items");
    }

    // ============== NEGATIVE TEST CASES ==============

    @Test(groups = {"negative"})
    public void testOrderWithRestaurantUnavailable() {
        // Restaurant is not available
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": null,
                                          "status": "REJECTED",
                                          "restaurant": null,
                                          "estimatedDelivery": null,
                                          "deliveryPartner": null,
                                          "bill": null,
                                          "errors": ["RESTAURANT_NOT_AVAILABLE"]
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        request.setRestaurantId("REST-INVALID-999");

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(400)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertEquals(orderResponse.getStatus(), "REJECTED", "Order should be rejected");
        Assert.assertTrue(orderResponse.getErrors().contains("RESTAURANT_NOT_AVAILABLE"), "Error should indicate restaurant unavailable");
    }

    @Test(groups = {"negative"})
    public void testOrderWithNonExistentItem() {
        // Item doesn't exist in restaurant
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": null,
                                          "status": "REJECTED",
                                          "restaurant": null,
                                          "estimatedDelivery": null,
                                          "deliveryPartner": null,
                                          "bill": null,
                                          "errors": ["ITEM_NOT_FOUND_ITEM-999"]
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        FoodOrderRequest.OrderItem invalidItem = new FoodOrderRequest.OrderItem();
        invalidItem.setItemId("ITEM-999");
        invalidItem.setName("Non-existent item");
        invalidItem.setQty(1);
        invalidItem.setPrice(999);
        request.getItems().add(invalidItem);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(400)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertEquals(orderResponse.getStatus(), "REJECTED", "Order should be rejected");
        Assert.assertTrue(orderResponse.getErrors().stream().anyMatch(e -> e.contains("ITEM_NOT_FOUND")), "Error should indicate item not found");
    }

    @Test(groups = {"negative"})
    public void testOrderOutsideDeliveryRadius() {
        // Delivery address is outside restaurant's delivery radius
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": null,
                                          "status": "REJECTED",
                                          "restaurant": null,
                                          "estimatedDelivery": null,
                                          "deliveryPartner": null,
                                          "bill": null,
                                          "errors": ["DELIVERY_OUTSIDE_RADIUS"]
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        // Set delivery address far away
        request.getDeliveryAddress().setLat(12.5);
        request.getDeliveryAddress().setLng(77.0);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(400)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertEquals(orderResponse.getStatus(), "REJECTED", "Order should be rejected");
        Assert.assertTrue(orderResponse.getErrors().contains("DELIVERY_OUTSIDE_RADIUS"), "Error should indicate delivery outside radius");
    }

    @Test(groups = {"negative"})
    public void testOrderWithInvalidPaymentMode() {
        // Invalid or unsupported payment mode
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": null,
                                          "status": "REJECTED",
                                          "restaurant": null,
                                          "estimatedDelivery": null,
                                          "deliveryPartner": null,
                                          "bill": null,
                                          "errors": ["INVALID_PAYMENT_MODE"]
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        request.setPaymentMode("CRYPTO");

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(400)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertTrue(orderResponse.getErrors().contains("INVALID_PAYMENT_MODE"), "Error should indicate invalid payment mode");
    }

    @Test(groups = {"negative"})
    public void testOrderNoDeliveryPartnerAvailable() {
        // No delivery partner available to accept the order
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .willReturn(aResponse()
                                .withStatus(503)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": null,
                                          "status": "REJECTED",
                                          "restaurant": null,
                                          "estimatedDelivery": null,
                                          "deliveryPartner": null,
                                          "bill": null,
                                          "errors": ["NO_DELIVERY_PARTNER_AVAILABLE"]
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(503)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertTrue(orderResponse.getErrors().contains("NO_DELIVERY_PARTNER_AVAILABLE"), "Error should indicate no delivery partner available");
    }

    @Test(groups = {"negative"})
    public void testOrderWithMissingRequiredFields() {
        // Missing required customer ID
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/food-orders"))
                        .withRequestBody(matchingJsonPath("$.customerId", absent()))
                        .willReturn(aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "orderId": null,
                                          "status": "REJECTED",
                                          "restaurant": null,
                                          "estimatedDelivery": null,
                                          "deliveryPartner": null,
                                          "bill": null,
                                          "errors": ["CUSTOMER_ID_REQUIRED"]
                                        }
                                        """)
                        )
        );

        FoodOrderRequest request = data.createSampleOrder();
        request.setCustomerId(null);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/food-orders")
                .then()
                .assertThat()
                .statusCode(400)
                .log().all()
                .extract()
                .response();

        FoodOrderResponse orderResponse = response.as(FoodOrderResponse.class);
        Assert.assertTrue(orderResponse.getErrors().contains("CUSTOMER_ID_REQUIRED"), "Error should indicate customer ID is required");
    }



}

