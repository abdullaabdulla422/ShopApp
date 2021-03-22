package com.banyan.naajilshop.model;

/**
 * Created by Jo
 */
public class My_Orders_Model {

    private String customername, order_no, order_id, order_subtotal, order_discount,order_coupon_code, order_tax_amt, order_shipping_charges,
            order_status, order_grand_total, order_createdat;

    public My_Orders_Model() {
    }

    public My_Orders_Model(String customername, String order_no, String order_id, String order_subtotal, String order_discount,
                           String order_coupon_code,
                           String order_tax_amt, String order_shipping_charges, String order_status, String order_grand_total,
                           String order_createdat) {

        this.customername = customername;
        this.order_no = order_no;
        this.order_id = order_id;
        this.order_subtotal = order_subtotal;
        this.order_discount = order_discount;
        this.order_coupon_code = order_coupon_code;
        this.order_tax_amt = order_tax_amt;
        this.order_shipping_charges = order_shipping_charges;
        this.order_status = order_status;
        this.order_grand_total = order_grand_total;
        this.order_createdat = order_createdat;
    }

    // Getter Methods

    public String getCustomername() {
        return customername;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getOrder_subtotal() {
        return order_subtotal;
    }

    public String getOrder_discount() {
        return order_discount;
    }

    public String getOrder_coupon_code() {
        return order_coupon_code;
    }

    public String getOrder_tax_amt() {
        return order_tax_amt;
    }

    public String getOrder_shipping_charges() {
        return order_shipping_charges;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getOrder_grand_total() {
        return order_grand_total;
    }

    public String getOrder_createdat() {
        return order_createdat;
    }

    // Setter Methods

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setOrder_subtotal(String order_subtotal) {
        this.order_subtotal = order_subtotal;
    }

    public void setOrder_discount(String order_discount) {
        this.order_discount = order_discount;
    }

    public void setOrder_coupon_code(String order_coupon_code) {
        this.order_coupon_code = order_coupon_code;
    }

    public void setOrder_tax_amt(String order_tax_amt) {
        this.order_tax_amt = order_tax_amt;
    }

    public void setOrder_shipping_charges(String order_shipping_charges) {
        this.order_shipping_charges = order_shipping_charges;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void setOrder_grand_total(String order_grand_total) {
        this.order_grand_total = order_grand_total;
    }

    public void setOrder_createdat(String order_createdat) {
        this.order_createdat = order_createdat;
    }

}

