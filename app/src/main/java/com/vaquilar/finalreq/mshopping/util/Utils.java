package com.vaquilar.finalreq.mshopping.util;

import com.vaquilar.finalreq.mshopping.api.clients.RestClient;

public class Utils {

    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    public static final String Login_Fragment = "LoginFragment";
    public static final String SignUp_Fragment = "SignUpFragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
    public static final String CategoryImage = RestClient.BASE_URL + "assets/images/ProductImage/category/";
    public static final String ProductImage = RestClient.BASE_URL + "assets/images/ProductImage/product/";


}
