package com.example.film_rental_app.location_store_staffmodule.controller;

import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Integer staffId) {
        return ResponseEntity.ok(staffService.getStaffById(staffId));
    }

    @PostMapping
    public ResponseEntity<Staff> createStaff(@Valid @RequestBody Staff staff) {
        Staff created = staffService.createStaff(staff);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<Staff> updateStaff(
            @PathVariable Integer staffId,
            @Valid @RequestBody Staff updated) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, updated));
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}