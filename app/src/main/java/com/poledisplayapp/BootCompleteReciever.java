package com.poledisplayapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootCompleteReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ){
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PrefName,Context.MODE_PRIVATE);

//            sharedPreferences.edit().putString("store", "1620").putString("station", "2").apply();

            String Store_Num= sharedPreferences.getString("store","");
            String Station_Num = sharedPreferences.getString("station","");

            Log.e("", "onReceive:123 "+Store_Num );

            if (Store_Num!=null && !Store_Num.equals("")){

            }
        }
    }
}
