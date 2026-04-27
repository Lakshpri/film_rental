package com.example.film_rental_app.payment_reportsmodule.service;

import java.util.Map;

// Defines all report operations available in the system
// ReportServiceImpl provides the actual implementation
public interface ReportService {

    // Returns all customers with total count — used for the customer report table
    Map<String, Object> getCustomerList();

    // Returns all films with total count — used for the film catalog report table
    Map<String, Object> getFilmList();

    // Returns all staff members with total count — used for the staff report table
    Map<String, Object> getStaffList();

    // Aggregates payments by store via Staff -> Store join — throws error if no data found
    Map<String, Object> getSalesByStore();

    // Aggregates payments by category via Rental -> Inventory -> Film -> Category chain — throws error if no data found
    Map<String, Object> getSalesByCategory();

    // Returns all actors with total count — used for the actor info report table
    Map<String, Object> getActorInfo();
}