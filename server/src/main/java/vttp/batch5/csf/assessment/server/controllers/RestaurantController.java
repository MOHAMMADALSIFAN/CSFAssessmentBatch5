package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.model.CartItem;
import vttp.batch5.csf.assessment.server.model.Exception.PaymentNotSuccessfulException;
import vttp.batch5.csf.assessment.server.model.MenuItem;
import vttp.batch5.csf.assessment.server.model.Order;
import vttp.batch5.csf.assessment.server.model.PaymentResponse;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import vttp.batch5.csf.assessment.server.services.PaymentService;
import vttp.batch5.csf.assessment.server.services.RestaurantService;


@RestController
@RequestMapping(path = "/api")
public class RestaurantController {

    @Autowired
    private RestaurantService resturantsvc;

    @Autowired
    private RestaurantRepository RestaurantRepo;

    @Autowired
    private PaymentService paymentService;

    // TODO: Task 2.2
    // You may change the method's signature
    @GetMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItem>> getMenus() {
        List<MenuItem> menuItems = resturantsvc.getMenu();
        return ResponseEntity.ok(menuItems);
    }

    // TODO: Task 4
    // Do not change the method's signature
    @PostMapping(path = "/food-order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
        try {
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject orderJson = reader.readObject();
            reader.close();
            String username = orderJson.getString("username");
            String password = orderJson.getString("password");
            JsonArray itemsArray = orderJson.getJsonArray("items");
            boolean isValidUser = RestaurantRepo.verifyUsernameandpassword(username, password);
            if (!isValidUser) {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("message", "Invalid username and/or password")
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse.toString());
            }
            String orderId = RestaurantRepo.generateUniqueId();
            double totalPrice = 0.0;
            List<CartItem> orderItems = new ArrayList<>();
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject itemJson = itemsArray.getJsonObject(i);
                CartItem item = new CartItem();
                item.set_id(itemJson.getString("_id"));
                item.setName(itemJson.getString("name"));
                item.setPrice(itemJson.getJsonNumber("price").doubleValue());
                item.setQuantity(itemJson.getInt("quantity"));
                if (itemJson.containsKey("description")) {
                    item.setDescription(itemJson.getString("description"));
                }
                orderItems.add(item);
                totalPrice += (item.getPrice() * item.getQuantity());
            }
            PaymentResponse paymentResponse = paymentService.processPayment(orderId, username, totalPrice);
            java.sql.Date sqlOrderDate = new java.sql.Date(paymentResponse.getTimestamp());
            RestaurantRepo.saveOrder(
                    orderId,
                    paymentResponse.getPaymentId(),
                    sqlOrderDate,
                    paymentResponse.getTotal(),
                    username
            );
            Order mongoOrder = new Order();
            mongoOrder.setOrder_id(orderId);
            mongoOrder.set_id(orderId);
            mongoOrder.setPayment_id(paymentResponse.getPaymentId());
            mongoOrder.setUsername(username);
            mongoOrder.setTotal(paymentResponse.getTotal());
            mongoOrder.setTimestamp(paymentResponse.getTimestampAsDate());
            mongoOrder.setItems(orderItems);
            resturantsvc.saveOrder(mongoOrder);
            JsonObject receiptJson = Json.createObjectBuilder()
                    .add("order_id", paymentResponse.getOrderId())
                    .add("payment_id", paymentResponse.getPaymentId())
                    .add("total", paymentResponse.getTotal())
                    .add("timestamp", paymentResponse.getTimestamp())
                    .build();
            return ResponseEntity.ok(receiptJson.toString());
        } catch (PaymentNotSuccessfulException e) {
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorJson.toString());
        }
    }
}
