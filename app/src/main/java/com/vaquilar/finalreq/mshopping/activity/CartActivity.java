package com.vaquilar.finalreq.mshopping.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vaquilar.finalreq.mshopping.R;
import com.vaquilar.finalreq.mshopping.adapter.CartAdapter;
import com.vaquilar.finalreq.mshopping.model.Cart;
import com.vaquilar.finalreq.mshopping.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ImageView emptyCart;
    LinearLayout checkoutLL;
    TextView totalPrice;
    private String mState = "SHOW_MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();
        emptyCart = findViewById(R.id.empty_cart_img);
        checkoutLL = findViewById(R.id.checkout_LL);
        totalPrice = findViewById(R.id.total_price);
        totalPrice.setText("Php. " + getTotalPrice() + "");
        setUpCartRecyclerview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        MenuItem item = menu.findItem(R.id.cart_delete);
        if (mState.equalsIgnoreCase("HIDE_MENU")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_delete:

                AlertDialog diaBox = showDeleteDialog();
                diaBox.show();

                return true;

            case android.R.id.home:

                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog showDeleteDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)


                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        localStorage.deleteCart();
                        adapter.notifyDataSetChanged();
                        emptyCart.setVisibility(View.VISIBLE);
                        mState = "HIDE_MENU";
                        invalidateOptionsMenu();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    private void changeActionBarTitle(ActionBar actionBar) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Cart");
        tv.setTextSize(20);


        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);
    }

    private void setUpCartRecyclerview() {
        cartList = new ArrayList<>();
        cartList = getCartList();
        if (cartList.isEmpty()) {
            mState = "HIDE_MENU";
            invalidateOptionsMenu();
            emptyCart.setVisibility(View.VISIBLE);
            checkoutLL.setVisibility(View.GONE);
        }
        recyclerView = findViewById(R.id.cart_rv);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CartAdapter(cartList, CartActivity.this);
        recyclerView.setAdapter(adapter);
    }

    public void onCheckoutClicked(View view) {

        startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
    }

    @Override
    public void updateTotalPrice() {

        totalPrice.setText("Php. " + getTotalPrice() + "");
        if (getTotalPrice() == 0.0) {
            mState = "HIDE_MENU";
            invalidateOptionsMenu();
            emptyCart.setVisibility(View.VISIBLE);
            checkoutLL.setVisibility(View.GONE);
        }
    }
}
