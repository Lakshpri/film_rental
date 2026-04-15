package com.example.film_rental_app.Location_Store_StaffModule.controller;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import com.example.film_rental_app.Location_Store_StaffModule.repository.AddressRepository;
import com.example.film_rental_app.Master_DataModule.repository.CityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    public AddressController(AddressRepository addressRepository, CityRepository cityRepository) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/api/addresses")
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @GetMapping("/api/addresses/{addressId}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer addressId) {
        return addressRepository.findById(addressId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/cities/{cityId}/addresses")
    public ResponseEntity<List<Address>> getAddressesByCity(@PathVariable Integer cityId) {
        if (!cityRepository.existsById(cityId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(addressRepository.findByCity_CityId(cityId));
    }

    @PostMapping("/api/addresses")
    public Address createAddress(@RequestBody Address address) {
        return addressRepository.save(address);
    }

    @PutMapping("/api/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer addressId, @RequestBody Address updated) {
        return addressRepository.findById(addressId).map(addr -> {
            addr.setAddress(updated.getAddress());
            addr.setAddress2(updated.getAddress2());
            addr.setDistrict(updated.getDistrict());
            addr.setPostalCode(updated.getPostalCode());
            addr.setPhone(updated.getPhone());
            if (updated.getCity() != null) addr.setCity(updated.getCity());
            return ResponseEntity.ok(addressRepository.save(addr));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
        if (!addressRepository.existsById(addressId)) return ResponseEntity.notFound().build();
        addressRepository.deleteById(addressId);
        return ResponseEntity.noContent().build();
    }

}
