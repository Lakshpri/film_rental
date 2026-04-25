package com.example.film_rental_app.location_store_staffmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.InventoryRepository;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.exception.StoreAlreadyExistsException;
import com.example.film_rental_app.location_store_staffmodule.exception.StoreInvalidOperationException;
import com.example.film_rental_app.location_store_staffmodule.exception.StoreNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.StaffRepository;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    @Autowired private StoreRepository     storeRepository;
    @Autowired private CustomerRepository  customerRepository;
    @Autowired private StaffRepository     staffRepository;
    @Autowired private InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Store getStoreById(Integer storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
    }

    @Override
    public Store createStore(Store store) {
        if (store.getAddress() != null
                && storeRepository.existsByAddress_AddressId(store.getAddress().getAddressId())) {
            throw new StoreAlreadyExistsException(store.getAddress().getAddressId());
        }
        if (store.getManagerStaff() != null &&
                storeRepository.existsByManagerStaff_StaffId(store.getManagerStaff().getStaffId())) {

            throw new StoreAlreadyExistsException(
                    store.getManagerStaff().getStaffId(), true
            );
        }
        return storeRepository.save(store);
    }

    @Override
    public Store updateStore(Integer storeId, Store updated) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
        if (updated.getAddress() != null
                && (store.getAddress() == null
                || !store.getAddress().getAddressId().equals(updated.getAddress().getAddressId()))
                && storeRepository.existsByAddress_AddressId(updated.getAddress().getAddressId())) {
            throw new StoreAlreadyExistsException(updated.getAddress().getAddressId());
        }
        if (updated.getManagerStaff() != null) store.setManagerStaff(updated.getManagerStaff());
        if (updated.getAddress()      != null) store.setAddress(updated.getAddress());
        return storeRepository.save(store);
    }

    @Override
    public boolean deleteStore(Integer storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }
        // Block if customers are registered to this store
        if (customerRepository.existsByStore_StoreId(storeId)) {
            throw new StoreInvalidOperationException(storeId,
                    "This store still has customers registered to it. "
                            + "Please reassign all customers to another store before deleting this one.");
        }
        // Block if staff members belong to this store
        if (staffRepository.existsByStore_StoreId(storeId)) {
            throw new StoreInvalidOperationException(storeId,
                    "This store still has staff members assigned to it. "
                            + "Please reassign all staff to another store before deleting this one.");
        }
        // Block if inventory items exist in this store
        if (!inventoryRepository.findByStore_StoreId(storeId).isEmpty()) {
            throw new StoreInvalidOperationException(storeId,
                    "This store still has inventory items. "
                            + "Please remove all inventory from this store before deleting it.");
        }
        storeRepository.deleteById(storeId);
        return true;
    }
}
