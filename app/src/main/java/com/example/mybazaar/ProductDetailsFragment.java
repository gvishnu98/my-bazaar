package com.example.mybazaar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductDetailsFragment extends Fragment {


    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    private TextView detailsText;
    public static String productDetails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        detailsText=view.findViewById(R.id.tv_product_details);
        detailsText.setText(productDetails);
        return view;
    }
}