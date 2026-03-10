package com.example.quanlits.model;

public class Staff {
    private int id;
    private String name;
    private String gender;
    private String phone;

    public Staff(int id, String name, String gender, String phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
}
