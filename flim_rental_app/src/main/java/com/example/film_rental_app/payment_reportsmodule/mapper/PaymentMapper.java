package com.example.film_rental_app.payment_reportsmodule.mapper;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.payment_reportsmodule.dto.request.PaymentRequestDTO;
import com.example.film_rental_app.payment_reportsmodule.dto.response.PaymentResponseDTO;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        payment.setCustomer(customer);
        Staff staff = new Staff();
        staff.setStaffId(dto.getStaffId());
        payment.setStaff(staff);
        if (dto.getRentalId() != null) {
            Rental rental = new Rental();
            rental.setRentalId(dto.getRentalId());
            payment.setRental(rental);
        }
        return payment;
    }

    public void updateEntity(Payment payment, PaymentRequestDTO dto) {
        if (dto.getAmount() != null) {
            payment.setAmount(dto.getAmount());
        }
        if (dto.getPaymentDate() != null) {
            payment.setPaymentDate(dto.getPaymentDate());
        }
        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(dto.getCustomerId());
            payment.setCustomer(customer);
        }
        if (dto.getStaffId() != null) {
            Staff staff = new Staff();
            staff.setStaffId(dto.getStaffId());
            payment.setStaff(staff);
        }
        if (dto.getRentalId() != null) {
            Rental rental = new Rental();
            rental.setRentalId(dto.getRentalId());
            payment.setRental(rental);
        }
    }

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setLastUpdate(payment.getLastUpdate());
        if (payment.getCustomer() != null) {
            dto.setCustomerId(payment.getCustomer().getCustomerId());
            dto.setCustomerName(payment.getCustomer().getFirstName() + " " + payment.getCustomer().getLastName());
        }
        if (payment.getStaff() != null) {
            dto.setStaffId(payment.getStaff().getStaffId());
            dto.setStaffName(payment.getStaff().getFirstName() + " " + payment.getStaff().getLastName());
        }
        if (payment.getRental() != null) {
            dto.setRentalId(payment.getRental().getRentalId());
        }
        return dto;
    }
}


