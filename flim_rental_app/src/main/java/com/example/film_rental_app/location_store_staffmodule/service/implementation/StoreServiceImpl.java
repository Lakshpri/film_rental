package com.example.film_rental_app.location_store_staffmodule.service.implementation;

import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.exception.StoreNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

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
        return storeRepository.save(store);
    }

    @Override
    public Store updateStore(Integer storeId, Store updated) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
        if (updated.getManagerStaff() != null) store.setManagerStaff(updated.getManagerStaff());
        if (updated.getAddress() != null) store.setAddress(updated.getAddress());
        return storeRepository.save(store);
    }

    @Override
    public void deleteStore(Integer storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }
        storeRepository.deleteById(storeId);
    }
}

