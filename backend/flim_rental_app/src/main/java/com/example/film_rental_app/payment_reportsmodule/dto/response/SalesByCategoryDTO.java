package com.example.film_rental_app.payment_reportsmodule.dto.response;

import java.math.BigDecimal;

// Read-only DTO — holds aggregated payment summary grouped by film category
// Used in the Sales by Category report
public class SalesByCategoryDTO {

    private String     categoryName;  // e.g. "Action", "Drama"
    private Long       totalPayments; // total number of payments in that category
    private BigDecimal totalRevenue;  // total money collected from that category

    // Called by ReportServiceImpl to build this DTO from raw DB query results
    public SalesByCategoryDTO(String categoryName, Long totalPayments, BigDecimal totalRevenue) {
        this.categoryName  = categoryName;
        this.totalPayments = totalPayments;
        this.totalRevenue  = totalRevenue;
    }

    // Getters — called by Jackson at runtime to build the JSON response
    public String     getCategoryName()  { return categoryName; }
    public Long       getTotalPayments() { return totalPayments; }
    public BigDecimal getTotalRevenue()  { return totalRevenue; }
}