package com.iBring_user.app.Models;

import java.io.Serializable;

public class FoodHistoryModel implements Serializable {
    String id,rest_id,rest_name,rest_loc,rest_latt,rest_long,delivery_id,delivery_name,delivery_image,delivery_contact,items,price,destination,dest_lat,dest_long,
    date_time,food_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_loc() {
        return rest_loc;
    }

    public void setRest_loc(String rest_loc) {
        this.rest_loc = rest_loc;
    }

    public String getRest_latt() {
        return rest_latt;
    }

    public void setRest_latt(String rest_latt) {
        this.rest_latt = rest_latt;
    }

    public String getRest_long() {
        return rest_long;
    }

    public void setRest_long(String rest_long) {
        this.rest_long = rest_long;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_image() {
        return delivery_image;
    }

    public void setDelivery_image(String delivery_image) {
        this.delivery_image = delivery_image;
    }

    public String getDelivery_contact() {
        return delivery_contact;
    }

    public void setDelivery_contact(String delivery_contact) {
        this.delivery_contact = delivery_contact;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(String dest_lat) {
        this.dest_lat = dest_lat;
    }

    public String getDest_long() {
        return dest_long;
    }

    public void setDest_long(String dest_long) {
        this.dest_long = dest_long;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }
}
