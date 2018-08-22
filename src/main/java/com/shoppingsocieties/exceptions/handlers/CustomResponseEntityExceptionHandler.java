package com.shoppingsocieties.exceptions.handlers;

import com.shoppingsocieties.exceptions.PaymentException;
import com.shoppingsocieties.exceptions.PurchaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, PaymentException.class, PurchaseException.class})
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> errMsg = new HashMap<>();
        errMsg.put("error", e.getMessage());
        System.out.println(e.getMessage());
        return ResponseEntity.badRequest().body(errMsg);
    }
}
