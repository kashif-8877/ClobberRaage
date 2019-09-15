package com.example.clobberrage.Model;

public class Products
{
    public String brand,category,date,time,pid,pimage,pname,price;

    public Products()
    {

    }

    public Products(String brand, String category, String date, String time, String pid, String pimage, String pname, String price) {
        this.brand = brand;
        this.category = category;
        this.date = date;
        this.time = time;
        this.pid = pid;
        this.pimage = pimage;
        this.pname = pname;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
