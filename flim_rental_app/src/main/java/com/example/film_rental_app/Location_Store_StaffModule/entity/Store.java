 package com.example.film_rental_app.Location_Store_StaffModule.entity;

 import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Customer;
 import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Inventory;
 import jakarta.persistence.*;
 import org.hibernate.annotations.UpdateTimestamp;

 import java.time.LocalDateTime;
 import java.util.HashSet;
 import java.util.Set;

 @Entity
 @Table(name = "store")
 public class Store {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "store_id")
     private Integer storeId;

     @UpdateTimestamp
     @Column(name = "last_update", nullable = false)
     private LocalDateTime lastUpdate;

     @OneToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "manager_staff_id", nullable = false, unique = true,
             foreignKey = @ForeignKey(name = "fk_store_staff"))
     private Staff managerStaff;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "address_id", nullable = false,
             foreignKey = @ForeignKey(name = "fk_store_address"))
     private Address address;

     @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
     private Set<Staff> staffList = new HashSet<>();

     @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
     private Set<Customer> customers = new HashSet<>();

     @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
     private Set<Inventory> inventories = new HashSet<>();

     //  NoArgsConstructor
     public Store() {}

     //  AllArgsConstructor
     public Store(Integer storeId, LocalDateTime lastUpdate, Staff managerStaff,
                  Address address, Set<Staff> staffList,
                  Set<Customer> customers, Set<Inventory> inventories) {
         this.storeId = storeId;
         this.lastUpdate = lastUpdate;
         this.managerStaff = managerStaff;
         this.address = address;
         this.staffList = staffList;
         this.customers = customers;
         this.inventories = inventories;
     }

     //  Getters & Setters

     public Integer getStoreId() {
         return storeId;
     }

     public void setStoreId(Integer storeId) {
         this.storeId = storeId;
     }

     public LocalDateTime getLastUpdate() {
         return lastUpdate;
     }

     public Staff getManagerStaff() {
         return managerStaff;
     }

     public void setManagerStaff(Staff managerStaff) {
         this.managerStaff = managerStaff;
     }

     public Address getAddress() {
         return address;
     }

     public void setAddress(Address address) {
         this.address = address;
     }

     public Set<Staff> getStaffList() {
         return staffList;
     }

     public void setStaffList(Set<Staff> staffList) {
         this.staffList = staffList;
     }

     public Set<Customer> getCustomers() {
         return customers;
     }

     public void setCustomers(Set<Customer> customers) {
         this.customers = customers;
     }

     public Set<Inventory> getInventories() {
         return inventories;
     }

     public void setInventories(Set<Inventory> inventories) {
         this.inventories = inventories;
     }

     //  toString (exclude relationships to avoid recursion)
     @Override
     public String toString() {
         return "Store{" +
                 "storeId=" + storeId +
                 ", lastUpdate=" + lastUpdate +
                 '}';
     }
 }