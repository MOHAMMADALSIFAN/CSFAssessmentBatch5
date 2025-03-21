package vttp.batch5.csf.assessment.server.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import vttp.batch5.csf.assessment.server.model.Exception.ErrorMessage;
import vttp.batch5.csf.assessment.server.model.Exception.PaymentNotSuccessfulException;

@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(PaymentNotSuccessfulException.class)
    public ResponseEntity<ErrorMessage> handlePaymentNotSuccessfulException(PaymentNotSuccessfulException ex,
            WebRequest request) {
        ErrorMessage errMsg = new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(errMsg, HttpStatus.NOT_FOUND);
    }
}
