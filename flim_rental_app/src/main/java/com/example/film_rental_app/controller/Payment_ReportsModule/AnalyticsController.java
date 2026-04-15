package com.example.film_rental_app.controller.Payment_ReportsModule;

import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Customer;
import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Inventory;
import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.CustomerRepository;
import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.InventoryRepository;
import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.RentalRepository;
import com.example.film_rental_app.repository.Payment_ReportsModule.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;

    public AnalyticsController(CustomerRepository customerRepository,
                               PaymentRepository paymentRepository,
                               RentalRepository rentalRepository,
                               InventoryRepository inventoryRepository) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * GET /api/analytics/customer-balance/{customerId}
     * Returns the total amount paid by a customer.
     */
    @GetMapping("/customer-balance/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerBalance(@PathVariable Integer customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) return ResponseEntity.notFound().build();

        BigDecimal balance = paymentRepository.findByCustomer_CustomerId(customerId)
                .stream()
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(Map.of(
                "customerId", customerId,
                "customerName", customer.getFirstName() + " " + customer.getLastName(),
                "totalPaid", balance
        ));
    }

    /**
     * GET /api/analytics/rewards-report
     * Returns customers with 30+ rentals (reward-eligible).
     */
    @GetMapping("/rewards-report")
    public List<Map<String, Object>> getRewardsReport() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    int rentalCount = rentalRepository.findByCustomer_CustomerId(customer.getCustomerId()).size();
                    return Map.<String, Object>of(
                            "customerId", customer.getCustomerId(),
                            "name", customer.getFirstName() + " " + customer.getLastName(),
                            "email", customer.getEmail() != null ? customer.getEmail() : "",
                            "rentalCount", rentalCount,
                            "rewardEligible", rentalCount >= 30
                    );
                })
                .filter(m -> (int) m.get("rentalCount") >= 30)
                .collect(Collectors.toList());
    }

    /**
     * GET /api/analytics/film-in-stock?filmId=1&storeId=1
     * Returns inventory items that are currently available (not rented out).
     */
    @GetMapping("/film-in-stock")
    public ResponseEntity<Map<String, Object>> getFilmInStock(
            @RequestParam Integer filmId,
            @RequestParam Integer storeId) {

        List<Inventory> inventories = inventoryRepository.findByStore_StoreIdAndFilm_FilmId(storeId, filmId);
        List<Inventory> inStock = inventories.stream()
                .filter(inv -> inv.getRentals().stream().allMatch(r -> r.getReturnDate() != null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "filmId", filmId,
                "storeId", storeId,
                "inStockCount", inStock.size(),
                "inventoryIds", inStock.stream().map(Inventory::getInventoryId).collect(Collectors.toList())
        ));
    }

    /**
     * GET /api/analytics/film-not-in-stock?filmId=1&storeId=1
     * Returns inventory items currently rented out.
     */
    @GetMapping("/film-not-in-stock")
    public ResponseEntity<Map<String, Object>> getFilmNotInStock(
            @RequestParam Integer filmId,
            @RequestParam Integer storeId) {

        List<Inventory> inventories = inventoryRepository.findByStore_StoreIdAndFilm_FilmId(storeId, filmId);
        List<Inventory> notInStock = inventories.stream()
                .filter(inv -> inv.getRentals().stream().anyMatch(r -> r.getReturnDate() == null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "filmId", filmId,
                "storeId", storeId,
                "notInStockCount", notInStock.size(),
                "inventoryIds", notInStock.stream().map(Inventory::getInventoryId).collect(Collectors.toList())
        ));
    }
}

