package vttp.batch5.csf.assessment.server.model;

public class PaymentResponse {

    private String paymentId;
    private String orderId;
    private long timestamp;
    private double total;

    public PaymentResponse() {
    }

    public PaymentResponse(String paymentId, String orderId, long timestamp, double total) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.total = total;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public java.util.Date getTimestampAsDate() {
        return new java.util.Date(this.timestamp);
    }
}
