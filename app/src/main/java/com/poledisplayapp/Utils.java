package com.poledisplayapp;

import android.app.Dialog;
import android.content.res.ColorStateList;

import androidx.core.widget.CompoundButtonCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import poledisplayapp.R;

public class Utils {


//    public static void hideKeyboard(Context context) {

//            View v = MainActivity.getInstance().getCurrentFocus();
//            if (v != null) {
//                InputMethodManager imm = (InputMethodManager) MainActivity.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//    }

    public static  void setCheckBoxColor(CheckBox checkBox, int checkedColor, int uncheckedColor) {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {checkedColor, uncheckedColor};
        CompoundButtonCompat.setButtonTintList(checkBox, new
                ColorStateList(states, colors));
    }


    public static  void setRadioButtonColor(RadioButton radioButton, int checkedColor, int uncheckedColor) {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {checkedColor, uncheckedColor};
        CompoundButtonCompat.setButtonTintList(radioButton, new
                ColorStateList(states, colors));
    }



//    public static void showDialog(Context context, String message) {
//
//        Dialog myDialog = new Dialog(context, R.style.DialogSlideAnim_login);
//        myDialog.setCanceledOnTouchOutside(false);
//        View vDialog = LayoutInflater.from(context).inflate(R.layout.thankyou_for_purchase, null);
//
////        LinearLayout llRootCommonDialog = vDialog.findViewById(R.id.ll_root_dialog_common_for_app);
//        TextView tv_root_dialog_payment_process = vDialog.findViewById(R.id.tv_root_dialog_payment_process);
//
//        Button btnCommonDialogMessage = vDialog.findViewById(R.id.tv_message_dialog_common_for_app);
//        GradientDrawable bgShape = (GradientDrawable) btnCommonDialogMessage.getBackground();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            bgShape.setColor(context.getColor(R.color.colorAccent));
//        }
//
//        btnCommonDialogMessage.setText(message);
//        tv_root_dialog_payment_process.setVisibility(View.GONE);
//
//        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        myDialog.setContentView(vDialog);
//        myDialog.getWindow().setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams layoutParam = myDialog.getWindow().getAttributes();
//        myDialog.getWindow().setAttributes(layoutParam);
//
//        Thread timerThread = new Thread() {
//            public void run() {
//                try {
////                    sleep(3000);
//                    sleep(1500); //cut off half time
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (myDialog.isShowing())
//                        myDialog.dismiss();
////                    Constant.isDialogShow = false;
////                    Constant.isDialogShowSeclayout = false;
//                }
//            }
//        };
//        timerThread.start();
//
//        if (!myDialog.isShowing())
//            myDialog.show();
//    }
}
