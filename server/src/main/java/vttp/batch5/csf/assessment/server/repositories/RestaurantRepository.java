package vttp.batch5.csf.assessment.server.repositories;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    
    //SELECT COUNT(*) FROM customers WHERE username = "fred" AND password = SHA2("fred", 224);
    public static final String Count_Usernameandpass = "SELECT COUNT(*) FROM customers WHERE username = ? AND password = SHA2(?, 224)";

    public boolean verifyUsernameandpassword(String username, String password) {
        Integer count = template.queryForObject(Count_Usernameandpass, Integer.class, username, password);
        return count != null && count > 0;
    }

    public void saveOrder(String orderId, String paymentId, java.sql.Date orderDate, double total, String username) {
      String Insert_Order_sql = "INSERT INTO place_orders (order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)";
      template.update( Insert_Order_sql, orderId,paymentId,orderDate,total,username);
  }

  // Generate random 8-character order_id using UUID
  public String generateUniqueId() {
    return UUID.randomUUID().toString().toUpperCase().substring(0, 8);
  }
}
