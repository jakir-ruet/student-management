package com.jakirbd.student_management.fee.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class StudentFeeDiscountCreateRequest {

    @NotNull(message = "Student fee ID is required")
    private Long studentFeeId;

    @NotBlank(message = "Discount type is required")
    @Pattern(
            regexp = "FIXED|PERCENTAGE",
            message = "Discount type must be FIXED or PERCENTAGE"
    )
    private String discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(
            value = "0.01",
            message = "Discount value must be greater than zero"
    )
    private BigDecimal discountValue;

    @NotBlank(message = "Discount reason is required")
    @Size(
            max = 500,
            message = "Discount reason cannot exceed 500 characters"
    )
    private String reason;

    private Long createdBy;

    public StudentFeeDiscountCreateRequest() {
    }

    public Long getStudentFeeId() {
        return studentFeeId;
    }

    public void setStudentFeeId(Long studentFeeId) {
        this.studentFeeId = studentFeeId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}