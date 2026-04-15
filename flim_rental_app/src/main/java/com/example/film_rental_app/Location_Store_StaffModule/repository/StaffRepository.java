package com.example.film_rental_app.Location_Store_StaffModule.repository;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByStore_StoreId(Integer storeId);
}
