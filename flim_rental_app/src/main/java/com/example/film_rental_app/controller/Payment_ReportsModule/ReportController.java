package com.example.film_rental_app.controller.Payment_ReportsModule;


import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.CustomerRepository;
import com.example.film_rental_app.repository.Location_Store_StaffModule.StaffRepository;
import com.example.film_rental_app.repository.FilmCatalog_ContentModule.ActorRepository;
import com.example.film_rental_app.repository.FilmCatalog_ContentModule.FilmCategoryRepository;
import com.example.film_rental_app.repository.FilmCatalog_ContentModule.FilmRepository;
import com.example.film_rental_app.repository.Payment_ReportsModule.PaymentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;
    private final StaffRepository staffRepository;
    private final PaymentRepository paymentRepository;
    private final FilmCategoryRepository filmCategoryRepository;
    private final ActorRepository actorRepository;

    public ReportController(CustomerRepository customerRepository,
                            FilmRepository filmRepository,
                            StaffRepository staffRepository,
                            PaymentRepository paymentRepository,
                            FilmCategoryRepository filmCategoryRepository,
                            ActorRepository actorRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
        this.staffRepository = staffRepository;
        this.paymentRepository = paymentRepository;
        this.filmCategoryRepository = filmCategoryRepository;
        this.actorRepository = actorRepository;
    }

    @GetMapping("/customer-list")
    public List<Customer> customerList() {
        return customerRepository.findAll();
    }

    @GetMapping("/film-list")
    public List<Film> filmList() {
        return filmRepository.findAll();
    }

    @GetMapping("/staff-list")
    public List<Staff> staffList() {
        return staffRepository.findAll();
    }

    @GetMapping("/sales-by-store")
    public List<Payment> salesByStore() {
        return paymentRepository.findAll();
    }

    @GetMapping("/sales-by-category")
    public List<FilmCategory> salesByCategory() {
        return filmCategoryRepository.findAll();
    }

    @GetMapping("/actor-info")
    public List<Actor> actorInfo() {
        return actorRepository.findAll();
    }
}
