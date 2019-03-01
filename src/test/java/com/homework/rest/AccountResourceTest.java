package com.homework.rest;

import com.homework.app.BankApplication;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AccountResourceTest {

    private static Server server;

    private static int port;

    @BeforeClass
    public static void startApp() throws Exception {
        BankApplication app = new BankApplication();
        server = app.start(0);
        port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
    }

    @AfterClass
    public static void stopApp() throws Exception {
        server.stop();
        server.destroy();
    }

    @Test
    public void testCreateAccounts() {
        createAccount();
    }

    @Test
    public void testCreateAmountWithNegativeAmount() {
        String body = "{ \"startAmount\": -100 }";
        given()
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts")
                .then()
                .statusCode(422)
                .assertThat()
                .body("status", Matchers.equalTo("ERROR"));
    }

    @Test
    public void testGetAccountBalance() {
        String accountNumber = createAccount();
        given()
                .port(port)
                .when()
                .get("/accounts/{accountId}/balance", accountNumber)
                .then()
                .statusCode(200)
                .assertThat()
                .body("status", Matchers.equalTo("SUCCESS"));
    }

    @Test
    public void testGetAccountBalanceWithAccountNotFound() {
        given()
                .port(port)
                .when()
                .get("/accounts/{accountId}/balance", "BAD_ACCOUNT_NUMBER")
                .then()
                .statusCode(404)
                .assertThat()
                .body("status", Matchers.equalTo("ERROR"));
    }

    @Test
    public void testTransferAmount() {
        String fromAccountNumber = createAccount();
        String toAccountNumber = createAccount();
        String body = "{ \"toAccountNumber\": \"" + toAccountNumber + "\", \"amount\": 100 }";
        given()
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts/{accountId}/transfer", fromAccountNumber)
                .then()
                .statusCode(200)
                .assertThat()
                .body("status", Matchers.equalTo("SUCCESS"));

        given()
                .port(port)
                .when()
                .get("/accounts/{accountId}/balance", toAccountNumber)
                .then()
                .statusCode(200)
                .assertThat()
                .body("status", Matchers.equalTo("SUCCESS"))
                .body("data", Matchers.equalTo(200));

    }

    @Test
    public void testTransferAmountWithNegativeAmount() {
        String fromAccountNumber = createAccount();
        String toAccountNumber = createAccount();
        String body = "{ \"toAccountNumber\": \"" + toAccountNumber + "\", \"amount\": -100 }";
        given()
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts/{accountId}/transfer", fromAccountNumber)
                .then()
                .statusCode(422)
                .assertThat()
                .body("status", Matchers.equalTo("ERROR"));
    }

    @Test
    public void testTransferAmountWithWrongFromAccount() {
        String toAccountNumber = createAccount();
        String body = "{ \"toAccountNumber\": \"" + toAccountNumber + "\", \"amount\": 100 }";
        given()
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts/{accountId}/transfer", "BAD_ACCOUNT_NUMBER")
                .then()
                .statusCode(404)
                .assertThat()
                .body("status", Matchers.equalTo("ERROR"));
    }

    @Test
    public void testTransferAmountWithWrongToAccount() {
        String fromAccountNumber = createAccount();
        String toAccountNumber = "BAD_ACCOUNT_NUMBER";
        String body = "{ \"toAccountNumber\": \"" + toAccountNumber + "\", \"amount\": 100 }";
        given()
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/accounts/{accountId}/transfer", fromAccountNumber)
                .then()
                .statusCode(422)
                .assertThat()
                .body("status", Matchers.equalTo("ERROR"));
    }

    private String createAccount() {
        RequestSpecification request = RestAssured.given();
        request.port(port);
        request.header("Content-Type", "application/json");
        String body = "{ \"startAmount\": 100 }";
        request.body(body);
        Response response = request.post("/accounts");
        response.then()
                .statusCode(200)
                .assertThat()
                .body("status", Matchers.equalTo("SUCCESS"));
        return response.jsonPath().get("data");
    }

}
