package com.crm.rentcar.Exceptions.ControllerAdvice;

import com.crm.rentcar.Exceptions.GlobalExceptions.CustomAlreadyExistException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomCarPriceException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomNotFoundException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomOrderDateFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> handleCustomNotFoundException(CustomNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomAlreadyExistException.class)
    public ResponseEntity<?> handleCustomAlreadyExistException(CustomAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomCarPriceException.class)
    public ResponseEntity<?> handleCustomCarPriceException(CustomCarPriceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomOrderDateFormatException.class)
    public ResponseEntity<?> handleCustomOrderDateFormatException(CustomOrderDateFormatException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
