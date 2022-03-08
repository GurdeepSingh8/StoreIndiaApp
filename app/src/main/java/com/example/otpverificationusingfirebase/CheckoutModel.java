package com.example.otpverificationusingfirebase;

public class CheckoutModel {
    String name, price, quantity, total;

    public CheckoutModel() {
    }

    public CheckoutModel(String name, String price, String quantity, String total) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

