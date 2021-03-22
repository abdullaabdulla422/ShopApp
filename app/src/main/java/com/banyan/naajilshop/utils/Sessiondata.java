package com.banyan.naajilshop.utils;

import java.util.ArrayList;

public class Sessiondata {

    private static Sessiondata instance;
    //home screen
    private int districtId;
    private String district;
    private String categoryId;

    //product datas
    private String productId;
    private String name;
    private String description;
    private String originalPrice;
    private String discontPrice;
    private String discountPercentage;
    private String stock;
    private String capacity;
    private String weight;
    private String image;
    private ArrayList<String> subimage=new ArrayList<>();
    private String imagePath;

    //cart
    private String totalAmount;


    //products
    private int productCount;




    public static Sessiondata getInstance() {
        if (instance == null) {
            instance = new Sessiondata();
        }
        return instance;

    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    //product screen

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscontPrice() {
        return discontPrice;
    }

    public void setDiscontPrice(String discontPrice) {
        this.discontPrice = discontPrice;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public ArrayList<String> getSubimage() {
        return subimage;
    }

    public void setSubimage(ArrayList<String> subimage) {
        this.subimage = subimage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}

