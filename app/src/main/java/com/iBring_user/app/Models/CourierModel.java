package com.iBring_user.app.Models;

import java.io.Serializable;

public class CourierModel implements Serializable {

    String id,weight,parcelType,senderName,senderEmail,senderContact,senderLocation,senderLatt,senderLong,reciverName,receiverEmail,receiverContact,
            receiverAddress,receiverLatt,receiverLng,isFragile,image,city,price,specialNote,deliveryNote,orderStatus,order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getParcelType() {
        return parcelType;
    }

    public void setParcelType(String parcelType) {
        this.parcelType = parcelType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderContact() {
        return senderContact;
    }

    public void setSenderContact(String senderContact) {
        this.senderContact = senderContact;
    }

    public String getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(String senderLocation) {
        this.senderLocation = senderLocation;
    }

    public String getSenderLatt() {
        return senderLatt;
    }

    public void setSenderLatt(String senderLatt) {
        this.senderLatt = senderLatt;
    }

    public String getSenderLong() {
        return senderLong;
    }

    public void setSenderLong(String senderLong) {
        this.senderLong = senderLong;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverLatt() {
        return receiverLatt;
    }

    public void setReceiverLatt(String receiverLatt) {
        this.receiverLatt = receiverLatt;
    }

    public String getReceiverLng() {
        return receiverLng;
    }

    public void setReceiverLng(String receiverLng) {
        this.receiverLng = receiverLng;
    }

    public String getIsFragile() {
        return isFragile;
    }

    public void setIsFragile(String isFragile) {
        this.isFragile = isFragile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }
}
