package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.CustomerMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.ActorMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.location_store_staffmodule.mapper.StaffMapper;
import com.example.film_rental_app.location_store_staffmodule.mapper.StoreMapper;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import com.example.film_rental_app.master_datamodule.mapper.CategoryMapper;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import com.example.film_rental_app.payment_reportsmodule.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired private CustomerService customerService;
    @Autowired private CustomerMapper  customerMapper;

    @Autowired private FilmService filmService;
    @Autowired private FilmMapper  filmMapper;

    @Autowired private StaffService staffService;
    @Autowired private StaffMapper  staffMapper;

    @Autowired private StoreService storeService;
    @Autowired private StoreMapper  storeMapper;

    @Autowired private CategoryService categoryService;
    @Autowired private CategoryMapper  categoryMapper;

    @Autowired private ActorService actorService;
    @Autowired private ActorMapper  actorMapper;

    @Override
    public Map<String, Object> getCustomerList() {
        var customers = customerService.getAllCustomers()
                .stream().map(customerMapper::toResponseDTO).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", customers.size());
        map.put("customers", customers);
        return map;
    }

    @Override
    public Map<String, Object> getFilmList() {
        var films = filmService.getAllFilms()
                .stream().map(filmMapper::toResponseDTO).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", films.size());
        map.put("films", films);
        return map;
    }

    @Override
    public Map<String, Object> getStaffList() {
        var staff = staffService.getAllStaff()
                .stream().map(staffMapper::toResponseDTO).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", staff.size());
        map.put("staff", staff);
        return map;
    }

    @Override
    public Map<String, Object> getSalesByStore() {
        var stores = storeService.getAllStores()
                .stream().map(storeMapper::toResponseDTO).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", stores.size());
        map.put("stores", stores);
        return map;
    }

    @Override
    public Map<String, Object> getSalesByCategory() {
        var categories = categoryService.getAllCategories()
                .stream().map(categoryMapper::toResponseDTO).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", categories.size());
        map.put("categories", categories);
        return map;
    }

    @Override
    public Map<String, Object> getActorInfo() {
        var actors = actorService.getAllActors()
                .stream().map(actorMapper::toResponseDTO).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", actors.size());
        map.put("actors", actors);
        return map;
    }
}