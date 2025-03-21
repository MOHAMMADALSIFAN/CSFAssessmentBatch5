package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.model.Exception.PaymentNotSuccessfulException;
import vttp.batch5.csf.assessment.server.model.PaymentResponse;

@Service
public class PaymentService {
  private static final String PAYMENT_ENDPOINT = "https://payment-service-production-a75a.up.railway.app/api/payment";
    
    // Task 4: connect to chuck api and get the json response
    public PaymentResponse processPayment(String orderId, String username, double total) throws PaymentNotSuccessfulException {
        try {
          //https://stackoverflow.com/questions/19238715/how-to-set-an-accept-header-on-spring-resttemplate-request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-Authenticate", username);
            JsonObject requestBody = Json.createObjectBuilder()
                .add("order_id", orderId)
                .add("payer", username)
                .add("payee", "Mohammad Alsifan S/O Mohammad Najubudeen")
                .add("payment", total)
                .build();
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(PAYMENT_ENDPOINT, HttpMethod.POST,requestEntity,String.class);
            //https://docs.spring.io/spring-framework/docs/4.0.2.RELEASE_to_4.0.3.RELEASE/Spring%20Framework%204.0.3.RELEASE/org/springframework/http/HttpStatus.html#is2xxSuccessful()
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonReader reader = Json.createReader(new StringReader(response.getBody()));
                JsonObject responseJson = reader.readObject();
                reader.close();
                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setPaymentId(responseJson.getString("payment_id"));
                paymentResponse.setOrderId(responseJson.getString("order_id"));
                paymentResponse.setTimestamp(responseJson.getJsonNumber("timestamp").longValue());
                paymentResponse.setTotal(responseJson.getJsonNumber("total").doubleValue());
                return paymentResponse;
            } else {
                throw new PaymentNotSuccessfulException("Payment response returned error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new PaymentNotSuccessfulException("Payment processing failed: " + e.getMessage());
        }
    }
}
