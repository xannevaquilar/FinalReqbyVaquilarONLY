package com.vaquilar.finalreq.mshopping.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vaquilar.finalreq.mshopping.R;
import com.vaquilar.finalreq.mshopping.adapter.ProductAdapter;
import com.vaquilar.finalreq.mshopping.api.clients.RestClient;
import com.vaquilar.finalreq.mshopping.helper.Converter;
import com.vaquilar.finalreq.mshopping.helper.Data;
import com.vaquilar.finalreq.mshopping.model.Category;
import com.vaquilar.finalreq.mshopping.model.Product;
import com.vaquilar.finalreq.mshopping.model.ProductResult;
import com.vaquilar.finalreq.mshopping.model.Token;
import com.vaquilar.finalreq.mshopping.model.User;
import com.vaquilar.finalreq.mshopping.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends BaseActivity {
    private static int cart_count = 0;
    Data data;

    String Tag = "List";
    View progress;
    LocalStorage localStorage;
    Gson gson = new Gson();
    User user;
    Token token;
    String categoryName;
    Category category;
    List<Product> productList = new ArrayList<>();
    ProductAdapter mAdapter;
    private RecyclerView recyclerView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        progress = findViewById(R.id.progress_bar);

        localStorage = new LocalStorage(getApplicationContext());
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        token = new Token(user.getToken());
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category");
        category = new Category(categoryName, user.getToken());


        cart_count = cartCount();
        recyclerView = findViewById(R.id.product_rv);


        getCategoryProduct();


    }

    private void getCategoryProduct() {
        showProgressDialog();
        Call<ProductResult> call = RestClient.getRestService(getApplicationContext()).getCategoryProduct(category);
        call.enqueue(new Callback<ProductResult>() {
            @Override
            public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                Log.d("Response :=>", response.body() + "");
                if (response != null) {

                    ProductResult productResult = response.body();
                    if (productResult.getCode() == 200) {

                        productList = productResult.getProductList();
                        setUpRecyclerView();

                    }

                }

                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t) {
                Log.d("Error", t.getMessage());
                hideProgressDialog();

            }
        });

    }

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }

    private void changeActionBarTitle(ActionBar actionBar) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Products");
        tv.setTextSize(20);

        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);
    }

    private void setUpRecyclerView() {

        mAdapter = new ProductAdapter(productList, ProductActivity.this, Tag);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    private void setUpGridRecyclerView() {

        mAdapter = new ProductAdapter(productList, ProductActivity.this, Tag);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    public void onToggleClicked(View view) {
        if (Tag.equalsIgnoreCase("List")) {
            Tag = "Grid";
            setUpGridRecyclerView();
        } else {
            Tag = "List";
            setUpRecyclerView();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductActivity.this, cart_count, R.drawable.ic_shopping_basket));
        return true;
    }


    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();

    }

}
