package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.CustomerMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.ActorMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.location_store_staffmodule.mapper.StaffMapper;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.payment_reportsmodule.dto.response.SalesByCategoryDTO;
import com.example.film_rental_app.payment_reportsmodule.dto.response.SalesByStoreDTO;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import com.example.film_rental_app.payment_reportsmodule.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Implements ReportService — handles all report generation logic
// Delegates to domain services for list reports; uses PaymentRepository directly for aggregations
@Service
public class ReportServiceImpl implements ReportService {

    // Domain services used for fetching raw entity data
    @Autowired private CustomerService customerService;
    @Autowired private CustomerMapper  customerMapper;

    @Autowired private FilmService filmService;
    @Autowired private FilmMapper  filmMapper;

    @Autowired private StaffService staffService;
    @Autowired private StaffMapper  staffMapper;

    @Autowired private ActorService actorService;
    @Autowired private ActorMapper  actorMapper;

    // Used directly for aggregation queries — bypasses domain services intentionally
    @Autowired private PaymentRepository paymentRepository;

    // Fetches all customers, maps to response DTOs, returns with total count
    @Override
    public Map<String, Object> getCustomerList() {
        var customers = customerService.getAllCustomers()
                .stream().map(customerMapper::toResponseDTO).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", customers.size());
        map.put("customers", customers);
        return map;
    }

    // Fetches all films, maps to response DTOs, returns with total count
    @Override
    public Map<String, Object> getFilmList() {
        var films = filmService.getAllFilms()
                .stream().map(filmMapper::toResponseDTO).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", films.size());
        map.put("films", films);
        return map;
    }

    // Fetches all staff, maps to response DTOs, returns with total count
    @Override
    public Map<String, Object> getStaffList() {
        var staff = staffService.getAllStaff()
                .stream().map(staffMapper::toResponseDTO).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", staff.size());
        map.put("staff", staff);
        return map;
    }

    // Aggregates sales by store — Payment -> Staff -> Store join query
    // Maps raw Object[] rows to SalesByStoreDTO (storeId, totalPayments, totalRevenue)
    @Override
    public Map<String, Object> getSalesByStore() {
        List<Object[]> rows = paymentRepository.findSalesByStore();

        List<SalesByStoreDTO> stores = rows.stream().map(row -> new SalesByStoreDTO(
                (Integer)    row[0],   // storeId
                (Long)       row[1],   // totalPayments
                (BigDecimal) row[2]    // totalRevenue
        )).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", stores.size());
        map.put("stores", stores);
        return map;
    }

    // Aggregates sales by category — Payment -> Rental -> Inventory -> Film -> FilmCategory -> Category join query
    // Maps raw Object[] rows to SalesByCategoryDTO (categoryName, totalPayments, totalRevenue)
    @Override
    public Map<String, Object> getSalesByCategory() {
        List<Object[]> rows = paymentRepository.findSalesByCategory();

        List<SalesByCategoryDTO> categories = rows.stream().map(row -> new SalesByCategoryDTO(
                (String)     row[0],   // categoryName
                (Long)       row[1],   // totalPayments
                (BigDecimal) row[2]    // totalRevenue
        )).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", categories.size());
        map.put("categories", categories);
        return map;
    }

    // Fetches all actors, maps to response DTOs, returns with total count
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