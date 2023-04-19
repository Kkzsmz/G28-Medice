package com.example.medi;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 **/
public class Medice extends Fragment {


    private static final String TAG ="" ;
    RecyclerView mediceRecycler;
    RecyclerViewAdapter recyclerViewAdapter;


    FirebaseRecyclerOptions.Builder<MedicineModel> options =
            new FirebaseRecyclerOptions.Builder<MedicineModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("medicine"), MedicineModel.class);



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    public void onResume() {
        // medice list of  UI
        super.onResume();
        mediceRecycler = mediceRecycler.findViewById(R.id.medicineRecycle);
        mediceRecycler.setAdapter(recyclerViewAdapter);
        mediceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


    }



