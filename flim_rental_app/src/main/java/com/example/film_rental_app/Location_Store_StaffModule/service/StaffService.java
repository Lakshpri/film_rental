package com.example.film_rental_app.Location_Store_StaffModule.service;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Staff;

import java.util.List;

public interface StaffService {
    List<Staff> getAllStaff();

    Staff getStaffById(Integer staffId);

    Staff createStaff(Staff staff);

    Staff updateStaff(Integer staffId, Staff updated);

    void deleteStaff(Integer staffId);

    List<Staff> getStaffByStore(Integer storeId);
}
