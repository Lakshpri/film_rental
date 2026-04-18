package com.example.film_rental_app.location_store_staffmodule.mapper;


import com.example.film_rental_app.location_store_staffmodule.dto.request.AddressRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.AddressResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.master_datamodule.entity.City;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequestDTO dto) {
        Address address = new Address();
        address.setAddress(dto.getAddress());
        address.setAddress2(dto.getAddress2());
        address.setDistrict(dto.getDistrict());
        address.setPostalCode(dto.getPostalCode());
        address.setPhone(dto.getPhone());
        City city = new City();
        city.setCityId(dto.getCityId());
        address.setCity(city);
        return address;
    }

    public AddressResponseDTO toResponseDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setAddressId(address.getAddressId());
        dto.setAddress(address.getAddress());
        dto.setAddress2(address.getAddress2());
        dto.setDistrict(address.getDistrict());
        dto.setPostalCode(address.getPostalCode());
        dto.setPhone(address.getPhone());
        dto.setLastUpdate(address.getLastUpdate());
        if (address.getCity() != null) {
            dto.setCityId(address.getCity().getCityId());
            dto.setCityName(address.getCity().getCity());
            if (address.getCity().getCountry() != null) {
                dto.setCountryId(address.getCity().getCountry().getCountryId());
                dto.setCountryName(address.getCity().getCountry().getCountry());
            }
        }
        return dto;
    }

    public void updateEntity(Address address, AddressRequestDTO dto) {
        address.setAddress(dto.getAddress());
        address.setAddress2(dto.getAddress2());
        address.setDistrict(dto.getDistrict());
        address.setPostalCode(dto.getPostalCode());
        address.setPhone(dto.getPhone());
    }
}

