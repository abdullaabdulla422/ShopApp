package com.banyan.naajilshop.model;

import java.util.ArrayList;

/**
 * Created by Jo
 */
public class Product_Model {

    private String product_id, name, description, original_price, discount_percentage, discount_price, stock,
            stockstatus, image, capacity, category_id, key, product_total_mrp;
    private ArrayList<String>imagePath;

    public Product_Model() {
    }

    public Product_Model(String product_id, String name, String description, String original_price, String discount_price,
                         String discount_percentage, String stock, String stockstatus,
                         String image, String capacity, String category_id, String key, String total_mrp, ArrayList<String> imagePath) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.original_price = original_price;
        this.discount_price = discount_price;
        this.discount_percentage = discount_percentage;
        this.stock = stock;
        this.stockstatus = stockstatus;
        this.image = image;
        this.capacity = capacity;
        this.category_id = category_id;
        this.key = key;
        this.product_total_mrp = total_mrp;
        this.imagePath = imagePath;

    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }


    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStockstatus() {
        return stockstatus;
    }

    public void setStockstatus(String stockstatus) {
        this.stockstatus = stockstatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProduct_total_mrp() {
        return product_total_mrp;
    }

    public void setProduct_total_mrp(String product_total_mrp) {
        this.product_total_mrp = product_total_mrp;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }
}

