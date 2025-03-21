package vttp.batch5.csf.assessment.server.model.Exception;

public class PaymentNotSuccessfulException extends RuntimeException {
    
  public PaymentNotSuccessfulException() {
      super();
  }

  public PaymentNotSuccessfulException(String message, Throwable cause) {
      super(message, cause);
  }

  public PaymentNotSuccessfulException(String message) {
      super(message);
  }

  public PaymentNotSuccessfulException(Throwable cause) {
      super(cause);
  }
}