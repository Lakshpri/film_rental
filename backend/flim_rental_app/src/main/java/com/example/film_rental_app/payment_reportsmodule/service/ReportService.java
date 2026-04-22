package com.example.film_rental_app.payment_reportsmodule.service;

import java.util.Map;

public interface ReportService {

    Map<String, Object> getCustomerList();

    Map<String, Object> getFilmList();

    Map<String, Object> getStaffList();

    Map<String, Object> getSalesByStore();

    Map<String, Object> getSalesByCategory();

    Map<String, Object> getActorInfo();
}
