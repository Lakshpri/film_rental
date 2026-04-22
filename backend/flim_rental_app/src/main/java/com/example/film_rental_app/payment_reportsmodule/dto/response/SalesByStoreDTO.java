package com.example.film_rental_app.payment_reportsmodule.dto;

import java.math.BigDecimal;

public class SalesByStoreDTO {

    private Integer storeId;
    private Long    totalPayments;
    private BigDecimal totalRevenue;

    public SalesByStoreDTO(Integer storeId, Long totalPayments, BigDecimal totalRevenue) {
        this.storeId       = storeId;
        this.totalPayments = totalPayments;
        this.totalRevenue  = totalRevenue;
    }

    public Integer getStoreId()           { return storeId; }
    public Long    getTotalPayments()     { return totalPayments; }
    public BigDecimal getTotalRevenue()   { return totalRevenue; }

    public void setStoreId(Integer storeId)             { this.storeId = storeId; }
    public void setTotalPayments(Long totalPayments)    { this.totalPayments = totalPayments; }
    public void setTotalRevenue(BigDecimal totalRevenue){ this.totalRevenue = totalRevenue; }
}