package com.example.film_rental_app.repository.Location_Store_StaffModule;

import com.example.film_rental_app.entity.Location_Store_StaffModule.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByStore_StoreId(Integer storeId);
}
