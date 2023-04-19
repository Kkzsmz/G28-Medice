package com.example.medi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.checkerframework.checker.nullness.qual.NonNull;



public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<MedicineModel,RecyclerViewAdapter.ViewHolder> {

    public RecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<MedicineModel> options) {
        super(options);
    }

    @NonNull

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.medicine_item, viewGroup, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i,MedicineModel medicineModel) {
        viewHolder.txtTime.setText(medicineModel.getTxtTime());
        viewHolder.txtType.setText(medicineModel.getTxtType());
        viewHolder.edtHeight.setText(medicineModel.getEdtHeight());


        // Reference to an image file online
        Glide.with(viewHolder.img.getContext())
                .load(medicineModel.getSurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView txtTime,txtType,edtHeight;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.time1);
            txtType = (TextView) itemView.findViewById(R.id.type1);
            edtHeight=(TextView) itemView.findViewById(R.id.diagnos1);
            img = (ImageView) itemView.findViewById(R.id.img1);


        }

    }

}
