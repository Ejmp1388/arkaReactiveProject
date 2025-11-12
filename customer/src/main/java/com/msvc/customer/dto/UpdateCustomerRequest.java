package com.msvc.customer.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateCustomerRequest {
    @NotBlank private String name;
    @NotBlank private String country;
    @NotBlank private String state;
    @NotBlank private String district;
    @NotBlank private String city;
    @NotBlank private String address;

    //@Pattern(regexp = "^(\\+\\d{1,3}\\s?)?\\d{4,5}[- ]?\\d{4}$",
    //        message = "Formato de teléfono inválido")
    private String phone;

    private String email;

    // getters/setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
