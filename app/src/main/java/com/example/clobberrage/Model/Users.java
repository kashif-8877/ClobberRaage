package com.example.clobberrage.Model;

public class Users
{
    private String Name,Password,Phone,image,address;

    public Users() {
    }

    public Users(String name, String password, String phone, String image, String address) {
        Name = name;
        Password = password;
        Phone = phone;
        this.image = image;
        this.address = address;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
