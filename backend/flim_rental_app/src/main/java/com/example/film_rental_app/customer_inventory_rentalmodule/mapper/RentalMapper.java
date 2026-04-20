package com.example.film_rental_app.customer_inventory_rentalmodule.mapper;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.RentalRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.RentalResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public Rental toEntity(RentalRequestDTO dto) {
        Rental rental = new Rental();
        rental.setRentalDate(dto.getRentalDate());
        rental.setReturnDate(dto.getReturnDate());
        Inventory inventory = new Inventory();
        inventory.setInventoryId(dto.getInventoryId());
        rental.setInventory(inventory);
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        rental.setCustomer(customer);
        Staff staff = new Staff();
        staff.setStaffId(dto.getStaffId());
        rental.setStaff(staff);
        return rental;
    }

    public void updateEntity(Rental rental, RentalRequestDTO dto) {
        if (dto.getRentalDate() != null) {
            rental.setRentalDate(dto.getRentalDate());
        }
        rental.setReturnDate(dto.getReturnDate());
        if (dto.getInventoryId() != null) {
            Inventory inventory = new Inventory();
            inventory.setInventoryId(dto.getInventoryId());
            rental.setInventory(inventory);
        }
        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(dto.getCustomerId());
            rental.setCustomer(customer);
        }
        if (dto.getStaffId() != null) {
            Staff staff = new Staff();
            staff.setStaffId(dto.getStaffId());
            rental.setStaff(staff);
        }
    }

    public RentalResponseDTO toResponseDTO(Rental rental) {
        RentalResponseDTO dto = new RentalResponseDTO();
        dto.setRentalId(rental.getRentalId());
        dto.setRentalDate(rental.getRentalDate());
        dto.setReturnDate(rental.getReturnDate());
        dto.setLastUpdate(rental.getLastUpdate());
        if (rental.getInventory() != null) {
            dto.setInventoryId(rental.getInventory().getInventoryId());
            if (rental.getInventory().getFilm() != null) {
                dto.setFilmId(rental.getInventory().getFilm().getFilmId());
                dto.setFilmTitle(rental.getInventory().getFilm().getTitle());
            }
        }
        if (rental.getCustomer() != null) {
            dto.setCustomerId(rental.getCustomer().getCustomerId());
            dto.setCustomerName(rental.getCustomer().getFirstName() + " " + rental.getCustomer().getLastName());
        }
        if (rental.getStaff() != null) {
            dto.setStaffId(rental.getStaff().getStaffId());
            dto.setStaffName(rental.getStaff().getFirstName() + " " + rental.getStaff().getLastName());
        }
        return dto;
    }
}
