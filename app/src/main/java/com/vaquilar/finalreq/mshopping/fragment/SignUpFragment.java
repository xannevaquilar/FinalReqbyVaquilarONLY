package com.vaquilar.finalreq.mshopping.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.vaquilar.finalreq.mshopping.R;
import com.vaquilar.finalreq.mshopping.activity.LoginRegisterActivity;
import com.vaquilar.finalreq.mshopping.activity.MainActivity;
import com.vaquilar.finalreq.mshopping.api.clients.RestClient;
import com.vaquilar.finalreq.mshopping.model.User;
import com.vaquilar.finalreq.mshopping.model.UserResult;
import com.vaquilar.finalreq.mshopping.util.CustomToast;
import com.vaquilar.finalreq.mshopping.util.Utils;
import com.vaquilar.finalreq.mshopping.util.localstorage.LocalStorage;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, mobileNumber,
            password;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    User user;
    LocalStorage localStorage;
    Gson gson = new Gson();
    View progress;

    public SignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        localStorage = new LocalStorage(getContext());
        initViews();
        setListeners();
        return view;
    }
    private void initViews() {
        fullName = view.findViewById(R.id.fullName);
        progress = view.findViewById(R.id.progress_bar);
        mobileNumber = view.findViewById(R.id.mobileNumber);

        password = view.findViewById(R.id.password);

        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        terms_conditions = view.findViewById(R.id.terms_conditions);

        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                checkValidation();
                break;

            case R.id.already_user:
                new LoginRegisterActivity().replaceLoginFragment();
                break;
        }

    }

    private void checkValidation() {
        String getFullName = fullName.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getPassword = password.getText().toString();
        Pattern p = Pattern.compile(Utils.regEx);


        if (getFullName.length() == 0) {
            fullName.setError("Eneter Your Name");
            fullName.requestFocus();
        } else if (getMobileNumber.length() == 0) {
            mobileNumber.setError("Eneter Your Mobile Number");
            mobileNumber.requestFocus();
        } else if (getPassword.length() == 0) {
            password.setError("Eneter Password");
            password.requestFocus();
        } else if (getPassword.length() < 6) {
            password.setError("Eneter 6 digit Password");
            password.requestFocus();
        } else if (!terms_conditions.isChecked()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Accept Term & Conditions");
        } else {
            user = new User(getFullName, "", getMobileNumber, getPassword);
            registerUser(user);
        }

    }

    private void registerUser(User userString) {
        showProgressDialog();
        Call<UserResult> call = RestClient.getRestService(getContext()).register(userString);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                Log.d("Response :=>", response.body() + "");
                if (response != null) {

                    UserResult userResult = response.body();
                    if (userResult.getCode() == 201) {
                        String userString = gson.toJson(userResult.getUser());
                        localStorage.createUserLoginSession(userString);
                        Toast.makeText(getContext(), userResult.getStatus(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        new CustomToast().Show_Toast(getActivity(), view,
                                userResult.getStatus());

                    }

                } else {
                    new CustomToast().Show_Toast(getActivity(), view,
                            "Please Enter Correct Data");
                }

                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Log.d("Error==> ", t.getMessage());
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


}
