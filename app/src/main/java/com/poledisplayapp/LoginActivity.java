package com.poledisplayapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.poledisplayapp.Api.ApiClientBillBoard;
import com.poledisplayapp.Api.ApiClientQT;
import com.poledisplayapp.Api.ApiInterface;
import com.poledisplayapp.Task.TaskGetStation;
import com.poledisplayapp.Task.TaskImagesAdvance;
import com.poledisplayapp.Task.TaskRetrieveAdunit;
import com.poledisplayapp.Task.TaskSendmsg;
import com.poledisplayapp.adapter.StationAdapter;
import com.poledisplayapp.models.Auth_QTModel;
import com.poledisplayapp.models.ImagesDetailModel;
import com.poledisplayapp.models.RetrieveAdunitModel;
import com.poledisplayapp.models.Sign;
import com.poledisplayapp.models.StationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import poledisplayapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements TaskGetStation.TaskGetStationEvent, TaskImagesAdvance.TaskImagesAdvEvent,
        TaskRetrieveAdunit.TaskRetrieveAdunitEvent, TaskSendmsg.TaskSendmsgEvent {
    String Storeno;
    String stationNo;
    String android_id = null;
    ProgressDialog loading = null;
    //    EditText etStoreno, etStationNo;
    public static TextInputEditText etStoreno;
    Button btn_save, btn_Cancel;
    public static Button btn_next_from_stationList;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    StationAdapter stationAdapter;
    LinearLayout llLoginlayout, llStationLayout;
    CheckBox chkRemember_me, chkRemember_me_station;
    //    chkbiliboard;
    TextView tv_refersh;
    String[] permissions_camera = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static LoginActivity context;
    ApiInterface apiServiceBillboard;
    ImageView imgbanner;
    String signName = "";

    String latitude = "0.0", longitude = "0.0";

    static Auth_QTModel auth_qt = new Auth_QTModel();
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
//        apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        imgbanner = (ImageView) findViewById(R.id.imgbanner);
        if (!checkPermissions(LoginActivity.this, permissions_camera)) {
            turnOnPermissions(500);
        }
    }

    private void initview() {
        etStoreno = (TextInputEditText) findViewById(R.id.txtStoreno);
        chkRemember_me = (CheckBox) findViewById(R.id.chkRemember_me);
        Utils.setCheckBoxColor(chkRemember_me, Color.parseColor("#0e6fb6"), getResources().getColor(R.color.lightning_background));
        Constant.isFromBlipBoard = false;
        Constant.isFromQT = false;
        llLoginlayout = (LinearLayout) findViewById(R.id.llLoginlayout);
//        chkbiliboard = (CheckBox) findViewById(R.id.chkbiliboard);
//        chkbiliboard.setVisibility(View.GONE);
//        Utils.setCheckBoxColor(chkbiliboard, Color.parseColor("#0e6fb6"), getResources().getColor(R.color.lightning_background));
//        chkbiliboard.setChecked(false);
//        chkbiliboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//               /* if (isChecked) {
//                    Constant.isFromBlipBoard = true;
//                } else {
//                    Constant.isFromBlipBoard = false;
//                }*/
//            }
//        });
        // end
        if (Constant.AppPref != null) {
            boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
            if (storeRemember && !Constant.AppPref.getString("store", "").isEmpty()) {
                chkRemember_me.setChecked(true);
                String storenoVal = Constant.AppPref.getString("store", "");
                etStoreno.setText(storenoVal);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Objects.requireNonNull(etStoreno.getText()).toString().isEmpty()) {
                        etStoreno.setSelection(etStoreno.getText().length());
                    }
                }
            }
        } else {
            etStoreno.setText("");
        }
        chkRemember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.storeno_remember = true;
                    Constant.AppPref.edit().putBoolean("storeRemember", true).apply();
                } else {
                    Constant.storeno_remember = false;
                    Constant.AppPref.edit().putBoolean("storeRemember", false).apply();
                }
            }
        });
        chkRemember_me_station = (CheckBox) findViewById(R.id.chkRemember_me_station);
        Utils.setCheckBoxColor(chkRemember_me_station, Color.parseColor("#0e6fb6"), getResources().getColor(R.color.lightning_background));
        chkRemember_me_station.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.station_no_remember = true;
//                    Constant.AppPref.edit().putBoolean("stationRemember", true).apply();
                } else {
                    Constant.station_no_remember = false;
//                    Constant.AppPref.edit().putBoolean("stationRemember", false).apply();
                }
            }
        });
        llStationLayout = (LinearLayout) findViewById(R.id.llStationLayout);
        tv_refersh = (TextView) findViewById(R.id.tv_refersh);
        tv_refersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.AppPref.edit().putString("store", "").putString("station", "").apply();
                Constant.AppPref.edit().putBoolean("storeRemember", false).putBoolean("stationRemember", false).apply();
                etStoreno.getText().clear();
                chkRemember_me.setChecked(false);
            }
        });
        btn_next_from_stationList = (Button) findViewById(R.id.btn_next);
        btn_next_from_stationList.setEnabled(false);
        GradientDrawable bgShape = (GradientDrawable) btn_next_from_stationList.getBackground();
        bgShape.setColor(getResources().getColor(R.color.firstRowcolor));
        btn_next_from_stationList.setTextColor(getResources().getColor(R.color.grey));
        btn_next_from_stationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.isRadioButtonSelected) {
                    boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
                    if (storeRemember && Constant.station_no_remember) {
                        Constant.AppPref.edit().putBoolean("stationRemember", true).apply();
                    }

                    if (!Constant.Computerperfect.isEmpty()&&Constant.Computerperfect!=null){
                        Constant.Computerperfect.clear();
                    }
                    if (!Constant.localgetAdRequestModel.isEmpty()&&Constant.localgetAdRequestModel!=null){
                        Constant.localgetAdRequestModel.clear();
                    }
                    Constant.isfromlogin=true;
                    Log.e("", "onClick: 23" );

                    if (Constant.isFromQT){
                        callQT_Token();
                    }else{
                        go_with_the_flow();
                    }

                }

            }
        });
        btn_save = (Button)
                findViewById(R.id.btn_save);
        btn_Cancel = (Button)
                findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etStoreno.setText("");
                etStoreno.clearFocus();
            }
        });
        recyclerView = (RecyclerView)
                findViewById(R.id.recyclerView1);
        layoutManager = new
                LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(mLayoutManager);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Storeno = etStoreno.getText().toString();
//                stationNo = etStationNo.getText().toString();
//
//                if(!Storeno.isEmpty() && !stationNo.isEmpty()){
//
//                    Constant.AppPref.edit().putString("store", Storeno)
//                            .putString("station", stationNo).apply();
//
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
////                    finish();
//
//                }else{
//                    if(Storeno.isEmpty()){
//                        etStoreno.setError("Please Enter Store No");
//                    }else if(stationNo.isEmpty()){
//                        etStationNo.setError("Please Enter Station No");
//                    }
//                }
                Storeno = etStoreno.getText().toString();
//                here
//                Constant.AppPref.edit().putBoolean("storeRemember", Constant.storeno_remember).apply();
                if(Constant.isNetworkAvailable(LoginActivity.this)) {
                    if (!Storeno.isEmpty()) {
                        boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
                        boolean stationRemember = Constant.AppPref.getBoolean("stationRemember", false);
                        if (!Constant.AppPref.getString("store", "").isEmpty() && storeRemember
                                && !Constant.AppPref.getString("station", "").isEmpty()
                                && stationRemember) {
                            //need to check for same store or diffrent store no
                            String previuosStoreno = Constant.AppPref.getString("store", "");
                            String currentStoreno = Storeno;
                            if (previuosStoreno.trim().equals(currentStoreno.trim())) {
                                Constant.AppPref.edit().putString("store", Storeno).apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Log.e("", "onClick: 24" );
                            } else {
                                Constant.AppPref.edit().putString("store", Storeno).apply();
                                Constant.AppPref.edit().putBoolean("stationRemember", false).apply();
//                            if()
                                //toooo
//                            Constant.AppPref.edit().putBoolean("storeRemember", false).putBoolean("stationRemember", false).apply();
//
                                if (storeRemember) {
                                    chkRemember_me_station.setVisibility(View.VISIBLE);
                                } else {
                                    chkRemember_me_station.setVisibility(View.GONE);
                                }
                                callStationListWs(Storeno);
                            }

                        } else {
                            if (storeRemember) {
                                chkRemember_me_station.setVisibility(View.VISIBLE);
                            } else {
                                chkRemember_me_station.setVisibility(View.GONE);
                            }
                            callStationListWs(Storeno);
                        }

                    } else {
                        if (Storeno.isEmpty()) {
                            etStoreno.setError("Please Enter Store No");
                        }
                    }
                    hideKeyboard();
                }else{
                    Toast.makeText(LoginActivity.this, "No working internet connection found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    This is used to get the Access token for QT Web Service.
    
    public void callQT_Token() {

        loading = new ProgressDialog(context, R.style.MyprogressDTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
        Constant.loading = loading;

//        Edited by Varun or delete old images
        Constant.deleteFile();
//        END


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
                if (response.isSuccessful() && response.body() != null) {
                    auth_qt.setAccess_token(response.body().getAccess_token());
                    auth_qt.setId_token(response.body().getId_token());
                    auth_qt.setToken_type(response.body().getToken_type());
                    auth_qt.setExpires_in(response.body().getExpires_in());
                    auth_qt.setError(response.body().getError());
                    auth_qt.setError_description(response.body().getError_description());

                    Constant.localAuthQTlist = auth_qt;

                    if (response.body().getAccess_token() != null && !response.body().getAccess_token().isEmpty()) {
                        String StationNoPref = Constant.AppPref.getString("station","").trim();
                        callgetAdunit(Storeno+"_"+StationNoPref);

                    }
                }
            }

            @Override
            public void onFailure(Call<Auth_QTModel> call, Throwable t) {
                call.cancel();
            }
        });
    }
    
//    Calling this to get the Ad unit name
    public static void callgetAdunit(String adUnitname) {

        String url = null;
        //URL :- https://api.placeexchange.com/v3/orgs/00cb716a-c7f8-4f14-8244-cf510531e696/adunits
        url = Constant.BASE_URL_QT + Constant.ORGS + Constant.QT_ID + Constant.WS_AD_UNIT + "/" +adUnitname;
        TaskRetrieveAdunit taskretrieveadunit = new TaskRetrieveAdunit(context,context , Constant.localAuthQTlist.getAccess_token());
        taskretrieveadunit.execute(url);
    }

    @Override
    public void onRetrieveAdunitResult(List<RetrieveAdunitModel> retrieveAdunitModel, boolean b) {

        String StationNoPref = Constant.AppPref.getString("station","").trim();

        if (b){
//            When the Ad unit is not found then display the pop -up
            llStationLayout.setVisibility(View.GONE);
            Adunitnotfound_pop_up(context);
            call_msg_WS(Storeno,StationNoPref,"Fail");
        }else {
            if (Constant.localretrieveAdunitModelList.isEmpty()) {
                Constant.localretrieveAdunitModelList.addAll(retrieveAdunitModel);
            }

            for (int i = 0; i < retrieveAdunitModel.size(); i++) {
                if (retrieveAdunitModel.get(i).getName() != null && !retrieveAdunitModel.get(i).getName().equals("")) {
                    if (!retrieveAdunitModel.get(i).getAsset().getCapability().isVideo() && retrieveAdunitModel.get(i).getAsset().getCapability().isBanner()) {

                        if (retrieveAdunitModel.get(i).getStatus() == 3 && retrieveAdunitModel.get(i).getStatusDisplay().equals("Live")) {

                            if (Constant.adunit_height!=0 ){
                                Constant.adunit_height =0;
                            }
                            if (Constant.adunit_width != 0){
                                Constant.adunit_width = 0;
                            }
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

                            go_with_the_flow();
                        }
                        else{
//                            When the Ad unit is not Live then display the pop -up
                            if (Constant.loading.isShowing()){
                                Constant.loading.dismiss();
                            }

                            llStationLayout.setVisibility(View.GONE);
                            Adunitnotfound_pop_up(context);
                            Toast.makeText(context, "This Store Ad Unit is not Live", Toast.LENGTH_SHORT).show();

                            call_msg_WS(Storeno,StationNoPref,"Fail");

                        }
                    }
                }
            }
        }

    }

//    Show when the Ad unit is not found or Ad unit is not Live

    public void Adunitnotfound_pop_up(LoginActivity context) {

        View view;
        dialog = new Dialog(context, R.style.DialogSlideAnim_login);
        dialog.setCanceledOnTouchOutside(false);
        view = LayoutInflater.from(context).inflate(R.layout.adunit_error_pop_up, null);

        // set the custom dialog components - text, image and button
        TextView txtErrorName = (TextView) view.findViewById(R.id.txtErrorName);
        txtErrorName.setText("This station requires action, as this Enhanced Pole Display unit is not defined on the QT system, etc.");
//        if (Constant.AdUnitStatus.isEmpty() && Constant.AdUnitStatus.equals("")){
//
//        }else {
//            txtErrorName.setText("This station requires action, as this Enhanced Pole Display unit is not defined on the QT system, etc.");
//            Constant.AdUnitStatus="";
//        }
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

                Constant.isFromQT=false;
                Constant.customerOrderList.clear();
                Constant.stationList.clear();

                if (Constant.Computerperfect!=null && !Constant.Computerperfect.isEmpty()){
                    Constant.Computerperfect.clear();
                }
                Constant.isFromQT=false;
                Constant.AppPref.edit().clear().commit();

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

//    Old flow to go in Main Activity

    private void go_with_the_flow() {

        String StationNoPref = Constant.AppPref.getString("station","").trim();
        call_msg_WS(Storeno,StationNoPref,"Success");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void call_msg_WS(String storeno, String stationNoPref, String msg) {

        TaskSendmsg taskSendmsg = new TaskSendmsg(LoginActivity.this, context);
        taskSendmsg.execute(storeno,stationNoPref,msg);

    }

    @Override
    public void onSendmsgResult(String s) {


    }

    private void callStationListWs(String storeno) {
        TaskGetStation taskGetStation = new TaskGetStation(LoginActivity.this, this);
        taskGetStation.execute(storeno);

    }

    @Override
    public void onGetStationDetailsResult(List<StationModel> stationList, String msg) {
//       //Log.d("reminder_me:","reminder_me:" +Constant.storeno_remember);
        if (stationList != null && stationList.size() > 0) {
            Constant.appname = stationList.get(0).getApp_name();
            Constant.appNickname = stationList.get(0).getStore_nickname();
            Constant.storetype = stationList.get(0).getPoleDisplay().trim();
            Constant.address = stationList.get(0).getApp_name();
            Constant.Industry_Type = stationList.get(0).getIndustryType().trim();
            Constant.Open_Time = stationList.get(0).getOpenTime().trim();
            Constant.Close_Time = stationList.get(0).getCloseTime().trim();
            Constant.STOREID = Storeno;
            if (stationList.get(0).getPoleDisplay() != null) {
                if (stationList.get(0).getPoleDisplay().trim().length() > 0) {
                    String poledisplay = stationList.get(0).getPoleDisplay().trim();
                    if (poledisplay.equalsIgnoreCase("Individual Store/Corporate Office")) {
                        Constant.isFromBlipBoard = false;
                        Constant.isFromQT = false;
                    } else if (poledisplay.equalsIgnoreCase("Computer Perfect")) {
                        Constant.isFromBlipBoard = false;
                        Constant.isFromQT = false;
                    }
//                    Edited by Varun for QT
                    else if (poledisplay.equalsIgnoreCase("QT")){
                        Constant.isFromQT = true;
                        Constant.isFromBlipBoard = false;
                    }
//                    END
                    else {
                        Constant.isFromBlipBoard = false;
                        Constant.isFromQT = false;
                    }
                }
            }
//            Constant.saveFlagForBlipBoard(LoginActivity.this, Constant.isFromBlipBoard, Storeno);
            Constant.AppPref.edit().putString("store", Storeno).putString("appname", Constant.appname)
                    .putString("appNickname", Constant.appNickname)
                    .putString("storeType", Constant.storetype).apply();
            if (Constant.storeno_remember) {
                Constant.AppPref.edit().putBoolean("storeRemember", true).apply();

            }
            stationAdapter = new StationAdapter(LoginActivity.this, stationList, Storeno);
            recyclerView.setAdapter(stationAdapter);
//            hideKeyboard();
            etStoreno.setText("");
            llLoginlayout.setVisibility(View.GONE);
            llStationLayout.setVisibility(View.VISIBLE);

                android_id = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if (android_id == null) {
                    android_id = Constant.getUUId(LoginActivity.this);
                } else if (android_id.trim().length() <= 0) {
                    android_id = Constant.getUUId(LoginActivity.this);
                }
                Log.d("TEST", "deviceid" + android_id);
                signName = "PD_" + android_id + "_" + Storeno + Constant.appNickname.replace(" ", "_");
                Log.d("TEST", "signname" + signName);
                //from blipboard

//                Sign sign = Constant.getSign(LoginActivity.this);
                //get from shared preferance
//                if (sign != null) {
//                    if (sign.getId() != null) {
//                        if (sign.getId().trim().length() > 0) {
//                            ApiClientBillBoard.signId = sign.getId();
//                        }
//                    }
//                }
//                if (ApiClientBillBoard.signId != null) {
//                    if (ApiClientBillBoard.signId.trim().length() > 0) {
//                        if (Constant.getTimeToRefresh(LoginActivity.this, false)) {
//                            if (loading != null) {
//                                loading.dismiss();
//                            }
//                        } else {
//                            try {
//                                if (loading != null) {
//                                    loading.dismiss();
//                                }
//                                Constant.ImageDetalList = Constant.getImages(LoginActivity.this, "Blip", Storeno);
//                            } catch (Exception E) {
//                            }
//                        }
//                    } else {
//                        //fetch or create new
//                        fetchAllSigns();
//                    }
//                } else {
//                    //fetch or create new
//                    fetchAllSigns();
//                }

        } else {
//            alert popup
            if (llLoginlayout.getVisibility() == View.GONE) {
                etStoreno.setText("");
                llLoginlayout.setVisibility(View.VISIBLE);
                llStationLayout.setVisibility(View.GONE);
            }
            showErrorMessageDialog(LoginActivity.this, "", msg);
        }
    }

    private void hideKeyboard() {
        View v = LoginActivity.this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Below code is for auto login*********
//        Constant.AppPref = getSharedPreferences(Constant.PrefName, MODE_PRIVATE);
//        if (!Constant.AppPref.getString("store", "").isEmpty() && !Constant.AppPref.getString("station", "").isEmpty()) {
//
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//
//        }else{
//
//            initview();
//
//        }
        //end
        //code for displayed every time login screen
        Constant.AppPref = getSharedPreferences(Constant.PrefName, MODE_PRIVATE);
        initview();
        Constant.isRadioButtonSelected = false;
        //end
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
//        Toast.makeText(MainActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }

    public static void showErrorMessageDialog(Context context, final String desc1, String msg) {
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
                etStoreno.setText("");
                dialog.dismiss();
//                }
            }
        });
        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);
        dialog.show();
    }

    public boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void turnOnPermissions(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    permissions_camera,
                    code
            );
        } else {
            Toast.makeText(LoginActivity.this, "Please give access to wrote storage from setting!", Toast.LENGTH_SHORT).show();
        }
    }

//    public void fetchImageInAdvance(String storeno) {
//        Constant.deleteFiles();
//        String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_IMAGES_DETAIL + storeno + "/" +Constant.storetype;
////        String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_DISPLAY_IMAGE_DATA + Constant.storetype;
//        TaskImagesAdvance taskImagesAdvance = new TaskImagesAdvance(LoginActivity.this, LoginActivity.this, storeno);
//        taskImagesAdvance.execute(Urlban);
//    }

    @Override
    public void onGetImagesDetailsResult(List<ImagesDetailModel> ImageDetalList) {
    }

//    public void createNewSignID() {
//        Sign signNew = new Sign();
//        signNew.setEnabled(true);
//        signNew.setName(signName);
//        try {
//            String buildNo_Name = "";
//            try {
//                PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//                buildNo_Name = "V1." + pInfo.versionCode;
//
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            signNew.setDescription("CP for LOGIN " + " (" + " -" + Constant.appname + "-" + Constant.selected_station_no + ")_" + buildNo_Name);
//        } catch (Exception e) {
//            signNew.setDescription("CP for LOGIN " + " ( -" + Constant.appname + ")");
//        }
//        signNew.setLatitude(String.valueOf(latitude));
//        signNew.setLongitude(String.valueOf(longitude));
//        signNew.setLocation(Constant.address);
//        signNew.setHeight(1280);
//        signNew.setWidth(800);
//        TimeZone tz = TimeZone.getDefault();
//        signNew.setTimezone(tz.getID());
//        signNew.setDaily_impressions("100");
//        signNew.setSeconds_per_flip("" + ApiClientBillBoard.timeForFlips);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(signNew);
//        RequestBody body = RequestBody.create(jsonStr,
//                MediaType.parse("application/json")
//        );
//        ApiInterface apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
//        Call<Sign> call1 = apiServiceBillboard.CreateSigns(Constant.authHeader, body);
//        call1.enqueue(new Callback<Sign>() {
//            @Override
//            public void onResponse(Call<Sign> call, Response<Sign> response) {
//                if (response.body() != null) {
//                    //Log.d("TEST", "AdvSignS " + response.body());
//                    //pick a sign
//                    //save to local
//                    if (response.body().getId() != null) {
//                        if (response.body().getId().trim().length() > 0) {
//                            ApiClientBillBoard.signId = response.body().getId();
//                            Constant.saveSign(LoginActivity.this, response.body());
//                            if (loading != null) {
//                                loading.dismiss();
//                            }
//                        } else {
//                            try {
//                                loading.dismiss();
//                            } catch (Exception e) {
//                            }
//
//                        }
//                    } else {
//                        try {
//                            loading.dismiss();
//                        } catch (Exception e) {
//                        }
//
//                    }
//
//                } else {
//                    try {
//                        loading.dismiss();
//                    } catch (Exception e) {
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Sign> call, Throwable t) {
//                call.cancel();
//                try {
//                    loading.dismiss();
//                } catch (Exception e) {
//                }
//
//            }
//        });
//    }
//
//    public void fetchAllSigns() {
//        ApiInterface apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
//        Call<ArrayList<Sign>> call1 = apiServiceBillboard.GetSigns(Constant.authHeader);
//        call1.enqueue(new Callback<ArrayList<Sign>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Sign>> call, Response<ArrayList<Sign>> response) {
//                if (response.body() != null) {
//                    if (response.body().size() > 0) {
//                        for (int i = 0; i < response.body().size(); i++) {
//                            if (response.body().get(i).getName().contains("PD_"+android_id)) {
//                                ApiClientBillBoard.signId = response.body().get(i).getId();
//                                Constant.saveSign(LoginActivity.this, response.body().get(i));
//                                break;
//                            }
//                        }
//                        if (ApiClientBillBoard.signId != null) {
//                            if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                if (loading != null) {
//                                    loading.dismiss();
//                                }
//                            } else {
//                                getLocationFromAddress(Constant.address);
//                            }
//                        } else {
//                            getLocationFromAddress(Constant.address);
//                        }
//
//                    } else {
//                        getLocationFromAddress(Constant.address);
//                    }
//                } else {
//                    getLocationFromAddress(Constant.address);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Sign>> call, Throwable t) {
//                call.cancel();
//                try {
//                    loading.dismiss();
//                } catch (Exception e) {
//                }
//
//            }
//        });
//
//    }
//
//    public void getLocationFromAddress(String strAddress) {
//        Geocoder coder = new Geocoder(LoginActivity.this);
//        List<Address> address;
//        try {
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address != null) {
//                Address location = address.get(0);
//                latitude = String.valueOf(Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
//                longitude = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
//                createNewSignID();
//            } else {
//                createNewSignID();
//            }
//
//        } catch (Exception e) {
//            createNewSignID();
//        }
//    }

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

}
