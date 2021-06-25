package com.vaquilar.finalreq.mshopping.api;


import com.vaquilar.finalreq.mshopping.model.Category;
import com.vaquilar.finalreq.mshopping.model.CategoryResult;
import com.vaquilar.finalreq.mshopping.model.Order;
import com.vaquilar.finalreq.mshopping.model.OrdersResult;
import com.vaquilar.finalreq.mshopping.model.PlaceOrder;
import com.vaquilar.finalreq.mshopping.model.ProductResult;
import com.vaquilar.finalreq.mshopping.model.Token;
import com.vaquilar.finalreq.mshopping.model.User;
import com.vaquilar.finalreq.mshopping.model.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestService {

    @POST("api/v1/register")
    Call<UserResult> register(@Body User user);

    @POST("api/v1/login")
    Call<UserResult> login(@Body User user);

    @POST("api/v1/allcategory")
    Call<CategoryResult> allCategory(@Body Token token);

    @POST("api/v1/newProduct")
    Call<ProductResult> newProducts(@Body Token token);

    @POST("api/v1/homepage")
    Call<ProductResult> popularProducts(@Body Token token);

    @POST("api/v1/getlist")
    Call<ProductResult> getCategoryProduct(@Body Category category);

    @POST("api/v1/placeorder")
    Call<OrdersResult> confirmPlaceOrder(@Body PlaceOrder placeOrder);

    @POST("api/v1/orderDetails")
    Call<OrdersResult> orderDetails(@Body Order order);

    @POST("api/v1/updateUser")
    Call<UserResult> updateUser(@Body User user);

    @GET("api/v1/product/search")
    Call<ProductResult> searchProduct(@Query("s") String search);
}
