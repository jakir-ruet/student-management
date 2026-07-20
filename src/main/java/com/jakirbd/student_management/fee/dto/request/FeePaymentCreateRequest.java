package com.jakirbd.student_management.fee.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FeePaymentCreateRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Receipt number is required")
    @Size(
            max = 50,
            message = "Receipt number cannot exceed 50 characters"
    )
    private String receiptNumber;

    private LocalDateTime paymentDate;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Payment amount must be greater than zero"
    )
    private BigDecimal paymentAmount;

    @NotBlank(message = "Payment method is required")
    @Pattern(
            regexp = "CASH|CARD|BANK_TRANSFER|MOBILE_BANKING|CHEQUE|ONLINE",
            message = "Invalid payment method"
    )
    private String paymentMethod;

    @Size(
            max = 100,
            message = "Transaction reference cannot exceed 100 characters"
    )
    private String transactionReference;

    @Size(
            max = 500,
            message = "Notes cannot exceed 500 characters"
    )
    private String notes;

    private Long receivedBy;

    private Long createdBy;

    @NotEmpty(message = "At least one payment allocation is required")
    @Valid
    private List<FeePaymentAllocationCreateRequest> allocations;

    public FeePaymentCreateRequest() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(
            String transactionReference
    ) {
        this.transactionReference = transactionReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Long receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<FeePaymentAllocationCreateRequest>
    getAllocations() {
        return allocations;
    }

    public void setAllocations(
            List<FeePaymentAllocationCreateRequest> allocations
    ) {
        this.allocations = allocations;
    }
}