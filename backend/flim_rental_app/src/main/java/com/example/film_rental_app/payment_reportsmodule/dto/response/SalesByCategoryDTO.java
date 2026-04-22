package com.example.film_rental_app.payment_reportsmodule.dto;

import java.math.BigDecimal;

public class SalesByCategoryDTO {

    private String     categoryName;
    private Long       totalPayments;
    private BigDecimal totalRevenue;

    public SalesByCategoryDTO(String categoryName, Long totalPayments, BigDecimal totalRevenue) {
        this.categoryName  = categoryName;
        this.totalPayments = totalPayments;
        this.totalRevenue  = totalRevenue;
    }

    public String     getCategoryName()   { return categoryName; }
    public Long       getTotalPayments()  { return totalPayments; }
    public BigDecimal getTotalRevenue()   { return totalRevenue; }

    public void setCategoryName(String categoryName)        { this.categoryName = categoryName; }
    public void setTotalPayments(Long totalPayments)        { this.totalPayments = totalPayments; }
    public void setTotalRevenue(BigDecimal totalRevenue)    { this.totalRevenue = totalRevenue; }
}