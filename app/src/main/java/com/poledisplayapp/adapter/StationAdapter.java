package com.poledisplayapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import poledisplayapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poledisplayapp.Constant;
import com.poledisplayapp.LoginActivity;
import com.poledisplayapp.Utils;
import com.poledisplayapp.models.StationModel;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {

    List<StationModel> stationsList;
    Context context;
    String storeno;
    private int lastSelectedPosition = -1;
//    private int noselectionpos =  -1;

    public StationAdapter(Context context, List<StationModel> stationsList, String storeno) {
        this.context = context;
        this.stationsList = stationsList;
        this.storeno = storeno;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_station_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tv_stationVal.setText(stationsList.get(position).getStation_No().trim());
        holder.tv_storeval.setText(storeno);
        holder.rbSelection.setChecked(lastSelectedPosition == position);

        if(!Constant.isRadioButtonSelected){
            holder.rbSelection.setChecked(false);
        }

//        if(lstOrderTemsList.get(position).getOrderItemStatus().equalsIgnoreCase("open")){
//            holder.tv_statusvalue.setTextColor(context.getResources().getColor(R.color.green));
//
//        }else if(lstOrderTemsList.get(position).getOrderItemStatus().equalsIgnoreCase("cancelled")){
//            holder.tv_statusvalue.setTextColor(context.getResources().getColor(R.color.red));
//
//        }else {
//            holder.tv_statusvalue.setTextColor(context.getResources().getColor(R.color.blue_search));
//        }
//
//        holder.tv_statusvalue.setText(lstOrderTemsList.get(position).getOrderItemStatus());
//
//
//        if(!lstOrderTemsList.get(position).getOrderItemStatus().isEmpty()){
//
//            if(lstOrderTemsList.get(position).getOrderItemStatus().equalsIgnoreCase("Completed")){
//                holder.tv_startReturn.setVisibility(View.VISIBLE);
//            }else{
//                holder.tv_startReturn.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        if(lstOrderTemsList.get(position).getReturnDate() != null && !lstOrderTemsList.get(position).getOrderItemStatus().equalsIgnoreCase("open")){
//
//            String sourceString = "<b>" + "Return Eligibility:" + "</b> " + lstOrderTemsList.get(position).getReturnDate();
//            holder.tv_ReturnEligibilityValue.setText(Html.fromHtml(sourceString));
//
//        }else{
//
//            String sourceString = "<b>" + "Return Eligibility:" + "</b> " + "N/A" ;
//            holder.tv_ReturnEligibilityValue.setText(Html.fromHtml(sourceString));
//
//        }
//
//        holder.tv_quntityValue.setText(lstOrderTemsList.get(position).getQty());
//
//        if (!lstOrderTemsList.get(position).getInvLargeImage().isEmpty()) {
//            {
//                if (lstOrderTemsList.get(position).getInvLargeImage().contains("noimage")) {
//
//                    Glide.with(context).load(imgNoImageUrl + lstOrderTemsList.get(position)
//                            .getInvSmallImage()).placeholder(R.drawable.noimage)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true).into(holder.iv_orderImg);
//
//                } else {
//                    Glide.with(context).load(imgUrl + lstOrderTemsList.get(position).getInvSmallImage())
//                            .placeholder(R.drawable.progress_bar)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true).into(holder.iv_orderImg);
//                }
//
//            }
//        }
//
//
//        holder.tv_buyItagain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String orderid = lstOrderTemsList.get(position).getOrderID();
//                String itemid = lstOrderTemsList.get(position).getItemID();
//
//
//                if (Constant.SCREEN_LAYOUT == 1) {
//                    MainActivity.getInstance(). showReOrderConfirmation(orderid,itemid, 1);
//
//                } else if (Constant.SCREEN_LAYOUT == 2) {
//                    MainActivityDup.getInstance().showReOrderConfirmation(orderid,itemid, 1);
//                }
//
//            }
//        });
//
//        holder.tv_startReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stationsList.get(position).getStation_No()!= null && !stationsList.get(position).getStation_No().isEmpty()) {
//                    OrderHistoryFragment.getInstance().callOrderSummaryResultWebService(lstOrderTemsList.get(position).getOrderID());

                    lastSelectedPosition = position;
                    notifyDataSetChanged();

                    String stationNo = stationsList.get(position).getStation_No().trim();

                    Constant.selected_station_no = stationNo;
                    Constant.entered_storeno = storeno;

                    Constant.AppPref.edit().putString("station", stationNo).apply();

                    Constant.isRadioButtonSelected = true;

                    LoginActivity.btn_next_from_stationList.setEnabled(true);
                    GradientDrawable bgShape1 = (GradientDrawable) LoginActivity.btn_next_from_stationList.getBackground();
                    bgShape1.setColor(context.getResources().getColor(R.color.lightning_background_dark_color));
                    LoginActivity.btn_next_from_stationList.setTextColor(context.getResources().getColor(R.color.white));

//                    LoginActivity.btn_next_from_stationList.setBackgroundColor(context.getResources().getColor(R.color.lightning_background_dark_color));


//                    if(Constant.storeno_remember && Constant.station_no_remember){
//                        Constant.AppPref.edit().putBoolean("stationRemember", true).apply();
//                    }

//                   //Log.d("reminder_me_station:","reminder_me_station:" +Constant.station_no_remember);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_stationVal, tv_storeval;
        CardView cardView;
        Button btn_next;
        public RadioButton rbSelection;
//        tv_ReturnEligibilityValue,
//                tv_quntityValue, tv_statusvalue,tv_startReturn,tv_buyItagain;
//        ImageView iv_orderImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

//            iv_orderImg = itemView.findViewById(R.id.iv_orderImg);

            tv_stationVal = itemView.findViewById(R.id.tv_stationVal);
            tv_storeval = itemView.findViewById(R.id.tv_storeval);
            btn_next = itemView.findViewById(R.id.btn_next);
            cardView = itemView.findViewById(R.id.cardview);
            cardView.setBackgroundResource(R.drawable.gradient_drawable);
            rbSelection = (RadioButton)itemView.findViewById(R.id.rbSelection);
//            Utils.setRadioButtonColor(rbSelection, Color.parseColor("#0e6fb6"), context.getResources().getColor(R.color.lightning_background));
            Utils.setRadioButtonColor(rbSelection, Color.parseColor("#FFFFFF"), context.getResources().getColor(R.color.lightning_background_dark_color));
//            Utils.setRadioButtonColor(rbSelection, Color.parseColor("#0e6fb6"), context.getResources().getColor(R.color.white));

            rbSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    String stationNo = stationsList.get(lastSelectedPosition).getStation_No().trim();

                    Constant.selected_station_no = stationNo;
                    Constant.entered_storeno = storeno;

                    Constant.AppPref.edit().putString("station", stationNo).apply();

                    Constant.isRadioButtonSelected = true;

                    LoginActivity.btn_next_from_stationList.setEnabled(true);
//                    LoginActivity.btn_next_from_stationList.setBackgroundColor(context.getResources().getColor(R.color.lightning_background_dark_color));

                    GradientDrawable bgShape2 = (GradientDrawable) LoginActivity.btn_next_from_stationList.getBackground();
                    bgShape2.setColor(context.getResources().getColor(R.color.lightning_background_dark_color));
                    LoginActivity.btn_next_from_stationList.setTextColor(context.getResources().getColor(R.color.white));


                }
            });


//            tv_DateVal = itemView.findViewById(R.id.tv_Datevalue);
//            tv_ReturnEligibilityValue = itemView.findViewById(R.id.tv_ReturnEligibilityValue);
//            tv_quntityValue = itemView.findViewById(R.id.tv_quntityValue);
//            tv_startReturn = itemView.findViewById(R.id.tv_startReturn);
//            tv_buyItagain = itemView.findViewById(R.id.tv_buyItagain);
//            tv_quntityValue = itemView.findViewById(R.id.tv_quntityValue);
//
//            tv_startReturn.setTextColor(Color.parseColor(Constant.themeModel.ThemeColor));
//            tv_startReturn.setPaintFlags(tv_startReturn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
//
//            tv_buyItagain.setTextColor(Color.parseColor(Constant.themeModel.ThemeColor));
//            tv_buyItagain.setPaintFlags(tv_buyItagain.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        }
    }

}
