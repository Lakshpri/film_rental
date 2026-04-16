package com.example.film_rental_app.Location_Store_StaffModule.service;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();

    Address getAddressById(Integer addressId);

    Address createAddress(Address address);

    Address updateAddress(Integer addressId, Address updated);

    void deleteAddress(Integer addressId);

    List<Address> getAddressesByCity(Integer cityId);
}
