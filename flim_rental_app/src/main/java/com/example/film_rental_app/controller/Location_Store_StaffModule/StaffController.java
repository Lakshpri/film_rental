package com.example.film_rental_app.controller.Location_Store_StaffModule;


import com.example.film_rental_app.entity.Location_Store_StaffModule.Staff;
import com.example.film_rental_app.repository.Location_Store_StaffModule.StaffRepository;
import com.example.film_rental_app.repository.Location_Store_StaffModule.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StaffController {

    private final StaffRepository staffRepository;
    private final StoreRepository storeRepository;

    public StaffController(StaffRepository staffRepository, StoreRepository storeRepository) {
        this.staffRepository = staffRepository;
        this.storeRepository = storeRepository;
    }

    @GetMapping("/api/staff")
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @GetMapping("/api/staff/{staffId}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Integer staffId) {
        return staffRepository.findById(staffId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/stores/{storeId}/staff")
    public ResponseEntity<List<Staff>> getStaffByStore(@PathVariable Integer storeId) {
        if (!storeRepository.existsById(storeId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(staffRepository.findByStore_StoreId(storeId));
    }

    @PostMapping("/api/staff")
    public Staff createStaff(@RequestBody Staff staff) {
        return staffRepository.save(staff);
    }

    @PutMapping("/api/staff/{staffId}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Integer staffId, @RequestBody Staff updated) {
        return staffRepository.findById(staffId).map(staff -> {
            staff.setFirstName(updated.getFirstName());
            staff.setLastName(updated.getLastName());
            staff.setEmail(updated.getEmail());
            staff.setActive(updated.isActive());
            staff.setUsername(updated.getUsername());
            staff.setPassword(updated.getPassword());
            if (updated.getAddress() != null) staff.setAddress(updated.getAddress());
            if (updated.getStore() != null) staff.setStore(updated.getStore());
            return ResponseEntity.ok(staffRepository.save(staff));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/staff/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer staffId) {
        if (!staffRepository.existsById(staffId)) return ResponseEntity.notFound().build();
        staffRepository.deleteById(staffId);
        return ResponseEntity.noContent().build();
    }
}

