package com.example.film_rental_app.payment_reportsmodule.dto.response;

import java.math.BigDecimal;

// Read-only DTO — holds aggregated payment summary grouped by store
// Used in the Sales by Store report
public class SalesByStoreDTO {

    private Integer    storeId;       // e.g. 1, 2
    private Long       totalPayments; // total number of payments in that store
    private BigDecimal totalRevenue;  // total money collected from that store

    // Called by ReportServiceImpl to build this DTO from raw DB query results
    public SalesByStoreDTO(Integer storeId, Long totalPayments, BigDecimal totalRevenue) {
        this.storeId       = storeId;
        this.totalPayments = totalPayments;
        this.totalRevenue  = totalRevenue;
    }

    // Getters — called by Jackson at runtime to build the JSON response
    public Integer    getStoreId()       { return storeId; }
    public Long       getTotalPayments() { return totalPayments; }
    public BigDecimal getTotalRevenue()  { return totalRevenue; }
}