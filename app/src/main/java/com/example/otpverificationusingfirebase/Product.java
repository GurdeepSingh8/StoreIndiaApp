package com.example.otpverificationusingfirebase;

import android.content.Context;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

    @Exclude private String id;
    ArrayList<String> Uid = new ArrayList<>();
    ArrayList<String> Tags = new ArrayList<String>();
    int oldPrice;
    String image, name, newPrice, customerId;
    ArrayList<String> quantity1 = new ArrayList<String>();
    ArrayList<String> quantity2 = new ArrayList<String>();



    public Product() {
    }

    public Product(ArrayList<String> Uid,

                   ArrayList<String> Tags, String image, String name, String newPrice, int oldPrice, String customerId, ArrayList<String> quantityKg, ArrayList<String> quantityGms) {

        this.Uid = Uid;
        this.Tags = Tags;
        this.image = image;
        this.name = name;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.customerId = customerId;
        this.quantity1 = quantityKg;
        this.quantity2 = quantityGms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getUid() {
        return Uid;
    }

    public void setUid(ArrayList<String> uid) {
        Uid = uid;
    }

    public ArrayList<String> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<String> tags) {
        Tags = tags;
    }

    public ArrayList<String> getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(ArrayList<String> quantity2) {
        this.quantity2 = quantity2;
    }

    public ArrayList<String> getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(ArrayList<String> quantity1) {
        this.quantity1 = quantity1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}