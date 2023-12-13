package com.poledisplayapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import poledisplayapp.R;

import com.poledisplayapp.Constant;
import com.poledisplayapp.MainActivity;
import com.poledisplayapp.models.CustomerOrderModel;

import java.util.List;

public class PosOrderAdapter extends RecyclerView.Adapter<PosOrderAdapter.MyViewHolder> {

    Context context;
    private int lastPosition = -1;
    boolean isNewSet = true;
    List<CustomerOrderModel> listElementsArrayList;

    public PosOrderAdapter(Context context, List<CustomerOrderModel> listElementsArrayList) {
        this.context = context;
        this.listElementsArrayList = listElementsArrayList;
        isNewSet = true;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pos_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CustomerOrderModel customerOrderModel = listElementsArrayList.get(position);
        if (!Constant.descList.contains(String.valueOf(listElementsArrayList.size() - 1))) {
            Constant.descList.add(String.valueOf(listElementsArrayList.size() - 1));
        }
        holder.iv_line.setBackgroundColor(context.getResources().getColor(R.color.color_sky));
        if (Constant.isLightcolor) {
            holder.iv_line.setBackgroundColor(context.getResources().getColor(R.color.color_green));
            Constant.isLightcolor = false;
        } else {
            Constant.isLightcolor = true;
        }
        holder.title.setText(customerOrderModel.getDescription().trim());
        holder.tv_qty.setVisibility(View.GONE);
        holder.view_line.setVisibility(View.GONE);
        if (customerOrderModel.getQty() != null) {
            if (customerOrderModel.getQty().length() > 0) {
                holder.tv_qty.setText(customerOrderModel.getQty());
                holder.tv_qty.setVisibility(View.VISIBLE);
                holder.view_line.setVisibility(View.VISIBLE);
            }
        }
        holder.tv_price.setText(customerOrderModel.getPrice());
        holder.tv_size.setVisibility(View.GONE);
//        holder.tv_size.setVisibility(View.INVISIBLE);
        if (MainActivity.tvSize.getVisibility() == View.VISIBLE) {
            if (customerOrderModel.getSize() != null) {
                if (customerOrderModel.getSize().length() > 0) {
                    holder.tv_size.setVisibility(View.VISIBLE);
                    holder.tv_size.setText(customerOrderModel.getSize().trim());
                }else{
//                    holder.tv_size.setVisibility(View.GONE);
                    holder.tv_size.setVisibility(View.INVISIBLE);
                }
            }
        }
        try {
            if (!isNewSet) {
                Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.fade_in : R.anim.fade_out);
                holder.itemView.startAnimation(animation);
            }
            lastPosition = position;

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(listElementsArrayList==null)
            return 0;
        return listElementsArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tv_qty, tv_price, tv_size;
        ImageView iv_line;
        View view_line;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            iv_line = view.findViewById(R.id.iv_line);
            view_line = view.findViewById(R.id.view_line);
            tv_qty = view.findViewById(R.id.tv_qty);
            tv_price = view.findViewById(R.id.tv_price);
            tv_size = view.findViewById(R.id.tvSize);

        }
    }

    public void setData(List<CustomerOrderModel> listElementsArrayList) {
        isNewSet = false;
        this.listElementsArrayList = listElementsArrayList;
        notifyDataSetChanged();

    }

}
