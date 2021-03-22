package com.banyan.naajilshop.global;

public class AppConfig {
    public static String base1_url = "http://epictech.in/Nanjilshop/Apicontroller/"; // test

    public static String URL_CITY = base1_url + "District";
    public static String URL_CATEGORY = base1_url + "category";
    public static String URL_PRODUCT = base1_url + "product";
    public static String URL_USER_REGISTRATION = base1_url + "user_registration";
    public static String URL_UPDATE_PROFILE = base1_url + "update_profile";
    public static String URL_USER_LOGIN = base1_url + "user_login";

    public static String URL_ADD_TO_CART = base1_url + "addtocart";
    public static String URL_CART_LIST = base1_url + "cartlist";

    public static final String URL_UPDATE_CART_LIST = base1_url+"updatecart";
    public static final String URL_REMOVE_PRODUCT_CART = base1_url+"remove";

    public static final String URL_MY_ORDERS = base1_url+"orderlist";

    public static final String URL_CHECK_PIN = base1_url+"shippingcode_check";

    public static final String URL_PLACE_ORDER = base1_url+"addorder";

    public static final int TAG_LOAD_MORE_COUNT = 100;
    public static final int TAG_VOLLERY_TIMEOUT = 60000;

    public static final int TAG_SUCCESS = 1;
}
