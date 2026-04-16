package com.example.film_rental_app.location_store_staffmodule.service;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();

    Address getAddressById(Integer addressId);

    Address createAddress(Address address);

    Address updateAddress(Integer addressId, Address updated);

    void deleteAddress(Integer addressId);

    List<Address> getAddressesByCity(Integer cityId);
}
