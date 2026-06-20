import FoodDeliveryPOM.FoodOrderData;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class FoodDeliveryBaseTest {

    protected WireMockServer wireMockServer;

    @BeforeClass(alwaysRun = true)
    public void startWireMockServer() {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        RestAssured.baseURI = "http://localhost:8090";
    }

    @AfterClass(alwaysRun = true)
    public void stopWireMockServer() {
        wireMockServer.stop();
    }
}
