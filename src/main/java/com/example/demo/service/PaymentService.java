package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPayment(String id);
    List<PaymentResponse> listPayments();
    List<PaymentResponse> findByRepairOrderId(String repairOrderId);
    List<PaymentResponse> findByStatus(String status);
    PaymentResponse updateStatus(String id, String status);
    void deleteById(String id);
}
