package com.example.demo.dto;

// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.Setter;


public class CustomerResponse {
    private String message;
    private Object data;

    public CustomerResponse() {
    }

    public CustomerResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
