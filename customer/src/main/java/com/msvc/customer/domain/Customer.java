package com.msvc.customer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.Instant;

@Table("customers")
public class Customer {
    @Id
    private String id;
    private String name;
    private String country;
    private String state;
    private String district;
    private String city;
    private String address;

    private String phone;

    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;
    @Column("active")
    private boolean active;

    private String email;

    public Customer() { }

    // getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

