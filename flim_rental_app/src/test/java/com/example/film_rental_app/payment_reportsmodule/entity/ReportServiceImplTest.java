package com.example.film_rental_app.payment_reportsmodule.entity;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.CustomerResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.CustomerMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.ActorResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.ActorMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StaffResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StoreResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.mapper.StaffMapper;
import com.example.film_rental_app.location_store_staffmodule.mapper.StoreMapper;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import com.example.film_rental_app.master_datamodule.dto.response.CategoryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.mapper.CategoryMapper;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import com.example.film_rental_app.payment_reportsmodule.service.implementation.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    // ReportServiceImpl depends on all six service+mapper pairs
    @Mock private CustomerService  customerService;
    @Mock private CustomerMapper   customerMapper;

    @Mock private FilmService      filmService;
    @Mock private FilmMapper       filmMapper;

    @Mock private StaffService     staffService;
    @Mock private StaffMapper      staffMapper;

    @Mock private StoreService     storeService;
    @Mock private StoreMapper      storeMapper;

    @Mock private CategoryService  categoryService;
    @Mock private CategoryMapper   categoryMapper;

    @Mock private ActorService     actorService;
    @Mock private ActorMapper      actorMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    // ======================================================
    // getCustomerList
    // ======================================================

    @Test
    void testGetCustomerList_returnsCorrectCount() {
        Customer c1 = new Customer(); Customer c2 = new Customer();
        CustomerResponseDTO dto1 = new CustomerResponseDTO();
        CustomerResponseDTO dto2 = new CustomerResponseDTO();

        when(customerService.getAllCustomers()).thenReturn(List.of(c1, c2));
        when(customerMapper.toResponseDTO(c1)).thenReturn(dto1);
        when(customerMapper.toResponseDTO(c2)).thenReturn(dto2);

        Map<String, Object> result = reportService.getCustomerList();

        assertEquals(2, result.get("totalCount"));
        assertEquals(2, ((List<?>) result.get("customers")).size());
    }

    @Test
    void testGetCustomerList_emptyList_returnsZeroCount() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        Map<String, Object> result = reportService.getCustomerList();

        assertEquals(0, result.get("totalCount"));
        assertTrue(((List<?>) result.get("customers")).isEmpty());
    }

    @Test
    void testGetCustomerList_containsExpectedKeys() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        Map<String, Object> result = reportService.getCustomerList();

        assertTrue(result.containsKey("totalCount"));
        assertTrue(result.containsKey("customers"));
    }

    @Test
    void testGetCustomerList_resultNotNull() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        assertNotNull(reportService.getCustomerList());
    }

    // ======================================================
    // getFilmList
    // ======================================================

    @Test
    void testGetFilmList_returnsCorrectCount() {
        Film f1 = new Film(); Film f2 = new Film(); Film f3 = new Film();
        FilmResponseDTO dto1 = new FilmResponseDTO();
        FilmResponseDTO dto2 = new FilmResponseDTO();
        FilmResponseDTO dto3 = new FilmResponseDTO();

        when(filmService.getAllFilms()).thenReturn(List.of(f1, f2, f3));
        when(filmMapper.toResponseDTO(f1)).thenReturn(dto1);
        when(filmMapper.toResponseDTO(f2)).thenReturn(dto2);
        when(filmMapper.toResponseDTO(f3)).thenReturn(dto3);

        Map<String, Object> result = reportService.getFilmList();

        assertEquals(3, result.get("totalCount"));
        assertEquals(3, ((List<?>) result.get("films")).size());
    }

    @Test
    void testGetFilmList_emptyList_returnsZeroCount() {
        when(filmService.getAllFilms()).thenReturn(List.of());

        Map<String, Object> result = reportService.getFilmList();

        assertEquals(0, result.get("totalCount"));
        assertTrue(((List<?>) result.get("films")).isEmpty());
    }

    @Test
    void testGetFilmList_containsExpectedKeys() {
        when(filmService.getAllFilms()).thenReturn(List.of());

        Map<String, Object> result = reportService.getFilmList();

        assertTrue(result.containsKey("totalCount"));
        assertTrue(result.containsKey("films"));
    }

    @Test
    void testGetFilmList_resultNotNull() {
        when(filmService.getAllFilms()).thenReturn(List.of());

        assertNotNull(reportService.getFilmList());
    }

    // ======================================================
    // getStaffList
    // ======================================================

    @Test
    void testGetStaffList_returnsCorrectCount() {
        Staff s = new Staff();
        StaffResponseDTO dto = new StaffResponseDTO();

        when(staffService.getAllStaff()).thenReturn(List.of(s));
        when(staffMapper.toResponseDTO(s)).thenReturn(dto);

        Map<String, Object> result = reportService.getStaffList();

        assertEquals(1, result.get("totalCount"));
        assertEquals(1, ((List<?>) result.get("staff")).size());
    }

    @Test
    void testGetStaffList_emptyList_returnsZeroCount() {
        when(staffService.getAllStaff()).thenReturn(List.of());

        Map<String, Object> result = reportService.getStaffList();

        assertEquals(0, result.get("totalCount"));
        assertTrue(((List<?>) result.get("staff")).isEmpty());
    }

    @Test
    void testGetStaffList_containsExpectedKeys() {
        when(staffService.getAllStaff()).thenReturn(List.of());

        Map<String, Object> result = reportService.getStaffList();

        assertTrue(result.containsKey("totalCount"));
        assertTrue(result.containsKey("staff"));
    }

    // ======================================================
    // getSalesByStore
    // ======================================================

    @Test
    void testGetSalesByStore_returnsCorrectCount() {
        Store st1 = new Store(); Store st2 = new Store();
        StoreResponseDTO dto1 = new StoreResponseDTO();
        StoreResponseDTO dto2 = new StoreResponseDTO();

        when(storeService.getAllStores()).thenReturn(List.of(st1, st2));
        when(storeMapper.toResponseDTO(st1)).thenReturn(dto1);
        when(storeMapper.toResponseDTO(st2)).thenReturn(dto2);

        Map<String, Object> result = reportService.getSalesByStore();

        assertEquals(2, result.get("totalCount"));
        assertEquals(2, ((List<?>) result.get("stores")).size());
    }

    @Test
    void testGetSalesByStore_emptyList_returnsZeroCount() {
        when(storeService.getAllStores()).thenReturn(List.of());

        Map<String, Object> result = reportService.getSalesByStore();

        assertEquals(0, result.get("totalCount"));
        assertTrue(((List<?>) result.get("stores")).isEmpty());
    }

    @Test
    void testGetSalesByStore_containsExpectedKeys() {
        when(storeService.getAllStores()).thenReturn(List.of());

        Map<String, Object> result = reportService.getSalesByStore();

        assertTrue(result.containsKey("totalCount"));
        assertTrue(result.containsKey("stores"));
    }

    // ======================================================
    // getSalesByCategory
    // ======================================================

    @Test
    void testGetSalesByCategory_returnsCorrectCount() {
        Category cat = new Category();
        CategoryResponseDTO dto = new CategoryResponseDTO();

        when(categoryService.getAllCategories()).thenReturn(List.of(cat));
        when(categoryMapper.toResponseDTO(cat)).thenReturn(dto);

        Map<String, Object> result = reportService.getSalesByCategory();

        assertEquals(1, result.get("totalCount"));
        assertEquals(1, ((List<?>) result.get("categories")).size());
    }

    @Test
    void testGetSalesByCategory_emptyList_returnsZeroCount() {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        Map<String, Object> result = reportService.getSalesByCategory();

        assertEquals(0, result.get("totalCount"));
        assertTrue(((List<?>) result.get("categories")).isEmpty());
    }

    @Test
    void testGetSalesByCategory_containsExpectedKeys() {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        Map<String, Object> result = reportService.getSalesByCategory();

        assertTrue(result.containsKey("totalCount"));
        assertTrue(result.containsKey("categories"));
    }

    // ======================================================
    // getActorInfo
    // ======================================================

    @Test
    void testGetActorInfo_returnsCorrectCount() {
        Actor a1 = new Actor(); Actor a2 = new Actor();
        ActorResponseDTO dto1 = new ActorResponseDTO();
        ActorResponseDTO dto2 = new ActorResponseDTO();

        when(actorService.getAllActors()).thenReturn(List.of(a1, a2));
        when(actorMapper.toResponseDTO(a1)).thenReturn(dto1);
        when(actorMapper.toResponseDTO(a2)).thenReturn(dto2);

        Map<String, Object> result = reportService.getActorInfo();

        assertEquals(2, result.get("totalCount"));
        assertEquals(2, ((List<?>) result.get("actors")).size());
    }

    @Test
    void testGetActorInfo_emptyList_returnsZeroCount() {
        when(actorService.getAllActors()).thenReturn(List.of());

        Map<String, Object> result = reportService.getActorInfo();

        assertEquals(0, result.get("totalCount"));
        assertTrue(((List<?>) result.get("actors")).isEmpty());
    }

    @Test
    void testGetActorInfo_containsExpectedKeys() {
        when(actorService.getAllActors()).thenReturn(List.of());

        Map<String, Object> result = reportService.getActorInfo();

        assertTrue(result.containsKey("totalCount"));
        assertTrue(result.containsKey("actors"));
    }

    @Test
    void testGetActorInfo_resultNotNull() {
        when(actorService.getAllActors()).thenReturn(List.of());

        assertNotNull(reportService.getActorInfo());
    }
}
