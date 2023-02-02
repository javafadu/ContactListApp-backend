package com.contactlistapp.exception.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

    /*
        We created this class for the messages to be sent
        to the client when we throw an exceptions.
     */

public class ApiResponseError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String requestURI;

    // ****** Constructors ******

    private ApiResponseError() {
        timestamp = LocalDateTime.now();
    }

    public ApiResponseError(HttpStatus status) {
        this(); // call above constructor
        this.status = status;
    }

    public ApiResponseError(HttpStatus status, String message, String requestURI) {
        this(status); // call above constructor
        this.message = message;
        this.requestURI = requestURI;
    }

    // ****** Getters & Setters, exclude setTimestamp ******

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
}
