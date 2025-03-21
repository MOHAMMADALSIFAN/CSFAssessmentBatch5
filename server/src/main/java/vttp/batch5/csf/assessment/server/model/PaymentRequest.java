package vttp.batch5.csf.assessment.server.model;

public class PaymentRequest {

    private String order_id;
    private String payer;
    private String payee;
    private double payment;

    public PaymentRequest() {
    }

    public PaymentRequest(String order_id, String payer, String payee, double payment) {
        this.order_id = order_id;
        this.payer = payer;
        this.payee = payee;
        this.payment = payment;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }
}
