package com.example.film_rental_app.location_store_staffmodule.controller;

import com.example.film_rental_app.location_store_staffmodule.dto.request.AddressRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.AddressResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.mapper.AddressMapper;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private CityService cityService;
    @Autowired
    private AddressMapper addressMapper;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        List<AddressResponseDTO> result = addressService.getAllAddresses().stream()
                .map(addressMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Integer addressId) {
        return ResponseEntity.ok(addressMapper.toResponseDTO(addressService.getAddressById(addressId)));
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO dto) {
        Address address = addressMapper.toEntity(dto);
        City city = cityService.getCityById(dto.getCityId());
        address.setCity(city);
        return ResponseEntity.status(201).body(addressMapper.toResponseDTO(addressService.createAddress(address)));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Integer addressId,
                                                            @Valid @RequestBody AddressRequestDTO dto) {
        Address existing = addressService.getAddressById(addressId);
        addressMapper.updateEntity(existing, dto);
        if (dto.getCityId() != null) {
            City city = cityService.getCityById(dto.getCityId());
            existing.setCity(city);
        }
        return ResponseEntity.ok(addressMapper.toResponseDTO(addressService.updateAddress(addressId, existing)));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
