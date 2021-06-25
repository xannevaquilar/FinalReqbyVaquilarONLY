package com.vaquilar.finalreq.mshopping.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vaquilar.finalreq.mshopping.R;
import com.vaquilar.finalreq.mshopping.adapter.OrderAdapter;
import com.vaquilar.finalreq.mshopping.api.clients.RestClient;
import com.vaquilar.finalreq.mshopping.model.Order;
import com.vaquilar.finalreq.mshopping.model.OrdersResult;
import com.vaquilar.finalreq.mshopping.model.User;
import com.vaquilar.finalreq.mshopping.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderFragment extends Fragment {
    LocalStorage localStorage;
    LinearLayout linearLayout;
    private List<Order> orderList = new ArrayList<>();
    Gson gson = new Gson();
    private RecyclerView recyclerView;
    private OrderAdapter mAdapter;
    Order order;
    private List<Order> newOrderList = new ArrayList<>();
    public MyOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        recyclerView = view.findViewById(R.id.order_rv);
        linearLayout = view.findViewById(R.id.no_order_ll);
        localStorage = new LocalStorage(getContext());

        User user = gson.fromJson(localStorage.getUserLogin(), User.class);
        order = new Order(user.getId(), user.getToken());
        fetchOrderDetails(order);

        return view;
    }

    private void fetchOrderDetails(Order order) {

        Call<OrdersResult> call = RestClient.getRestService(getContext()).orderDetails(order);
        call.enqueue(new Callback<OrdersResult>() {
            @Override
            public void onResponse(Call<OrdersResult> call, Response<OrdersResult> response) {
                Log.d("Response :=>", response.body() + "");
                if (response != null) {

                    OrdersResult ordersResult = response.body();
                    if (ordersResult.getCode() == 200) {

                        orderList = ordersResult.getOrderList();
                        setupOrderRecycleView();

                    }

                }
            }

            @Override
            public void onFailure(Call<OrdersResult> call, Throwable t) {

            }
        });

    }

    private void setupOrderRecycleView() {
        if (orderList.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        mAdapter = new OrderAdapter(orderList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("MyOrder");
    }
}
