package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.CustomerMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.ActorMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.location_store_staffmodule.mapper.StaffMapper;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.payment_reportsmodule.dto.SalesByCategoryDTO;
import com.example.film_rental_app.payment_reportsmodule.dto.SalesByStoreDTO;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import com.example.film_rental_app.payment_reportsmodule.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired private CustomerService customerService;
    @Autowired private CustomerMapper  customerMapper;

    @Autowired private FilmService filmService;
    @Autowired private FilmMapper  filmMapper;

    @Autowired private StaffService staffService;
    @Autowired private StaffMapper  staffMapper;

    @Autowired private ActorService actorService;
    @Autowired private ActorMapper  actorMapper;

    // ✅ Use PaymentRepository directly for aggregation queries
    @Autowired private PaymentRepository paymentRepository;

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
        // ✅ Real aggregation: Payment -> Staff -> Store
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

    @Override
    public Map<String, Object> getSalesByCategory() {
        // ✅ Real aggregation: Payment -> Rental -> Inventory -> Film -> FilmCategory -> Category
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
