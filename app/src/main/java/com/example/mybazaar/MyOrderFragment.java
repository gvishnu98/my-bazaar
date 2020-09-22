package com.example.mybazaar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MyOrderFragment extends Fragment {

    public MyOrderFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrderRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_order, container, false);
        myOrderRecyclerView=view.findViewById(R.id.my_orders_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList=new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.dotshirt,"Dot Shirt","Delivered on Mon,15th Dec 2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.casual_shirt,"Casual Shirt","Delivered on Mon,15th Dec 2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.dotshirt,"Dot Shirt","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.casual_shirt,"Dot Shirt","Delivered on Mon,15th Dec 2020"));

        MyOrderAdapter myOrderAdapter=new MyOrderAdapter(myOrderItemModelList);
        myOrderRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;
    }
}