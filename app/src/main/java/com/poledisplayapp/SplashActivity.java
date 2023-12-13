package com.poledisplayapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import poledisplayapp.R;
import poledisplayapp.databinding.ActivitySplashBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.poledisplayapp.Api.ApiClientBillBoard;
import com.poledisplayapp.Api.ApiClientQT;
import com.poledisplayapp.Api.ApiInterface;
import com.poledisplayapp.Task.TaskGetAdRequest;
import com.poledisplayapp.Task.TaskGetStation;
import com.poledisplayapp.Task.TaskImagesAdvance;
import com.poledisplayapp.Task.TaskPlay;
import com.poledisplayapp.Task.TaskRetrieveAdunit;
import com.poledisplayapp.Task.TaskSubmitAdRequest;
import com.poledisplayapp.models.Auth_QTModel;
import com.poledisplayapp.models.GetAdRequestModel;
import com.poledisplayapp.models.ImagesDetailModel;
import com.poledisplayapp.models.RetrieveAdunitModel;
import com.poledisplayapp.models.Sign;
import com.poledisplayapp.models.StationModel;
import com.poledisplayapp.models.SubmitAdRequestModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SplashActivity extends Activity implements TaskGetStation.TaskGetStationEvent, TaskImagesAdvance.TaskImagesAdvEvent,
        TaskRetrieveAdunit.TaskRetrieveAdunitEvent , TaskGetAdRequest.TaskGetAdRequestEvent, TaskPlay.TaskPlayEvent {

    Context context;
    String storeNo;
    private static final int SPLASH_TIME = 2500, SPLASH_TIME_LOGIN = 1500;
    ImageView imgbanner;
    ActivitySplashBinding binding;
    Auth_QTModel auth_qt = new Auth_QTModel();
    int countTotal =0 ;
    int countForImage = 0;
    int numberofcall;
    int num;
    ProgressDialog loading = null;
    String StoreNoPref;
    static String StationNoPref;
    private Handler handler = new Handler();
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = this;
        imgbanner = (ImageView) findViewById(R.id.imgbanner);
        Constant.isgetAdreponesnull=false;
        Constant.AppPref = getSharedPreferences(Constant.PrefName, MODE_PRIVATE);
        binding.tvLogoffWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
                if (!storeRemember) {
                    Constant.AppPref.edit().putString("store", "").putString("station", "").putString("appname", "").putString("appNickname", "").putString("storeType", "").apply();
                    Constant.AppPref.edit().putBoolean("storeRemember", false).putBoolean("stationRemember", false).apply();
                }
                Constant.isTimeToRefreshImg = false;
                ApiClientBillBoard.signId = "";

                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            buildNo_Name = pInfo.versionName + pInfo.versionCode;
            //            buildNo_Name = pInfo.versionName;
            String buildNo_Name = "V1." + pInfo.versionCode;
            binding.tvbottomVersionno1.setText("" + buildNo_Name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //below if block is for auto login
        if (!Constant.AppPref.getString("store", "").isEmpty() && !Constant.AppPref.getString("station", "").isEmpty()) {
            storeNo = Constant.AppPref.getString("store", "");
            if (storeNo == null) {
                goToLoginScreen();
            } else if (storeNo.trim().length() <= 0) {
                goToLoginScreen();
            } else {
                try {
                    Constant.STOREID = storeNo;
                    Constant.selected_station_no = Constant.AppPref.getString("station", "");
                    Constant.STOREID = Constant.AppPref.getString("store", "");
                    Constant.appname = Constant.AppPref.getString("appname", "");
                    Constant.appNickname = Constant.AppPref.getString("appNickname", "");
                    Constant.storetype = Constant.AppPref.getString("storeType", "");
                    Constant.storetypes = Constant.AppPref.getString("storeType", "");
//                    Constant.isFromBlipBoard = Constant.getFlagForBlipBoard(SplashActivity.this, storeNo);
                    if (!Constant.isFromBlipBoard) {
                        if (Constant.appname != null && !Constant.appname.trim().isEmpty()) {
                            binding.tvbottomAppname.setText(Constant.appname.trim());
                        }
                    }
//                    else {
//                        if (Constant.appNickname != null && !Constant.appNickname.trim().isEmpty()) {
//                            Sign sign = Constant.getSign(SplashActivity.this);
//                            if (sign != null) {
//                                if (sign.getId() != null) {
//                                    if (sign.getId().trim().length() > 0) {
//                                        ApiClientBillBoard.signId = sign.getId();
//                                    }
//                                }
//                            }
//                            if (ApiClientBillBoard.signId != null) {
//                                if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                    try {
//                                        binding.tvbottomAppname.setText(Constant.STOREID + " - " + ApiClientBillBoard.signId + " - " + Constant.appNickname.trim());
//                                    } catch (Exception e) {
//                                    }
//                                }
//                            }
//                        }
//                    }
                    binding.tvbottomStationno1.setText("R" + Constant.AppPref.getString("station", ""));
                } catch (Exception e) {
                }
                initview();
            }

        } else {
            goToLoginScreen();
        }
    }

    private void initview() {
        if(!Constant.isNetworkAvailable(SplashActivity.this)){
            showErrorMessageDialog(SplashActivity.this, "", "No active internet connection found!");
        }else {
            if (storeNo.equalsIgnoreCase("707") && Constant.LighningURL.toLowerCase().contains("posservice")) {
                showErrorMessageDialog(SplashActivity.this, "", "The store#707 is incorrect for secure server!");
            } else {
                callStationListWs(storeNo);
            }
        }
    }

    private void callStationListWs(String storeNo) {
        TaskGetStation taskGetStation = new TaskGetStation(SplashActivity.this, this);
        taskGetStation.execute(storeNo);
    }

    @Override
    public void onGetStationDetailsResult(List<StationModel> stationList, String msg) {
        if (stationList != null && stationList.size() > 0) {
            Constant.selected_station_no = stationList.get(0).getStation_No();
            Constant.entered_storeno = storeNo;
            Constant.STOREID = storeNo;
            Constant.appname = stationList.get(0).getApp_name();
            Constant.appNickname = stationList.get(0).getStore_nickname();
            Constant.storetype = stationList.get(0).getPoleDisplay().trim();
            Constant.Industry_Type = stationList.get(0).getIndustryType().trim();
            Constant.address = stationList.get(0).getApp_name();
            Constant.Open_Time = stationList.get(0).getOpenTime().trim();
            Constant.Close_Time = stationList.get(0).getCloseTime().trim();

            Constant.AppPref.edit().putString("store", storeNo)
                    .putString("appname", Constant.appname)
                    .putString("appNickname", Constant.appNickname)
                    .putString("storeType", Constant.storetype).apply();

            if (stationList.get(0).getPoleDisplay() != null) {
                if (stationList.get(0).getPoleDisplay().trim().length() > 0) {
                    String poledisplay = stationList.get(0).getPoleDisplay().trim();
                    if (poledisplay.equalsIgnoreCase("Individual Store/Corporate Office")) {
                        Constant.isFromBlipBoard = false;
                        Constant.isFromQT=false;
//                        Constant.isFromQT=true;
                    } else if (poledisplay.equalsIgnoreCase("QT")){
                        Constant.isFromQT=true;
                        Constant.isFromBlipBoard = false;
                    }else if (poledisplay.equalsIgnoreCase("Computer Perfect")) {
                        Constant.isFromBlipBoard = false;
                        Constant.isFromQT=false;
                    } else {
                        Constant.isFromBlipBoard = false;
                        Constant.isFromQT=false;
                    }
                }
            }
//            Constant.saveFlagForBlipBoard(SplashActivity.this, Constant.isFromBlipBoard, storeNo);
            if (!Constant.isFromBlipBoard) {
                if (Constant.appname != null && !Constant.appname.trim().isEmpty()) {
                    binding.tvbottomAppname.setText(Constant.appname.trim());
                }
            }
//            else {
//                if (Constant.appNickname != null && !Constant.appNickname.trim().isEmpty()) {
//                    if (ApiClientBillBoard.signId != null) {
//                        if (ApiClientBillBoard.signId.trim().length() > 0) {
//                            try {
//                                binding.tvbottomAppname.setText(Constant.STOREID + " - " + ApiClientBillBoard.signId + " - " + Constant.appNickname.trim());
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                }
//            }
//            Edited by Varun for QT
            if (Constant.isFromQT){
//                Constant.LocalImageDetalList = Constant.getImages(SplashActivity.this, "QT", storeNo);
                if (Constant.LocalImageDetalList!=null ){
                    Constant.LocalImageDetalList.clear();
                }
                if (Constant.LocalImageDetalList==null || Constant.LocalImageDetalList.size()<=0){

                    loading = new ProgressDialog(context, R.style.MyprogressDTheme);
                    loading.setCancelable(false);
                    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loading.show();
                    Constant.loading = loading;
                    callQT_Token();
                }else{
                    goToMainScreen();
                }
            }
//            END
            else if (!Constant.isFromBlipBoard && !Constant.isFromQT) { // get images from corporate from here
                Constant.LocalImageDetalList = Constant.getImages(SplashActivity.this, "Home", storeNo);
//                Edited by Varun for internal corporate changes type like toy/wine etc
//                then webservice will call if the type is same then webservice will not be trigger
                if (Constant.storetype.equals(Constant.storetypes)) {
//                    END
                    Log.e("", "same : " );
                    if (Constant.LocalImageDetalList == null) {
                        //Log.d("TEST","FETCH LOCAL");
                        fetchImageInAdvance(storeNo);
                    } else if (Constant.LocalImageDetalList.size() <= 0) {
                        fetchImageInAdvance(storeNo);
                    } else {
                        try {
                            Constant.LocalImageHeaderList = Constant.getImages(SplashActivity.this, "Header", storeNo);
                            Constant.LocalImageFooterList = Constant.getImages(SplashActivity.this, "Footer", storeNo);
                        } catch (Exception e) {
                        }
                        goToMainScreen();
                    }
//                    Edited by Varun for internal corporate changes type like toy/wine etc
//                then webservice will call if the type is same then webservice will not be trigger
                } else{
                    Log.e("", "not same : " );
                    if (Constant.LocalImageDetalList != null && !Constant.LocalImageDetalList.isEmpty()) {
                        Constant.LocalImageDetalList.clear();
                    }
                    Constant.isfromsplash = true;
                    goToMainScreen();
                }
                // end
            }
//            else {
//                Sign sign = Constant.getSign(SplashActivity.this);
//                //get from shared preferance
//                if (sign != null) {
//                    if (sign.getId() != null) {
//                        if (sign.getId().trim().length() > 0) {
//                            ApiClientBillBoard.signId = sign.getId();
//                        }
//                    }
//                }
//                if (ApiClientBillBoard.signId == null) {
//                    goToMainScreen();
//                } else if (ApiClientBillBoard.signId.trim().length() <= 0) {
//                    goToMainScreen();
//
//                } else {
//                    Constant.ImageDetalList = Constant.getImages(SplashActivity.this, "Blip", storeNo);
//                    goToMainScreen();
//                }
//            }

        } else {
            if (Constant.LighningURL.toLowerCase().contains("posservice")) {
                showErrorMessageDialog(SplashActivity.this, "", "The store " + storeNo + " is incorrect for secure server!");
            } else {
                showErrorMessageDialog(SplashActivity.this, "", "The store " + storeNo + " is incorrect for test server!");
            }
        }

    }

    public void fetchImageInAdvance(String storeno) {

        Constant.deleteFiles();

        if (Constant.Computerperfect!=null && !Constant.Computerperfect.isEmpty()){
            Constant.Computerperfect.clear();
        }

        if (Constant.iscomefrom_adrequest_Same || Constant.isFromQT){
                Constant.storetype = "Computer Perfect-Wine & Spirit";
//            Constant.storetype = Constant.Industry_Type;
        }

        String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_IMAGES_DETAIL + storeno  + "/" +Constant.storetype;;
//        String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_DISPLAY_IMAGE_DATA + Constant.storetype;
        TaskImagesAdvance taskImagesAdvance = new TaskImagesAdvance(SplashActivity.this, this, storeno);
        taskImagesAdvance.execute(Urlban);

    }

    @Override
    public void onGetImagesDetailsResult(List<ImagesDetailModel> ImageDetalList) {
        if (!Constant.isFromQT) {
            goToMainScreen();
        }
    }

    public void goToMainScreen() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME);
    }

    public void goToLoginScreen() {
        new Handler().postDelayed(() -> {
            try {
                Constant.saveSign(SplashActivity.this, null);
                ApiClientBillBoard.signId = "";
            }catch (Exception e){

            }
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_LOGIN);
    }


    public void showErrorMessageDialog(Context context, final String desc1, String msg) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim_login);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.error_message_dialog);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = 400;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.tvTitle);
//        if(!desc1.isEmpty() && desc1.equalsIgnoreCase("stationMsg")){
//            txtTitle.setText("Station Message");
//        }else {
        txtTitle.setText("Store# Message");
//        }
        TextView tvDiscountDesc = (TextView) dialog.findViewById(R.id.tvDiscountDesc);
        tvDiscountDesc.setText(msg);
//        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
//        iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
//        GradientDrawable bgShape = (GradientDrawable) btnClose.getBackground();
//        bgShape.setColor(Color.parseColor(Constant.themeModel.ThemeColor));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!desc1.isEmpty() && desc1.equalsIgnoreCase("stationMsg")){
//                    dialog.dismiss();
//                }else{
                dialog.dismiss();
                try {
                    Constant.saveSign(SplashActivity.this, null);
                    ApiClientBillBoard.signId = "";
                }catch (Exception e){

                }
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
//                }
            }
        });
        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);
        dialog.show();
    }


    public void callQT_Token() {

        StationNoPref = Constant.AppPref.getString("station", "");
        StoreNoPref = Constant.AppPref.getString("store", "");

        Constant.deleteFile();

        auth_qt.setUsername(Constant.QT_USERNAME);
        auth_qt.setPassword(Constant.QT_PASSWORD);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(auth_qt);
        RequestBody body = RequestBody.create(jsonStr,
                MediaType.parse("application/json")
        );
        ApiInterface apiService = ApiClientQT.getClient().create(ApiInterface.class);
        Call<Auth_QTModel> call1 = apiService.AUTH_QT_CALL(body);
        call1.enqueue(new Callback<Auth_QTModel>() {
            @Override
            public void onResponse(Call<Auth_QTModel> call, Response<Auth_QTModel> response) {
                if (response.body() != null) {
                    auth_qt.setAccess_token(response.body().getAccess_token());
                    auth_qt.setId_token(response.body().getId_token());
                    auth_qt.setToken_type(response.body().getToken_type());
                    auth_qt.setExpires_in(response.body().getExpires_in());
                    auth_qt.setError(response.body().getError());
                    auth_qt.setError_description(response.body().getError_description());
//                    Toast.makeText(context, "Success"+auth_qt.getAccess_token(), Toast.LENGTH_SHORT).show();

//                    Constant.localAuthQTlist.add(auth_qt);
                    Constant.localAuthQTlist=auth_qt;

                    if (response.body().getAccess_token()!=null && !response.body().getAccess_token().equals("")){

                        if (isCallingAllowed()){
                            callgetAdunit(StoreNoPref+"_"+StationNoPref);
                        }else{
                            goToMainScreen();
                        }
//                        callgetAdRequest(StoreNoPref+"_"+StationNoPref);
//                        numberofcall ++;
                    }

                }
            }

            @Override
            public void onFailure(Call<Auth_QTModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();

            }
        });
    }
//Calling this to get the Ad unit name
    public void callgetAdunit(String adUnitname) {

        String url = null;
            //URL :- https://api.placeexchange.com/v3/orgs/00cb716a-c7f8-4f14-8244-cf510531e696/adunits/Varun_Test
            url = Constant.BASE_URL_QT + Constant.ORGS + Constant.QT_ID + Constant.WS_AD_UNIT+ "/" +adUnitname;
            TaskRetrieveAdunit taskretrieveadunit = new TaskRetrieveAdunit(this,getApplicationContext() , Constant .localAuthQTlist.getAccess_token());
            taskretrieveadunit.execute(url);
    }

    @Override
    public void onRetrieveAdunitResult(List<RetrieveAdunitModel> retrieveAdunitModel, boolean b) {

        if (b){
//            Toast.makeText(context, "AD Unit Not Found", Toast.LENGTH_SHORT).show();
            Constant.isgetAdreponesnull = true;
            Adunitnotfound_pop_up(context);
//            goToMainScreen();
        }else {
            if (Constant.localretrieveAdunitModelList.size() <= 0 && Constant.localretrieveAdunitModelList.isEmpty() && Constant.localretrieveAdunitModelList == null) {
                Constant.localretrieveAdunitModelList.addAll(retrieveAdunitModel);
            }

            for (int i = 0; i < retrieveAdunitModel.size(); i++) {
                if (retrieveAdunitModel.get(i).getName() != null && !retrieveAdunitModel.get(i).getName().equals("")) {
                    if (!retrieveAdunitModel.get(i).getAsset().getCapability().isVideo() && retrieveAdunitModel.get(i).getAsset().getCapability().isBanner()) {


                        if (retrieveAdunitModel.get(i).getStatus() == 3 && retrieveAdunitModel.get(i).getStatusDisplay().equals("Live")) {

                            if (Constant.adunit_height!=0 && Constant.adunit_width != 0){
                                Constant.adunit_width=0;
                                Constant.adunit_height =0;
                            }
//                            if (retrieveAdunitModel.get(i).getSlot()!=null && retrieveAdunitModel.get(i).getSlot().getHeight() != 0
//                                    && retrieveAdunitModel.get(i).getSlot().getWidth() != 0){
//                                Constant.adunit_height = retrieveAdunitModel.get(i).getSlot().getHeight();
//                                Constant.adunit_width = retrieveAdunitModel.get(i).getSlot().getWidth();
//                            }else{
//                                Constant.adunit_height = retrieveAdunitModel.get(i).getAdFormats().get(0).getH();
//                                Constant.adunit_width = retrieveAdunitModel.get(i).getAdFormats().get(0).getW();
//                            }
                            if (retrieveAdunitModel.get(i).getAdFormats() != null && !retrieveAdunitModel.get(i).getAdFormats().isEmpty()) {
                                // Check if the adFormats list is not empty
                                Constant.adunit_height = retrieveAdunitModel.get(i).getAdFormats().get(0).getH();
                                Constant.adunit_width = retrieveAdunitModel.get(i).getAdFormats().get(0).getW();
                            } else {
                                // Handle the case where the adFormats list is empty or null
                                Constant.adunit_height = retrieveAdunitModel.get(i).getSlot().getHeight();
                                Constant.adunit_width = retrieveAdunitModel.get(i).getSlot().getWidth();
                            }
                            Log.e("", "Height: "+Constant.adunit_height+ "Width: "+Constant.adunit_width );

                            callgetAdRequest(StoreNoPref + "_" + StationNoPref);
                            numberofcall++;
                            fetchImageInAdvance(storeNo);
                        } else{
                            if (Constant.loading.isShowing()){
                                Constant.loading.dismiss();
                            }

                            Adunitnotfound_pop_up(context);
                            Toast.makeText(context, "This Store Ad Unit is not Live", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    private void Adunitnotfound_pop_up(Context context) {

        View view;
        dialog = new Dialog(context, R.style.DialogSlideAnim_login);
        dialog.setCanceledOnTouchOutside(false);
        view = LayoutInflater.from(context).inflate(R.layout.adunit_error_pop_up, null);

        // set the custom dialog components - text, image and button
        TextView txtErrorName = (TextView) view.findViewById(R.id.txtErrorName);

        txtErrorName.setText("This station requires action, as this Enhanced Pole Display unit is not defined on the QT system, etc.");

        txtErrorName.setGravity(Gravity.CENTER);

        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);

                if (!storeRemember) {
                    Constant.AppPref.edit().putString("store", "")
                            .putString("station", "")
                            .putString("appname", "")
                            .putString("appNickname", "")
                            .putString("storeType", "")
                            .apply();

                    Constant.AppPref.edit().putBoolean("storeRemember", false)
                            .putBoolean("stationRemember", false)
                            .apply();
                }

                Constant.customerOrderList.clear();
                Constant.stationList.clear();
                Constant.isFromQT=false;
                finish();
            }
        });

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.setContentView(view);
        dialog.getWindow().setAttributes(params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();

    }

    public void callgetAdRequest(String name) {

        Constant.deleteFiles();

        if (!Constant.localgetAdRequestModel.isEmpty()&&Constant.localgetAdRequestModel!=null){
            Constant.localgetAdRequestModel.clear();
        }
        if (!Constant.LocalImageDetalList2.isEmpty()&&Constant.LocalImageDetalList2!=null){
            Constant.LocalImageDetalList2.clear();
        }
        if (!Constant.ImageDetalList3.isEmpty()&&Constant.ImageDetalList3!=null){
            Constant.ImageDetalList3.clear();
        }

        String url = null;
        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.WS_AD_UNIT + "/" +name +Constant.WS_SUBMIT_AD_REQUEST;
        TaskGetAdRequest taskGetAdRequest = new TaskGetAdRequest(context,getApplicationContext(),auth_qt.getAccess_token() , name);
        taskGetAdRequest.execute(url);
    }

    @Override
    public void onGetAdRequestResult(List<GetAdRequestModel> getAdRequestModel, String ad_unit_name) {
//       Check for total number of response to get
//        if the get Ad request is called 5 times then it will wait till we get all the 5 response after that we proceed furthur.
        if (getAdRequestModel!=null) {
            Constant.localgetAdRequestModel.addAll(getAdRequestModel);

//            END
                if (getAdRequestModel == null) {
                    Constant.isgetAdreponesnull = true;
                    goToMainScreen();
                } else {
//          END
                    Constant.isfromlogin = false;
                    goToMainScreen();

                }

            for (int i = 0; i < getAdRequestModel.size(); i++) {
                if (getAdRequestModel.get(i).getCreative() != null) {
                    double min_duration = getAdRequestModel.get(i).getCreative().getDuration();
                    callplays(getAdRequestModel.get(i).getContext(), ad_unit_name,min_duration);
                }
            }
        }else {
            if (getAdRequestModel == null) {

//                Used to call when there is only 1 ad return from the API and other 2 are null then it will only show 1 ad rather than no image
                    if (Constant.localgetAdRequestModel == null || Constant.localgetAdRequestModel.isEmpty()) {
                        Constant.isgetAdreponesnull = true;
                        goToMainScreen();
                    } else {
                        Constant.isfromlogin = false;
                        goToMainScreen();
//                        Toast.makeText(context, "123456", Toast.LENGTH_SHORT).show();
                    }

//                END
            }
        }

    }

    private void callplays(String scontext, String ad_unit_name, double min_duration) {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        String url = null;
        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.WS_AD_UNIT + "/" +ad_unit_name +Constant.WS_PLAYS;
        TaskPlay taskPlay = new TaskPlay(scontext,this,getApplicationContext(),auth_qt.getAccess_token(),ts,min_duration);
        taskPlay.execute(url);

    }

    @Override
    public void onGetplay(Object o, double min_duration) {

        // Delay in milliseconds (15 seconds in this case)
        long delayMillis = (long) (min_duration * 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this, "Calling AGAIN", Toast.LENGTH_SHORT).show();
                MainActivity.callgetAdunit(StoreNoPref+"_"+StationNoPref);
            }
        }, delayMillis);

    }


    @SuppressLint("MissingSuperCall" )
    @Override
    protected void onDestroy() {
        super.onStop();

        // Dismiss the dialog if it's showing
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (loading!=null&& loading.isShowing()){
            loading.dismiss();
        }
    }

    @SuppressLint("NewApi")
    public boolean isCallingAllowed() {

//        Get the Current Hour and Minutes
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

//        Convert Our Close time and Open time to 24 hours Format
        String time24Hour_OPEN_TIME = convert12HourTo24Hour(Constant.Open_Time);
        String time24Hour_CLOSE_TIME = convert12HourTo24Hour(Constant.Close_Time);

        LocalTime localTime = LocalTime.parse(time24Hour_OPEN_TIME);
        LocalTime localTime1 = LocalTime.parse(time24Hour_CLOSE_TIME);

        int Open_time_hour = localTime.getHour();
        int Open_time_minutes = localTime.getMinute();
        int Close_Time_hour = localTime1.getHour();
        int Close_Time_minutes = localTime1.getMinute();

        Log.e("", "isCallingAllowed:open  "+Open_time_hour );
        Log.e("", "isCallingAllowed:close "+Open_time_minutes);


        if ( (currentHour >= Open_time_hour && currentMinute >= 0)
                && (currentHour < Close_Time_hour && currentHour >= Open_time_hour)){
            return true;
        }else{
            return false;
        }
    }

    private static String convert12HourTo24Hour(String time12Hour) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("h a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:MM");

        try {
            Date date = inputFormat.parse(time12Hour);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the original string if conversion fails
        return time12Hour;
    }

//--------------------------------------- Un wanted Code ----------------------------
//    public void callSubmitAdRequest(String name) {
//
//        String url = null;
//        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.WS_AD_UNIT + "/" +name +Constant.WS_SUBMIT_AD_REQUEST;
//        TaskSubmitAdRequest taskSubmitAdRequest = new TaskSubmitAdRequest(this,getApplicationContext(),auth_qt.getAccess_token());
//        taskSubmitAdRequest.execute(url);
//    }
//
//    @Override
//    public void onSubmitAdRequstResult(List<SubmitAdRequestModel> submitAdRequest) {
//
//        for (int i=0 ; i<submitAdRequest.size(); i++) {
//            if (submitAdRequest.get(i).getCreative()!=null){
//                Constant.localSubmitAdRequestlist.addAll(submitAdRequest);
////                Toast.makeText(context, ""+submitAdRequest.get(i).getCreative().getName(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    public void saveImage(Context mContext, String urlDetail, File tempFile) {
//        if (mContext != null) {
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(urlDetail)
//                    .into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            write(resource, tempFile);
//                        }
//
//                        @Override
//                        public void onLoadCleared(Drawable placeholder) {
//                        }
//                    });
//        }
//    }
//
//    public void write(Bitmap bitmap, File tempFile) {
//        OutputStream outStream = null;
//        try {
//            outStream = new FileOutputStream(tempFile);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
//            outStream.close();
//
//            countForImage++;
//            Log.d("kaveriImage", "Image count: " + countForImage + " Total count: " + countTotal);
//
//            if (countForImage >= countTotal) {
//                // Perform required operations when all images are saved
//                Constant.ImageDetalList2 = new ArrayList<>();
//                Constant.ImageHeaderList = new ArrayList<>();
//                Constant.ImageFooterList = new ArrayList<>();
//                Constant.ImageDetalList2.addAll(Constant.LocalImageDetalList2);
//                Constant.ImageHeaderList.addAll(Constant.LocalImageHeaderList);
//                Constant.ImageFooterList.addAll(Constant.LocalImageFooterList);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}