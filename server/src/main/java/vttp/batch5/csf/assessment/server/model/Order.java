package vttp.batch5.csf.assessment.server.model;

import java.util.Date;
import java.util.List;

public class Order {

    private String _id;
    private String order_id;
    private String payment_id;
    private String username;
    private double total;
    private Date timestamp;
    private List<CartItem> items;

    public Order(String _id, String order_id, String payment_id, String username, double total, Date timestamp,
            List<CartItem> items) {
        this._id = _id;
        this.order_id = order_id;
        this.payment_id = payment_id;
        this.username = username;
        this.total = total;
        this.timestamp = timestamp;
        this.items = items;
    }

    public Order() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
        this._id = order_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
