package com.example.film_rental_app.location_store_staffmodule.service.implementation;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.exception.AddressNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.AddressRepository;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CityRepository cityRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Integer addressId, Address updated) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        address.setAddress(updated.getAddress());
        address.setAddress2(updated.getAddress2());
        address.setDistrict(updated.getDistrict());
        address.setPostalCode(updated.getPostalCode());
        address.setPhone(updated.getPhone());
        if (updated.getCity() != null) address.setCity(updated.getCity());
        return addressRepository.save(address);
    }

    @Override
    public boolean deleteAddress(Integer addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException(addressId);
        }
        addressRepository.deleteById(addressId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAddressesByCity(Integer cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new ResourceNotFoundException("City", cityId);
        }
        return addressRepository.findByCity_CityId(cityId);
    }
}

