package com.example.film_rental_app.location_store_staffmodule.entity;


import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Size(max = 50)
    @Column(name = "address2", length = 50)
    private String address2;

    @NotBlank
    @Size(max = 20)
    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @NotBlank
    @Size(max = 20)
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_address_city"))
    private City city;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Staff> staffList = new HashSet<>();

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Store> stores = new HashSet<>();

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public @NotBlank @Size(max = 50) String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank @Size(max = 50) String address) {
        this.address = address;
    }

    public @Size(max = 50) String getAddress2() {
        return address2;
    }

    public void setAddress2(@Size(max = 50) String address2) {
        this.address2 = address2;
    }

    public @NotBlank @Size(max = 20) String getDistrict() {
        return district;
    }

    public void setDistrict(@NotBlank @Size(max = 20) String district) {
        this.district = district;
    }

    public @Size(max = 10) String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@Size(max = 10) String postalCode) {
        this.postalCode = postalCode;
    }

    public @NotBlank @Size(max = 20) String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank @Size(max = 20) String phone) {
        this.phone = phone;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(Set<Staff> staffList) {
        this.staffList = staffList;
    }

    public Set<Store> getStores() {
        return stores;
    }

    public void setStores(Set<Store> stores) {
        this.stores = stores;
    }

    public Address(Integer addressId, String address, String address2, String district, String postalCode, String phone, LocalDateTime lastUpdate, City city, Set<Customer> customers, Set<Staff> staffList, Set<Store> stores) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.district = district;
        this.postalCode = postalCode;
        this.phone = phone;
        this.lastUpdate = lastUpdate;
        this.city = city;
        this.customers = customers;
        this.staffList = staffList;
        this.stores = stores;
    }
    public Address(){}

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", address='" + address + '\'' +
                ", address2='" + address2 + '\'' +
                ", district='" + district + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phone='" + phone + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", city=" + city +
                ", customers=" + customers +
                ", staffList=" + staffList +
                ", stores=" + stores +
                '}';
    }
}
