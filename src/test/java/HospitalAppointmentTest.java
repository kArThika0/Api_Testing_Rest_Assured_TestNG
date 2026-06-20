import HospitalPOM.AppointmentData;
import HospitalPOM.AppointmentRequest;
import HospitalPOM.AppointmentResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class HospitalAppointmentTest extends FoodDeliveryBaseTest {

    AppointmentData data = new AppointmentData();

    @Test(groups = {"positive"})
    public void testSuccessfulBooking() {
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/appointments"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "appointmentId": "APT-20260710-003",
                                          "status": "BOOKED",
                                          "doctorName": "Dr. Ramesh Iyer",
                                          "hospital": "Apollo Chennai",
                                          "scheduledAt": "2026-07-10T10:30:00+05:30",
                                          "tokenNumber": 3,
                                          "instructions": "Arrive 15 mins early. Bring previous reports.",
                                          "errors": []
                                        }
                                        """)
                        )
        );

        AppointmentRequest req = data.createSampleRequest();

        AppointmentResponse resp = given()
                .header("Content-Type", "application/json")
                .body(req)
                .when()
                .post("/api/v1/appointments")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(AppointmentResponse.class);

        Assert.assertEquals(resp.getStatus(), "BOOKED");
        Assert.assertNotNull(resp.getAppointmentId());
        Assert.assertEquals(resp.getDoctorName(), "Dr. Ramesh Iyer");
    }

    @Test(groups = {"negative"})
    public void testDoctorUnavailable() {
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/appointments"))
                        .withRequestBody(matchingJsonPath("$.appointmentDate", equalTo("2026-07-11")))
                        .willReturn(aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "appointmentId": null,
                                          "status": "REJECTED",
                                          "doctorName": null,
                                          "hospital": null,
                                          "scheduledAt": null,
                                          "tokenNumber": null,
                                          "instructions": null,
                                          "errors": ["DOCTOR_UNAVAILABLE"]
                                        }
                                        """)
                        )
        );

        AppointmentRequest req = data.createSampleRequest();
        req.setAppointmentDate("2026-07-11"); // assume Sunday/unavailable by business rule

        AppointmentResponse resp = given()
                .header("Content-Type", "application/json")
                .body(req)
                .when()
                .post("/api/v1/appointments")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .as(AppointmentResponse.class);

        Assert.assertEquals(resp.getStatus(), "REJECTED");
        Assert.assertTrue(resp.getErrors().contains("DOCTOR_UNAVAILABLE"));
    }

    @Test(groups = {"negative"})
    public void testSlotConflict() {
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/appointments"))
                        .withRequestBody(matchingJsonPath("$.timeSlot", equalTo("10:00")))
                        .willReturn(aResponse()
                                .withStatus(409)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "appointmentId": null,
                                          "status": "SLOT_CONFLICT",
                                          "doctorName": null,
                                          "hospital": null,
                                          "scheduledAt": null,
                                          "tokenNumber": null,
                                          "instructions": null,
                                          "errors": ["SLOT_CONFLICT"]
                                        }
                                        """)
                        )
        );

        AppointmentRequest req = data.createSampleRequest();
        req.setTimeSlot("10:00"); // pre-booked in AppointmentUtils

        AppointmentResponse resp = given()
                .header("Content-Type", "application/json")
                .body(req)
                .when()
                .post("/api/v1/appointments")
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .as(AppointmentResponse.class);

        Assert.assertEquals(resp.getStatus(), "SLOT_CONFLICT");
        Assert.assertTrue(resp.getErrors().contains("SLOT_CONFLICT"));
    }

    @Test(groups = {"negative"})
    public void testInsuranceNotCovering() {
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/appointments"))
                        .withRequestBody(matchingJsonPath("$.insuranceId", equalTo("INS-NOCARD")))
                        .willReturn(aResponse()
                                .withStatus(422)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "appointmentId": null,
                                          "status": "REJECTED",
                                          "doctorName": null,
                                          "hospital": null,
                                          "scheduledAt": null,
                                          "tokenNumber": null,
                                          "instructions": null,
                                          "errors": ["INSURANCE_NOT_COVERED"]
                                        }
                                        """)
                        )
        );

        AppointmentRequest req = data.createSampleRequest();
        req.setInsuranceId("INS-NOCARD");

        AppointmentResponse resp = given()
                .header("Content-Type", "application/json")
                .body(req)
                .when()
                .post("/api/v1/appointments")
                .then()
                .assertThat()
                .statusCode(422)
                .extract()
                .as(AppointmentResponse.class);

        Assert.assertEquals(resp.getStatus(), "REJECTED");
        Assert.assertTrue(resp.getErrors().contains("INSURANCE_NOT_COVERED"));
    }

    @Test(groups = {"negative"})
    public void testPatientNotRegistered() {
        wireMockServer.stubFor(
                post(urlEqualTo("/api/v1/appointments"))
                        .withRequestBody(matchingJsonPath("$.patientId", equalTo("PAT-404")))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                          "appointmentId": null,
                                          "status": "REJECTED",
                                          "doctorName": null,
                                          "hospital": null,
                                          "scheduledAt": null,
                                          "tokenNumber": null,
                                          "instructions": null,
                                          "errors": ["PATIENT_NOT_FOUND"]
                                        }
                                        """)
                        )
        );

        AppointmentRequest req = data.createSampleRequest();
        req.setPatientId("PAT-404");

        AppointmentResponse resp = given()
                .header("Content-Type", "application/json")
                .body(req)
                .when()
                .post("/api/v1/appointments")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .as(AppointmentResponse.class);

        Assert.assertEquals(resp.getStatus(), "REJECTED");
        Assert.assertTrue(resp.getErrors().contains("PATIENT_NOT_FOUND"));
    }
}

