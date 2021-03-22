package com.banyan.naajilshop.model;

/**
 * Created by Jo
 */
public class Cart_Model {

    private String key, product_id, name, description, original_price,discount_price, discount_percentage, qty,total_price, product_total_price,
            allowed_qty, allow_back_order, capacity, weight, image;

    public Cart_Model() {
    }

    public Cart_Model(String key, String product_id, String name, String description, String original_price, String discount_price,
                      String discount_percentage, String qty, String total_price, String product_total_price, String allowed_qty,
                      String allow_back_order, String capacity, String weight, String image) {

        this.key = key;
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.original_price = original_price;
        this.discount_price = discount_price;
        this.discount_percentage = discount_percentage;
        this.qty = qty;
        this.total_price = total_price;
        this.product_total_price = product_total_price;
        this.allowed_qty = allowed_qty;
        this.allow_back_order = allow_back_order;
        this.capacity = capacity;
        this.weight = weight;
        this.image = image;
    }

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
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

    public String getDDiscount_price() {
        return discount_price;
    }

    public void setDDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getDDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(String product_total_price) {
        this.product_total_price = product_total_price;
    }

    public String getallowed_qty() {
        return allowed_qty;
    }

    public void setallowed_qty(String allowed_qty) {
        this.allowed_qty = allowed_qty;
    }

    public String getallow_back_order() {
        return allow_back_order;
    }

    public void setallow_back_order(String allow_back_order) {
        this.allow_back_order = allow_back_order;
    }

    public String getcapacity() {
        return capacity;
    }

    public void setcapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

}

