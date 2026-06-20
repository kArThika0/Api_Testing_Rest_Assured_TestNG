# API Testing Framework - Rest Assured + TestNG

A comprehensive **REST API testing automation framework** built with **RestAssured**, **TestNG**, and **WireMock**. This project demonstrates industry-standard API testing practices using the **Page Object Model (POM)** pattern, proper test organization, and end-to-end test scenarios.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture & Design Patterns](#architecture--design-patterns)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Test Scenarios](#test-scenarios)
- [Setup & Installation](#setup--installation)
- [Running Tests](#running-tests)
- [Key Features](#key-features)
- [Best Practices Demonstrated](#best-practices-demonstrated)
- [Interview Highlights](#interview-highlights)

---

## 🎯 Project Overview

This project is a **production-ready API automation framework** demonstrating comprehensive REST API testing across multiple domains:

1. **Food Delivery Order System** - Complex multi-factor order management
2. **Hospital Appointment Booking** - Healthcare domain with business validations
3. **User Management** - Core user CRUD operations

Each module includes:
- ✅ Positive test scenarios (happy path)
- ✅ Negative test scenarios (error cases)
- ✅ Data validation tests
- ✅ Business rule enforcement
- ✅ Schema validation

---

## 🏗️ Architecture & Design Patterns

### Page Object Model (POM)
The framework uses **POM** to separate test logic from test data and API interactions:

```
src/main/java/
├── FoodDeliveryPOM/
│   ├── FoodOrderRequest.java      # Request POJO (Data)
│   ├── FoodOrderResponse.java     # Response POJO (Data)
│   ├── FoodOrderData.java         # Test Data Builder
│   └── Utils.java                 # Helper utilities
├── UserManagementPOM/
│   └── UserPOJO.java              # User model
└── HospitalPOM/
    ├── AppointmentRequest.java    # Appointment request model
    ├── AppointmentResponse.java   # Appointment response model
    ├── AppointmentData.java       # Test data factory
    └── AppointmentUtils.java      # Business logic helpers
```

### Test Class Organization
```
src/test/java/
├── FoodDeliveryBaseTest.java      # Base test setup (WireMock server)
├── FoodDeliveryTest.java          # Food delivery API tests
├── UserManagementTest.java        # User management tests
└── HospitalAppointmentTest.java   # Hospital appointment tests
```

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **API Testing** | RestAssured | 5.x |
| **Test Framework** | TestNG | 7.x |
| **Mocking** | WireMock | 3.x |
| **Build Tool** | Maven | 3.8.x |
| **Language** | Java | 11+ |
| **JSON Processing** | Jackson | 2.x |
| **JSON Schema Validation** | JSON Schema Validator | 5.x |

### Maven Dependencies
```xml
<dependency>
    <groupId>io.restassured</groupId>
    <artifactId>rest-assured</artifactId>
</dependency>
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
</dependency>
<dependency>
    <groupId>com.github.tomakehurst</groupId>
    <artifactId>wiremock-jre8</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

---

## 📁 Project Structure

```
RestAssuredTestNGFramework/
│
├── src/
│   ├── main/java/
│   │   ├── FoodDeliveryPOM/
│   │   │   ├── FoodOrderRequest.java        # Food order req model
│   │   │   ├── FoodOrderResponse.java       # Food order resp model
│   │   │   ├── FoodOrderData.java           # Test data builder
│   │   │   └── Utils.java                   # Utility helpers
│   │   ├── UserManagementPOM/
│   │   │   └── UserPOJO.java                # User data model
│   │   └── HospitalPOM/
│   │       ├── AppointmentRequest.java      # Appointment request
│   │       ├── AppointmentResponse.java     # Appointment response
│   │       ├── AppointmentData.java         # Appointment test data
│   │       └── AppointmentUtils.java        # Business logic helpers
│   │
│   ├── main/resources/
│   │   └── schemas/
│   │       ├── foodDelivery.json            # JSON Schema validation
│   │       └── userManagement.json          # JSON Schema validation
│   │
│   └── test/java/
│       ├── FoodDeliveryBaseTest.java        # Base test class
│       ├── FoodDeliveryTest.java            # Food delivery tests
│       ├── UserManagementTest.java          # User management tests
│       └── HospitalAppointmentTest.java     # Appointment tests
│
├── pom.xml                                  # Maven configuration
├── testng-food-delivery.xml                 # TestNG suite config
└── README.md                                # This file
```

---

## 📊 Test Scenarios

### 1. **Food Delivery Order System**

#### Positive Tests ✅
- **Test:** Successful food order creation with valid items, delivery address, and promo code
- **Validates:** Order acceptance, delivery partner assignment, bill calculation
- **Expected:** 201 ACCEPTED status

- **Test:** Order without promo code
- **Validates:** Order creation without discount
- **Expected:** 201 ACCEPTED (0% discount)

- **Test:** Order with multiple items
- **Validates:** Multi-item order handling and subtotal calculation
- **Expected:** 201 ACCEPTED

#### Negative Tests ❌
- **Scenario:** Restaurant unavailable
  - **Error:** RESTAURANT_NOT_AVAILABLE
  - **Status:** 400 Bad Request

- **Scenario:** Non-existent item in order
  - **Error:** ITEM_NOT_FOUND
  - **Status:** 400 Bad Request

- **Scenario:** Delivery address outside service radius
  - **Error:** DELIVERY_OUTSIDE_RADIUS
  - **Status:** 400 Bad Request

- **Scenario:** Invalid payment mode
  - **Error:** INVALID_PAYMENT_MODE
  - **Status:** 400 Bad Request

- **Scenario:** No delivery partner available
  - **Error:** NO_DELIVERY_PARTNER_AVAILABLE
  - **Status:** 503 Service Unavailable

- **Scenario:** Missing required fields
  - **Error:** CUSTOMER_ID_REQUIRED
  - **Status:** 400 Bad Request

---

### 2. **Hospital Appointment Booking**

#### Positive Tests ✅
- **Test:** Successful appointment booking
- **Validates:** Doctor availability, patient registration, slot availability, insurance coverage
- **Expected:** 201 BOOKED status with appointmentId and token number

#### Negative Tests ❌
- **Scenario:** Doctor unavailable on selected date (Sunday)
  - **Error:** DOCTOR_UNAVAILABLE
  - **Status:** 400 Bad Request

- **Scenario:** Time slot already booked
  - **Error:** SLOT_CONFLICT
  - **Status:** 409 Conflict

- **Scenario:** Insurance not covering specialty
  - **Error:** INSURANCE_NOT_COVERED
  - **Status:** 422 Unprocessable Entity

- **Scenario:** Patient not registered in system
  - **Error:** PATIENT_NOT_FOUND
  - **Status:** 404 Not Found

---

### 3. **User Management API** (Included in base)

Tests for basic CRUD operations on user endpoints.

---

## 🚀 Setup & Installation

### Prerequisites
- **Java 11+** (JDK)
- **Maven 3.8+**
- **Git**

### Installation Steps

1. **Clone the repository:**
```bash
git clone https://github.com/kArThika0/Api_Testing_Rest_Assured_TestNG.git
cd RestAssuredTestNGFramework
```

2. **Install dependencies:**
```bash
mvn clean install
```

3. **Verify installation:**
```bash
mvn -v
java -version
```

---

## ▶️ Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
# Food Delivery tests
mvn -Dtest=FoodDeliveryTest test

# Hospital Appointment tests
mvn -Dtest=HospitalAppointmentTest test

# User Management tests
mvn -Dtest=UserManagementTest test
```

### Run Tests by Group (if configured in testng.xml)
```bash
mvn test -Dgroups=positive
mvn test -Dgroups=negative
```

### Run with Custom TestNG Suite
```bash
mvn test -DsuiteXmlFile=testng-food-delivery.xml
```

### View Test Reports
After running tests, reports are generated in:
```
target/surefire-reports/
target/site/
```

Open `target/surefire-reports/index.html` in a browser to view detailed test results.

---

## ✨ Key Features

### 1. **REST API Testing Best Practices**
- ✅ BDD-style test organization (Given-When-Then)
- ✅ Clear test names describing behavior
- ✅ Positive and negative test coverage
- ✅ Proper assertion strategies

### 2. **Request/Response Handling**
- ✅ **POJO Serialization/Deserialization** - Automatic JSON conversion using Jackson
- ✅ **Request Builders** - Fluent API for building complex requests
- ✅ **Response Assertions** - Status code, headers, body validation
- ✅ **Error Response Handling** - Graceful error message extraction

### 3. **Test Data Management**
- ✅ **Factories Pattern** - Reusable test data builders (FoodOrderData, AppointmentData)
- ✅ **Builder Pattern** - Fluent API for constructing test objects
- ✅ **Sample Data Sets** - Pre-built valid and invalid data scenarios

### 4. **API Mocking with WireMock**
- ✅ **Stub Creation** - Easy endpoint mocking without real backend
- ✅ **Response Templating** - Dynamic response generation
- ✅ **Request Matching** - Match by URL, method, headers, and body
- ✅ **Status Code Control** - Test both success and failure scenarios

### 5. **Validation Techniques**
- ✅ **JSON Schema Validation** - Validate response structure against schema
- ✅ **Field Assertions** - Check individual response fields
- ✅ **Collection Assertions** - Validate arrays and nested objects
- ✅ **Custom Matchers** - Domain-specific validation logic

### 6. **Code Organization**
- ✅ **Page Object Model (POM)** - Separation of concerns
- ✅ **Base Test Classes** - Shared setup/teardown logic
- ✅ **Helper Utilities** - Common functions in AppointmentUtils, Utils classes
- ✅ **Package Structure** - Logical grouping by domain

---

## 🎓 Best Practices Demonstrated

### 1. **Test Naming Convention**
```java
testSuccessfulFoodOrderCreation()        // What is being tested + expected outcome
testOrderWithRestaurantUnavailable()     // Error scenario naming
testDoctorUnavailable()                  // Domain-specific language
```

### 2. **Assertion Patterns**
```java
// Status code validation
.statusCode(201)

// Response body validation
.body(matchesJsonSchemaInClasspath("schemas/foodDelivery.json"))

// Field-level assertions
Assert.assertEquals(resp.getStatus(), "BOOKED");
Assert.assertTrue(resp.getOrderId().startsWith("FDO-"));

// Error handling
Assert.assertTrue(resp.getErrors().contains("SLOT_CONFLICT"));
```

### 3. **POJO Usage for Type Safety**
```java
// Serialization - Object to JSON
FoodOrderRequest request = data.createSampleOrder();
given().body(request).post(endpoint);

// Deserialization - JSON to Object
FoodOrderResponse response = given().post(endpoint)
    .then()
    .extract()
    .as(FoodOrderResponse.class);

// Type-safe assertions
Assert.assertEquals(response.getStatus(), "ACCEPTED");
```

### 4. **WireMock Stub Management**
```java
wireMockServer.stubFor(
    post(urlEqualTo("/api/v1/food-orders"))
        .withRequestBody(matchingJsonPath("$.customerId"))
        .willReturn(aResponse()
            .withStatus(201)
            .withHeader("Content-Type", "application/json")
            .withBody(responseBody))
);
```

### 5. **Test Data Builders**
```java
// Factory pattern for test data
AppointmentRequest req = data.createSampleRequest();

// Builder pattern for customization
req.setTimeSlot("10:00");           // Easy modification
AppointmentResponse resp = given()
    .body(req)
    .post(endpoint)
    .then()
    .extract()
    .as(AppointmentResponse.class);
```

### 6. **Business Logic Separation**
The `AppointmentUtils` class contains reusable business logic helpers:
```java
checkDoctorAvailability(doctorId, date, slot)       // Doctor availability rules
validatePatientRecord(patientId)                     // Patient validation
checkSlotConflict(doctorId, date, timeSlot)        // Slot booking conflicts
verifyInsurance(insuranceId, specialty)              // Insurance coverage
generateAppointmentConfirmation(...)                 // Business confirmation logic
```

---


### Testing Knowledge Demonstrated
1. **API Testing Fundamentals**
   - HTTP methods (POST, GET, PUT, DELETE)
   - Status codes and error handling
   - Request/Response lifecycle
   - Content negotiation (JSON)

2. **Automation Framework Design**
   - Page Object Model (POM) architecture
   - Test data management and factories
   - Base class inheritance for reusability
   - Separation of concerns (test vs. helper vs. data)

3. **TestNG Features**
   - Test groups (@Test annotations)
   - Before/After hooks (@BeforeClass, @AfterClass)
   - Assertions and validation
   - Test execution control

4. **RestAssured API**
   - Given-When-Then fluent API
   - Request building and customization
   - Response validation and extraction
   - POJO serialization/deserialization
   - JSON path and schema validation

5. **API Mocking & Testing Isolation**
   - WireMock server management
   - Endpoint stubbing with request matching
   - Response templating
   - Test independence and no external dependencies

6. **Test Scenarios & Coverage**
   - Positive (happy path) testing
   - Negative (error case) testing
   - Boundary value testing
   - Error message validation
   - Business rule enforcement

7. **Code Quality**
   - Consistent naming conventions
   - DRY principle (Don't Repeat Yourself)
   - Proper inheritance and reusability
   - Logical package organization
   - Clear, readable test code

### Why This Project 

✅ **Real-world Scenarios** - Food delivery, healthcare appointments, user management (relatable domains)

✅ **Complete Test Coverage** - Both positive and negative scenarios for each API

✅ **Production Patterns** - Uses industry-standard practices (POM, factories, proper organization)

✅ **Demonstrates Advanced Concepts**:
   - JSON schema validation
   - Business logic helpers
   - Test data builders
   - Mock server management
   - Complex object handling

✅ **Scalable Architecture** - Easy to add new test modules (just follow the pattern)

✅ **Clear Documentation** - Well-commented code with logical structure

---

## 📚 Additional Resources

### RestAssured Documentation
- [RestAssured Official Docs](https://rest-assured.io/)
- [RestAssured GitHub](https://github.com/rest-assured/rest-assured)

### TestNG Documentation
- [TestNG Official Docs](https://testng.org/doc/)
- [TestNG Annotations Guide](https://testng.org/doc/documentation-main.html#annotations)

### WireMock Documentation
- [WireMock Official Docs](https://wiremock.org/docs/)
- [WireMock Request Matching](https://wiremock.org/docs/request-matching/)

### Best Practices
- [REST API Testing Best Practices](https://www.restful-api-testing.com/)
- [API Testing Strategies](https://testautomation.expert/api-testing)

---

## 🤝 Contributing

This project serves as a learning resource and portfolio demonstration. Feel free to:

1. Learn from the patterns used
2. Extend with additional test scenarios
3. Modify test data for different business rules
4. Add more API endpoints

---

## 📄 License

This project is open source and available for educational purposes.

---

## 👤 Author

**SDET & QA Automation Engineer**

Focus areas:
- REST API Automation
- TestNG & RestAssured Frameworks
- Maven-based project structure
- Test data management and factories
- WireMock mocking servers
- CI/CD integration ready

---

## 📞 Questions?

Review the code comments, test classes, and helper utilities for additional insights. Each test demonstrates a specific API testing pattern or business scenario.

---

**Happy Testing! 🚀**

