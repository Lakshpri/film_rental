package com.example.film_rental_app.location_store_staffmodule.dto.response;


import java.time.LocalDateTime;

public class StoreResponseDTO {

    private Integer storeId;
    private Integer managerStaffId;
    private String managerFullName;
    private Integer addressId;
    private String addressLine;
    private String city;
    private String country;
    private LocalDateTime lastUpdate;

    public StoreResponseDTO() {}

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public Integer getManagerStaffId() { return managerStaffId; }
    public void setManagerStaffId(Integer managerStaffId) { this.managerStaffId = managerStaffId; }

    public String getManagerFullName() { return managerFullName; }
    public void setManagerFullName(String managerFullName) { this.managerFullName = managerFullName; }

    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}

