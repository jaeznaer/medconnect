package com.mycompany.medsconnect;

import java.util.ArrayList;

/**
 * Created by joel on 28-02-2016.
 */

public class Medicine {
    String manufacturer;
    String brand;

    String category;
    String d_class;
    String unit_qty;
    String unit_type;
    String package_qty;
    String package_type;
    String package_price;
    String unit_price;
    String generic_id;

    String constituentName;
    String constituentStrength;

    private static Medicine medObject = null;
    private Medicine(){}
    public static Medicine getInstance() {
        if (medObject == null)
            medObject = new Medicine();
        return medObject;
    }
    private String[] medSugg;
    public void setMyData(String[] temp){
        this.medSugg = temp;
    }

    public String[] getMyData(){
        return medSugg;
    }
    private ArrayList<String> medDetails;
    public void setMedDetails(ArrayList<String> temp){
        this.medDetails = temp;
    }
    public ArrayList<String> getMedDetails(){
        return medDetails;
    }
    private ArrayList<String> altMed;
    public void setAltMed(ArrayList<String> temp){
        this.altMed = temp;
    }
    public ArrayList<String> getAltMed(){
        return altMed;
    }

    private String productName;
    public void setProductName(String temp){
        this.productName = temp;
    }
    public String getProductName(){
        return productName;
    }

    private String customerEmail;
    public void setCustomerEmail(String temp){
        this.customerEmail = temp;
    }
    public String getCustomerEmail(){
        return customerEmail;
    }

    private String orderDetails;
    public void setOrderDetails(String temp){
        this.orderDetails = temp;
    }
    public String getOrderDetails(){
        return orderDetails;
    }


    private String orderid;
    public void setOrderid(String orderid) {this.orderid = orderid;}
    public String getOrderid(){ return orderid;}


    public String getConstituentName() {
        return constituentName;
    }
    public void setConstituentName(String constituentName) {
        this.constituentName = constituentName;
    }

    public String getConstituentStrength() {
        return constituentStrength;
    }
    public void setConstituentStrength(String constituentStrength) {
        this.constituentStrength = constituentStrength;
    }


    private Boolean uploadFlag = false;
    public void setUploadFlag(Boolean flag) {this.uploadFlag = flag;}
    public Boolean getUploadFlag(){return uploadFlag;}
    private String imageUrl;
    public void setImageUrl(String url) {this.imageUrl = url;}
    public String getImageUrl(){return imageUrl;}


    //Ticker
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    //Name
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    //Price
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    //Change
    public String getDClass() {
        return d_class;
    }
    public void setDClass(String d_class) {
        this.d_class = d_class;
    }
    //DayHigh
    public String getUnitType() {
        return unit_type;
    }
    public void setUnitType(String unit_type) {
        this.unit_type = unit_type;
    }
    //DayLow
    public String getUnitQty() {
        return unit_qty;
    }
    public void setUnitQty(String unit_qty) {
        this.unit_qty= unit_qty;
    }
    //YearHigh
    public String getPackageType() {
        return package_type;
    }
    public void setPackageType(String package_type) {
        this.package_type = package_type;
    }
    //YearLow
    public String getPackageQty() {
        return package_qty;
    }
    public void setPackageQty(String package_qty) {
        this.package_qty= package_qty;
    }

    public String getPackagePrice() {
        return package_price;
    }
    public void setPackagePrice(String package_price) {
        this.package_price= package_price;
    }
    //prev close
    public String getUnitPrice() {
        return unit_price;
    }
    public void setUnitPrice(String unit_price) {
        this.unit_price= unit_price;
    }
    //Change Percent
    public String getGenericId() {
        return generic_id;
    }
    public void setGenericId(String generic_id) {
        this.generic_id= generic_id;
    }


}
