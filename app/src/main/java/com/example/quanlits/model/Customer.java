package com.example.quanlits.model;

public class Customer {
    private String name;
    private String email;
    private String phone;
    private int imageResource;

    public Customer(String name, String email, String phone, int imageResource) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageResource = imageResource;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getImageResource() {
        return imageResource;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
