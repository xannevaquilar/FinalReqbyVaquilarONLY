package com.vaquilar.finalreq.mshopping.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.vaquilar.finalreq.mshopping.R;
import com.vaquilar.finalreq.mshopping.model.User;
import com.vaquilar.finalreq.mshopping.util.localstorage.LocalStorage;

public class ProfileFragment extends Fragment {

    TextView name, email, mobile, address;

    LocalStorage localStorage;
    Gson gson = new Gson();

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        address = view.findViewById(R.id.address);
        localStorage = new LocalStorage(getContext());

        User user = gson.fromJson(localStorage.getUserLogin(), User.class);
        name.setText(user.getFname());
        email.setText(user.getEmail());
        mobile.setText(user.getMobile());
        address.setText(user.getAddress());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }
}
