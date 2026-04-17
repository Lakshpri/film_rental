package com.example.film_rental_app.location_store_staffmodule.dto.request;


import jakarta.validation.constraints.NotNull;

public class StoreRequestDTO {

    @NotNull(message = "Manager staff ID is required")
    private Integer managerStaffId;

    @NotNull(message = "Address ID is required")
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

