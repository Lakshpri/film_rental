package com.example.film_rental_app.location_store_staffmodule.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StoreRequestDTO {

    @NotNull(message = "Manager Staff ID is required")
    @Positive(message = "Manager Staff ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer managerStaffId;

    @NotNull(message = "Address ID is required")
    @Positive(message = "Address ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer addressId;

    public StoreRequestDTO() {}

    public StoreRequestDTO(Integer managerStaffId, Integer addressId) {
        this.managerStaffId = managerStaffId;
        this.addressId = addressId;
    }

    public Integer getManagerStaffId() { return managerStaffId; }
    public void setManagerStaffId(Integer managerStaffId) { this.managerStaffId = managerStaffId; }

    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
}
