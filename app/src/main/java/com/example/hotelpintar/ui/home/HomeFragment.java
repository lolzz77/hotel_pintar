package com.example.hotelpintar.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hotelpintar.BookingActivity;
import com.example.hotelpintar.R;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;

    private Button vbutBook, vbutBook2, vbutBook3, vbutBook4;
    private String mEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders
                .of(this)
                .get(HomeViewModel.class);
        View root = inflater
                .inflate(R.layout.fragment_home, container, false);
        final TextView textView = root
                .findViewById(R.id.text_home);

        //My code starts here
        vbutBook = root.findViewById(R.id.IDbuttonBook);
        vbutBook2 = root.findViewById(R.id.IDbuttonBook2);
        vbutBook3 = root.findViewById(R.id.IDbuttonBook3);
        vbutBook4 = root.findViewById(R.id.IDbuttonBook4);
        vbutBook.setOnClickListener(this);
        vbutBook2.setOnClickListener(this);
        vbutBook3.setOnClickListener(this);
        vbutBook4.setOnClickListener(this);
        //Til here

        homeViewModel
                .getText()
                .observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //Code below is default one,
                //it will display 'THIS IS HOME FRAGMENT' like that
                //textView.setText(s);
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v==vbutBook || v==vbutBook2 || v==vbutBook3 || v==vbutBook4 ) {
            Intent j = new Intent(getActivity(), BookingActivity.class);
            startActivity(j);
        }
    }

}