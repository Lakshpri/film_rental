package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.payment_reportsmodule.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private CustomerService customerService;
    @Override
    public Map<String, Object> getCustomerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("customers", customerService.getAllCustomers());
        return map;
    }

    @Override
    public Map<String, Object> getFilmList() {
        Map<String, Object> map = new HashMap<>();
        map.put("films", "Film list data"); // connect later
        return map;
    }

    @Override
    public Map<String, Object> getStaffList() {
        Map<String, Object> map = new HashMap<>();
        map.put("staff", "Staff list data");
        return map;
    }

    @Override
    public Map<String, Object> getSalesByStore() {
        Map<String, Object> map = new HashMap<>();
        map.put("salesByStore", "Sales by store data");
        return map;
    }

    @Override
    public Map<String, Object> getSalesByCategory() {
        Map<String, Object> map = new HashMap<>();
        map.put("salesByCategory", "Sales by category data");
        return map;
    }

    @Override
    public Map<String, Object> getActorInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("actors", "Actor info data");
        return map;
    }
}