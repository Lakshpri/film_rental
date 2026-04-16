package com.example.film_rental_app.location_store_staffmodule.service;

import com.example.film_rental_app.location_store_staffmodule.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores();

    Store getStoreById(Integer storeId);

    Store createStore(Store store);

    Store updateStore(Integer storeId, Store updated);

    void deleteStore(Integer storeId);
}
