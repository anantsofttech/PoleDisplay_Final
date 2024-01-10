package com.poledisplayapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.github.demono.AutoScrollViewPager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.poledisplayapp.Api.ApiClient;
import com.poledisplayapp.Api.ApiClientBillBoard;
import com.poledisplayapp.Api.ApiClientQT;
import com.poledisplayapp.Api.ApiInterface;
import com.poledisplayapp.Task.TaskAdApprovals;
import com.poledisplayapp.Task.TaskGetAdRequest;
import com.poledisplayapp.Task.TaskImages;
import com.poledisplayapp.Task.TaskPlay;
import com.poledisplayapp.Task.TaskRetrieveAdunit;
import com.poledisplayapp.Task.TaskSubmitAdRequest;
import com.poledisplayapp.adapter.MyCustomPagerAdapter;
import com.poledisplayapp.adapter.PosOrderAdapter;
import com.poledisplayapp.models.AdApprovalModel;
import com.poledisplayapp.models.AdvSign;
import com.poledisplayapp.models.Auth_QTModel;
import com.poledisplayapp.models.CustomerOrderModel;
import com.poledisplayapp.models.GetAdRequestModel;
import com.poledisplayapp.models.ImagesDetailModel;
import com.poledisplayapp.models.Result;
import com.poledisplayapp.models.RetrieveAdunitModel;
import com.poledisplayapp.models.Sign;
import com.poledisplayapp.models.SubmitAdRequestModel;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import poledisplayapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TaskImages.TaskImagesEvent, TaskGetAdRequest.TaskGetAdRequestEvent ,
        TaskRetrieveAdunit.TaskRetrieveAdunitEvent , TaskSubmitAdRequest.TaskSubmitAdRequestEvent, TaskPlay.TaskPlayEvent,
        TaskAdApprovals.TaskAdApprovalsEvent {

    int offlineDelay = 5;
    String android_id = null;
    public static Dialog dialog;
    private static final String TAG = "datainfo:";
    private DatabaseReference mFirebaseDatabase;
    private int count = 0;
    private long startMillis = 0;;
    boolean isTouchVer = false;
    String firebaseKey, buildNo_Name;
    RelativeLayout lladv;
    public AutoScrollViewPager mViewPager;
    public MyCustomPagerAdapter myCustomPagerAdapter;
    private String imgUrlPath = Constant.IMG_BASE + Constant.IMG_BANNER_URL;
    TextView tvbottomAppname;
    RecyclerView listview;
    String signName = "";
    TextView tvLogoff;
    TextView tvbottomStationno;
    TextView tvbottomVersionno;
    TextView tvWebsiteUrl;
    TextView totalValue;
    TextView SubtotalValue;
    TextView taxValue, tvdepositValue,tax3Value,tax3 , tvTax;
    public static TextView tvSize;
    boolean isAllowLoading = true;
    String latitude = "0.0", longitude = "0.0";
    int advCount = 0;
    PosOrderAdapter adapter;
    //    List<CustomerOrderModel> ListElementsArrayList;
    List<CustomerOrderModel> ListElementsArrayList;
    //    List<String> descList;
    LinearLayout llOrderTotal, llBillingLayout, llposOrderMainLayout, llOrderfooter, lldeposit;
    String StoreNoPref;
    static String StationNoPref, appnamepref, appNicknamepref;
    boolean istenderedVal = false;
    public ImageView Headerimageview, llFooterimageview;
//    FrameLayout flVersion_footer;
//    String lastmodelForDelete;
//    AutoViewPager mViewPager;

    //    static MainActivity mainActivity;
//        public static MainActivity getInstance() {
//            return mainActivity;
//        }
    public static LruCache<String, Bitmap> memCache;
    public static MainActivity context;
    ImageView imgbanner;
    DecimalFormat df;

    String address = "";
    boolean isInternetAvail = true;
    int numberofcall;
    int num ;
    static Auth_QTModel auth_qt = new Auth_QTModel();
    ProgressDialog loading = null;
    private Handler handler = new Handler();
    private Runnable delayedRunnable;
    private boolean computerPerfectImageCalled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //fetchBillboardADVS();
        //address
//        if (Constant.isFromBlipBoard) {
//            address = Constant.address;
//            if (Constant.address != null) {
//                if (Constant.address.trim().length() > 0) {
//                    String[] splitted = Constant.address.split("-");
//                    if (splitted.length >= 2) {
//                        address = "";
//                        for (int i = 0; i < splitted.length; i++) {
//                            if (i != 0) {
//                                if (splitted[i] != null) {
//                                    if (splitted[i].trim().length() > 0) {
//                                        address = address + ", " + splitted[i].replace("                    ", " ").trim();
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//            Log.d("TEST", "ADDRESS: " + address);
//
//        }
        checkInternetAvaibilityLoop();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imgbanner = (ImageView) findViewById(R.id.imgbanner);
        df = new DecimalFormat("####0.00");
        df.setMaximumFractionDigits(2);
//        mainActivity = this;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setLogo(R.drawable.logo1);
//            getSupportActionBar().setDisplayUseLogoEnabled(true);
//        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            View cView = getLayoutInflater().inflate(R.layout.actionbar_header, null);
//            getSupportActionBar().setCustomView(cView);
        }
        Constant.AppPref = getSharedPreferences(Constant.PrefName, MODE_PRIVATE);
        if (!Constant.AppPref.getString("store", "").isEmpty() && !Constant.AppPref.getString("station", "").isEmpty()) {
            StationNoPref = Constant.AppPref.getString("station", "");
            StoreNoPref = Constant.AppPref.getString("store", "");
            appnamepref = Constant.AppPref.getString("appname", "");
            appNicknamepref = Constant.AppPref.getString("appNickname", "");
            Constant.STOREID = StoreNoPref;

        } else {
            StationNoPref = Constant.selected_station_no;
            StoreNoPref = Constant.entered_storeno;
            appnamepref = Constant.appname;
            appNicknamepref = Constant.appNickname;
            Constant.STOREID = StoreNoPref;
        }
        if (Constant.STOREID == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (Constant.STOREID.trim().length() <= 0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
//            if (Constant.isFromBlipBoard) {
//                try {
//                    android_id = Settings.Secure.getString(this.getContentResolver(),
//                            Settings.Secure.ANDROID_ID);
//                } catch (Exception e) {
//                }
//                if (android_id == null) {
//                    android_id = Constant.getUUId(MainActivity.this);
//                } else if (android_id.trim().length() <= 0) {
//                    android_id = Constant.getUUId(MainActivity.this);
//                }
//                if (android_id == null) {
//                    android_id = "emulator";
//                } else if (android_id.trim().length() <= 0) {
//                    android_id = "emulator";
//                }
//                Log.d("TEST", "deviceid" + android_id);
//                signName = "PD_" + android_id + "_" + Constant.STOREID + Constant.appNickname.replace(" ", "_");
//                Log.d("TEST", "signname" + signName);
//            }
            initview();
//            scheduleRepeatingTask(context);
        }

    }

//    private void scheduleRepeatingTask(MainActivity context) {
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        // Set the alarm to start at a specific time (you can adjust this based on your requirement)
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.HOUR_OF_DAY, 24); // Start after 24 hours
//        long intervalMillis = 24 * 60 * 60 * 1000; // Repeat every 24 hours
//
//        alarmManager.setInexactRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                intervalMillis,
//                pendingIntent
//        );
//    }


    private void initview() {
        lladv = findViewById(R.id.lladv);
        // buildNo_Name = BuildConfig.VERSION_NAME + BuildConfig.VERSION_CODE;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            buildNo_Name = pInfo.versionName + pInfo.versionCode;
            //            buildNo_Name = pInfo.versionName;
            buildNo_Name = "V1." + pInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        llHeader.setBackgroundColor(getResources().getColor(R.color.store_104color));
        Headerimageview = (ImageView) findViewById(R.id.Headerimageview);
        llFooterimageview = (ImageView) findViewById(R.id.llFooterimageview);
        listview = findViewById(R.id.listView1);
        llOrderTotal = (LinearLayout) findViewById(R.id.llOrderTotal);
//        llOrderTotal.setVisibility(View.GONE);
//        flVersion_footer = (FrameLayout) findViewById(R.id.flVersion_footer);
//        flVersion_footer.setAlpha(0.4f);
//        llWelcomeViewpagerLayout = (LinearLayout) findViewById(R.id.llWelcomeViewpagerLayout);
        llposOrderMainLayout = (LinearLayout) findViewById(R.id.llposOrderMainLayout);
        llBillingLayout = (LinearLayout) findViewById(R.id.llBillingLayout);
        llOrderfooter = (LinearLayout) findViewById(R.id.llOrderfooter);
        lldeposit = (LinearLayout) findViewById(R.id.lldeposit);
        lldeposit.setVisibility(View.GONE);
        tvLogoff = (TextView) findViewById(R.id.tvLogoff);
//        Billing footer
        tvbottomStationno = (TextView) findViewById(R.id.tvbottomStationno);
        tvbottomVersionno = (TextView) findViewById(R.id.tvbottomVersionno);
        tvbottomStationno.setText("R" + StationNoPref);
//        tvbottomVersionno.setText("V100.7");
        tvbottomVersionno.setText(buildNo_Name);
//        welcome temp
//        tvbottomStationnotemp = (TextView) findViewById(R.id.tvbottomStationnotemp);
//        tvbottomStationnotemp.setText("R" + StationNoPref);
//        tvbottomVersionnotemp = (TextView) findViewById(R.id.tvbottomVersionnotemp);
////        tvbottomVersionnotemp.setText("V100.7");
//        tvbottomVersionnotemp.setText(buildNo_Name);
        //welcome temp end
        totalValue = (TextView) findViewById(R.id.totalValue);
        SubtotalValue = (TextView) findViewById(R.id.SubtotalValue);
        taxValue = (TextView) findViewById(R.id.taxValue);
        tax3Value = (TextView) findViewById(R.id.tax3Value);
        tax3 = (TextView) findViewById(R.id.tax3);
        tvTax = (TextView) findViewById(R.id.tax);
//        tax3Value.setText("0.00");

        //need to changes start ------------
        if(StoreNoPref.equalsIgnoreCase("707")||(StoreNoPref.equalsIgnoreCase("7365"))
         || (StoreNoPref.equalsIgnoreCase("2401")) || (StoreNoPref.equalsIgnoreCase("104"))){
            tax3Value.setVisibility(View.VISIBLE);
            tax3.setVisibility(View.VISIBLE);
        }else{
            tax3Value.setVisibility(View.GONE);
            tax3.setVisibility(View.GONE);
        }
        // end **********

        tvdepositValue = (TextView) findViewById(R.id.tvdepositValue);
        tvSize = (TextView) findViewById(R.id.tvSize);
//        tvSize.setVisibility(View.GONE);
        tvSize.setVisibility(View.INVISIBLE);
        tvWebsiteUrl = (TextView) findViewById(R.id.tvWebsiteUrl);
        tvWebsiteUrl.setPaintFlags(tvWebsiteUrl.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tvLogoffWelcometemp = (TextView) findViewById(R.id.tvLogoffWelcometemp);
//        tvLogoffWelcometemp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Constant.AppPref.edit().putString("store", "").putString("station", "").apply();
//
//                boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
//                if(!storeRemember){
//                    Constant.AppPref.edit().putString("store", "").putString("station", "").apply();
//                    Constant.AppPref.edit().putBoolean("storeRemember", false).putBoolean("stationRemember", false).apply();
//                }
//
//                ListElementsArrayList.clear();
//                Constant.customerOrderList.clear();
//                Constant.stationList.clear();
//                finish();
//
//                //code for clear screen
//                Intent mStartActivity = new Intent(MainActivity.this, LoginActivity.class);
//                int mPendingIntentId = 123456;
//                PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//                AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
//                assert mgr != null;
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                System.exit(0);
//                //end ***********
//            }
//        });
        tvLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Constant.AppPref.edit().putString("store", "").putString("station", "").apply();
                boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
                if (!storeRemember) {
                    Constant.AppPref.edit().putString("store", "").putString("station", "").putString("appname", "").putString("appNickname", "").putString("storeType", "").apply();
                    Constant.AppPref.edit().putBoolean("storeRemember", false).putBoolean("stationRemember", false).apply();
                }
                Constant.isTimeToRefreshImg = false;
                ApiClientBillBoard.signId = "";
                if (ListElementsArrayList != null)
                    ListElementsArrayList.clear();
                if (Constant.customerOrderList != null)
                    Constant.customerOrderList.clear();
                Constant.stationList.clear();
                Constant.isFromQT=false;
//                finish();
                //below code for direct exit from tablet desktop on logoff
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishAndRemoveTask();
//                } else {
//                    System.runFinalizersOnExit(true);
//                }
//                System.exit(0);
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                //end
                //code for clear screen
//                Intent mStartActivity = new Intent(MainActivity.this, LoginActivity.class);
//                int mPendingIntentId = 123456;
//                PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//                AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
//                assert mgr != null;
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                System.exit(0);
                //end ***********
            }
        });
        //ll_empty_view = (LinearLayout) findViewById(R.id.ll_empty_view);
        //ll_empty_view.setVisibility(View.GONE);
//        try{
        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewPage);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {
                    Log.d("TEST", "HERE IN POSITION" + positionOffset);
                    if (Constant.isNetworkAvailable(MainActivity.this)) {
                        isInternetAvail = true;
//                        if (Constant.isFromBlipBoard) {
//                            if (Constant.ImageDetalList != null) {
//                                if (Constant.ImageDetalList.size() > 0) {
//                                    if (advCount <= Constant.ImageDetalList.size() - 1) {
//                                        //nothing will plan later if any change in logic // kaveri
//                                    } else {
//                                        advCount = 0;
//                                    }
//                                    Log.d("TEST", "" + Constant.ImageDetalList.get(advCount).getId());
//                                    if (Constant.ImageDetalList.get(advCount).getId() != null) {
//                                        if (!Constant.ImageDetalList.get(advCount).getId().equalsIgnoreCase("0")) {
//                                            if (!Constant.ImageDetalList.get(advCount).getId().equalsIgnoreCase("-1")) {
//                                                recordFlips(Constant.ImageDetalList.get(advCount).getId());
//                                                advCount++;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    } else {
                        isInternetAvail = false;

                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //mViewPager.setIndeterminate(true);
//        } catch (Exception e){
//
//        }
        //for the current item image
        //end
       /* if (Constant.isTimeToRefreshImg) {
           //Log.d("CounterForImage", "CounterForImage:" + Constant.TransactionCounterForImage);
           //Log.d("refresh", "refresh:");
//            Toast.makeText(MainActivity.this, "refersh", Toast.LENGTH_LONG).show();
            ll_empty_view.setVisibility(View.VISIBLE);
            callImagesWs();

        } else {
            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, mViewPager, false);
            mViewPager.setAdapter(myCustomPagerAdapter);
        }*/
        //for the current item image
       /* mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
               //Log.d("currentPage", "currentPage:" + currentPage);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });*/
        //end current item
        /*FrameLayout flVersion_footer_welcome_dialog = findViewById(R.id.flVersion_footer_welcome_dialog);
        flVersion_footer_welcome_dialog.setAlpha(0.4f);*/
        TextView tvLogoffWelcome1 = findViewById(R.id.tvLogoffWelcome);
        tvbottomAppname = (TextView) findViewById(R.id.tvbottomAppname);
        TextView tvbottomDialogStationno1 = (TextView) findViewById(R.id.tvbottomStationno1);
        TextView tvbottomDialogVersionno1 = (TextView) findViewById(R.id.tvbottomVersionno1);
        LinearLayout llbottomver = findViewById(R.id.llbottomver);
        llbottomver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tapToRefresh();
            }
        });
//        if (!Constant.isFromBlipBoard) {
//            if (appnamepref != null && !appnamepref.trim().isEmpty()) {
//                tvbottomAppname.setText("" + appnamepref.trim());
//            }
//        } else {
            if (appNicknamepref != null && !appNicknamepref.trim().isEmpty()) {
                if (ApiClientBillBoard.signId != null) {
                    if (ApiClientBillBoard.signId.trim().length() > 0) {
                        try {
                            tvbottomAppname.setText(Constant.STOREID + " - " + ApiClientBillBoard.signId + " - " + Constant.appNickname.trim());
                        } catch (Exception e) {
                        }
                    }
                }
            }
//        }
        tvbottomDialogStationno1.setText("R" + StationNoPref);
        tvbottomDialogVersionno1.setText(buildNo_Name);
        tvLogoffWelcome1.setOnClickListener(new View.OnClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Maximum duration between two clicks in milliseconds
            long lastClickTime = 0;

            @Override
            public void onClick(View view) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // Double click detected
                    showPopupDialog();
                }
                lastClickTime = clickTime;
            }
        });

        tvTax.setOnClickListener(new View.OnClickListener() {
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Maximum duration between two clicks in milliseconds
            long lastClickTime = 0;

            @Override
            public void onClick(View view) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // Double click detected
                    showPopupDialog();
                }
                lastClickTime = clickTime;
            }
        });

//        if (Constant.isFromBlipBoard) {
//            //display blipboard advertisement
//            if (Constant.ImageDetalList == null) {
//                //dummy
//                setDummyAdvertisement(); // blipboard
//            } else if (Constant.ImageDetalList.size() <= 0) {
//                //dummy
//                setDummyAdvertisement(); // blipboard
//            } else {
//                if (!Constant.getTimeToRefresh(MainActivity.this, false)) {
//                    myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, false);
//                    mViewPager.setAdapter(myCustomPagerAdapter);
//                    mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
//                    mViewPager.startAutoScroll();
//                    mViewPager.setVisibility(View.VISIBLE);
//                }
//            }
//            scheduleAsveCalls(true);
//            //get header footer with blipboard
//        } else {

//            Setting the NO image when reponse of get A drequest id null or NO resposne in return.
//            This is only display when app come from splash screen when in splash screen ad unit is not found or ad request is null
            if (Constant.isgetAdreponesnull){
                Constant.isgetAdreponesnull=false;
                if (Constant.loading != null && Constant.loading.isShowing()) {
                    Constant.loading.dismiss();
                    Constant.loading = null;
                }

//    If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                setnoimage();
                Constant.iscomefrom_adrequest_Same = true;
                callcomputerperfectimage();
                if (Constant.STOREID.equals("707") || Constant.STOREID.equals("7365")) {
                    Toast.makeText(context, "No ads found on the ad server, calling the default images.", Toast.LENGTH_LONG).show();
                }
//               END
            }else {
//                END
//                Checking for QT or not
//                if this from QT then check for saved images and we found the image then check for the is come from loginscreen
//                If the login screen is true then we need to call the Webservice of QT again.
                if (Constant.isFromQT) {

                    if (Constant.isfromlogin) {
                        if (Constant.ImageDetalList3 != null && !Constant.ImageDetalList3.isEmpty()) {
                            Constant.ImageDetalList3.clear();
                            Constant.isfromlogin = false;
                            Log.e(TAG, "initview: 12345 ");
                        }
                    }

                    if (Constant.ImageDetalList3 != null) {
                        if (Constant.ImageDetalList3.size() > 0) {
                            Constant.ImageDetalList = new ArrayList<>();
                            Collections.sort(Constant.ImageDetalList3, new Comparator<ImagesDetailModel>() {
                                public int compare(ImagesDetailModel obj1, ImagesDetailModel obj2) {
                                    // ## Ascending order
                                    return obj1.getImage().compareToIgnoreCase(obj2.getImage()); // To compare string values
                                }
                            });
                            Constant.ImageDetalList.addAll(Constant.ImageDetalList3);
                            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, false);
                            mViewPager.setAdapter(myCustomPagerAdapter);
                            mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
                            mViewPager.startAutoScroll();
                            //mViewPager.setIndeterminate(true);
                        } else {
//                        edited by Varun for QT
//                        We are checking for QT flag is true or false
                            callImagesWs();
                            if (Constant.isFromQT) {
                                Log.e(TAG, "initview: QT1.1");
                                if (isCallingAllowed()) {
                                    callgetAdRequest(StoreNoPref + "_" + StationNoPref);
                                }else{
                                    callcomputerperfectimage();
                                }
                            }
                        }
                    }
                }
//                END
//                Added bescause before when we logout from one store and login to other store then the Old images is still there
                else {
                    if (Constant.LocalImageDetalList == null) {
                        Constant.LocalImageDetalList = Constant.getImages(MainActivity.this, "Home", Constant.STOREID);
                    } else if (Constant.LocalImageDetalList.size() <= 0) {
                        Constant.LocalImageDetalList = Constant.getImages(MainActivity.this, "Home", Constant.STOREID);
                    }
//                    If the login screen is true then we need to call the Webservice again.
                    if (Constant.isfromlogin || Constant.isfromsplash){
                        if (Constant.LocalImageDetalList!=null&&!Constant.LocalImageDetalList.isEmpty()) {
                            Constant.LocalImageDetalList.clear();
                            Constant.isfromlogin = false;
                            Constant.isfromsplash = false;
                        }
                    }
//                    END

                    if (Constant.LocalImageDetalList != null) {
                        if (Constant.LocalImageDetalList.size() > 0) {
                            Constant.ImageDetalList = new ArrayList<>();
                            Collections.sort(Constant.LocalImageDetalList, new Comparator<ImagesDetailModel>() {
                                public int compare(ImagesDetailModel obj1, ImagesDetailModel obj2) {
                                    // ## Ascending order
                                    return obj1.getImage().compareToIgnoreCase(obj2.getImage()); // To compare string values
                                }
                            });
                            Constant.ImageDetalList.addAll(Constant.LocalImageDetalList);
                            if (Constant.ImageDetalList == null) {
//                                setDummyAdvertisement(); // corpotate
                                setnoimage();
                            } else if (Constant.ImageDetalList.size() <= 0) {
                                Constant.ImageDetalList = new ArrayList<>();
//                                setDummyAdvertisement(); // corporate
                                setnoimage();
                            }
                            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, false);
                            mViewPager.setAdapter(myCustomPagerAdapter);
                            mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
                            mViewPager.startAutoScroll();
                            //mViewPager.setIndeterminate(true);
                        } else {
//                        edited by Varun for QT
//                        We are checking for QT flag is true or false
                            callImagesWs();

                            if (Constant.isFromQT) {
                                Log.e(TAG, "initview: QT1");
                                if (isCallingAllowed()) {
                                    callgetAdRequest(StoreNoPref + "_" + StationNoPref);
                                }else{
                                    callcomputerperfectimage();
                                }
                            }
                        }
                    } else {
//                    edited by Varun for QT
//                    We are checking for QT flag is true or false

                        callImagesWs();
                        if (Constant.isFromQT) {
                            Log.e(TAG, "initview: QT2");
                            if (isCallingAllowed()) {
                                callgetAdRequest(StoreNoPref + "_" + StationNoPref);
                            }else{
                                callcomputerperfectimage();
                            }
                        }
                    }
                }
            }
//        }

        if (Constant.LocalImageHeaderList == null) {
            Constant.LocalImageHeaderList = Constant.getImages(MainActivity.this, "Header", Constant.STOREID);
        } else if (Constant.LocalImageHeaderList.size() <= 0) {
            Constant.LocalImageHeaderList = Constant.getImages(MainActivity.this, "Header", Constant.STOREID);
        }
        if (Constant.LocalImageFooterList == null) {
            Constant.LocalImageFooterList = Constant.getImages(MainActivity.this, "Footer", Constant.STOREID);
        } else if (Constant.LocalImageFooterList.size() <= 0) {
            Constant.LocalImageFooterList = Constant.getImages(MainActivity.this, "Footer", Constant.STOREID);
        }
        if (Constant.LocalImageHeaderList != null) {
            if (Constant.LocalImageHeaderList.size() > 0) {
                Constant.ImageHeaderList.addAll(Constant.LocalImageHeaderList);
                setHeader();
            }
        }
        if (Constant.LocalImageFooterList != null) {
            if (Constant.LocalImageFooterList.size() > 0) {
                Constant.ImageFooterList.addAll(Constant.LocalImageFooterList);
                setfooter();
            }
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code
                callfirebasechildfun();
            }
        });

    }

//    Pop - up for 3 option logout / refesh add etc
    private void showPopupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select an Option")
                .setItems(new CharSequence[]{"Log Off/Delete Log In", "Force Ad/Header/Footer download", "View Downloading Activity"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle option selection
                        switch (which) {
                            case 0:
                                // Option 1 selected
                                logoffFromWelcome();
                                break;
                            case 1:
                                // Option 2 selected
                                if (Constant.isFromQT){
                                    Log.e(TAG, "QT7" );
                                    if (Constant.localgetAdRequestModel!=null && !Constant.localgetAdRequestModel.isEmpty()){
                                        Constant.localgetAdRequestModel.clear();
                                    }
                                    Constant.iscomefrom_adrequest_Same=false;
//                                    Checking if the store time is closed or not
                                    if (isCallingAllowed()) {
                                        callQT_Token();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                                        callcomputerperfectimage();
                                    }
//                                    END
                                    mViewPager.stopAutoScroll();
                                }else{
                                    callImagesWs();
                                }
                                Toast.makeText(MainActivity.this, "Images refresh", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                // Option 3 selected
                                Toast.makeText(MainActivity.this, "Option 3 selected", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle cancel click
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    This Api is used to get the token id
    public void callQT_Token() {

        handler.removeCallbacks(delayedRunnable);

        computerPerfectImageCalled = false;

        if (Constant.localgetAdRequestModel != null && !Constant.localgetAdRequestModel.isEmpty()) {
            Constant.localgetAdRequestModel.clear();
        }

        loading = new ProgressDialog(context, R.style.MyprogressDTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
        Constant.loading = loading;

//        Edited by Varunf or delete old images
        Constant.deleteFile();
        mViewPager.stopAutoScroll();
//        END

        auth_qt.setUsername(Constant.QT_USERNAME);
        auth_qt.setPassword(Constant.QT_PASSWORD);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(auth_qt);
        RequestBody body = RequestBody.create(jsonStr,
                MediaType.parse("application/json")
        );
        ApiInterface apiService= ApiClientQT.getClient().create(ApiInterface.class);
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
                        callgetAdunit(StoreNoPref+"_"+StationNoPref);
//                        Checking if the store time is closed or not
//                        if (isCallingAllowed()) {
//                            callgetAdunit(StoreNoPref+"_"+StationNoPref);
//                        }else{
//                            Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
//                            callcomputerperfectimage();
//                        }
//                        END
                    }
                }
            }

            @Override
            public void onFailure(Call<Auth_QTModel> call, Throwable t) {
                call.cancel();
//   If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                setnoimage();
                Constant.iscomefrom_adrequest_Same=true;
                callcomputerperfectimage();
                if (Constant.STOREID.equals("707") || Constant.STOREID.equals("7365")) {
                    Toast.makeText(context, "Unable to access the ad server, so we are calling the default images.", Toast.LENGTH_LONG).show();
                }
//                END
                Toast.makeText(MainActivity.this, "Authentication service fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Calling this to get the Ad unit name
    public static void callgetAdunit(String adUnitname) {

        String url = null;
        //URL :- https://api.placeexchange.com/v3/orgs/00cb716a-c7f8-4f14-8244-cf510531e696/adunits
        url = Constant.BASE_URL_QT + Constant.ORGS + Constant.QT_ID + Constant.WS_AD_UNIT + "/" +adUnitname;
        TaskRetrieveAdunit taskretrieveadunit = new TaskRetrieveAdunit(context,context , Constant.localAuthQTlist.getAccess_token());
        taskretrieveadunit.execute(url);
    }

    @Override
    public void onRetrieveAdunitResult(List<RetrieveAdunitModel> retrieveAdunitModel, boolean b) {

        handler.removeCallbacks(delayedRunnable);

        numberofcall=0;
        if (b && retrieveAdunitModel==null || retrieveAdunitModel.isEmpty()){
//           We show the Adunit not found pop-up when when there is no Adunit added in QT server for that specific store and station
//            Adunitnotfound_pop_up(context);
            callcomputerperfectimage();
            if (Constant.STOREID.equals("707") || Constant.STOREID.equals("7365")) {
                Toast.makeText(context, "Unable to access the ad server, so we are calling the default images.", Toast.LENGTH_LONG).show();
            }
//            END
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

                            callgetAdRequest(StoreNoPref + "_" + StationNoPref);
                            numberofcall++;
                        } else {
                            if (Constant.loading != null && Constant.loading.isShowing()) {
                                Constant.loading.dismiss();
                            }

//                            Adunitnotfound_pop_up(context);
                            callcomputerperfectimage();
                            if (Constant.STOREID.equals("707") || Constant.STOREID.equals("7365")) {
                                Toast.makeText(context, "Unable to access the ad server, so we are calling the default images.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }

    }

    public void callgetAdRequest(String name) {

        handler.removeCallbacks(delayedRunnable);

        Constant.deleteFiles();

        if (Constant.LocalImageDetalList2!=null && !Constant.LocalImageDetalList2.isEmpty()){
            Constant.LocalImageDetalList2.clear();
        }
        if (Constant.ImageDetalList3!=null && !Constant.ImageDetalList3.isEmpty()){
            Constant.ImageDetalList3.clear();
        }

        String url = null;

        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.WS_AD_UNIT + "/" +name +Constant.WS_SUBMIT_AD_REQUEST;
        TaskGetAdRequest taskGetAdRequest = new TaskGetAdRequest(this,context,Constant.localAuthQTlist.getAccess_token(), name);
        taskGetAdRequest.execute(url);
    }

    @Override
    public void onGetAdRequestResult(List<GetAdRequestModel> getAdRequestModel, String ad_unit_name) {

        num=0;

//        Edited by Varun for when there is no response from QT or we don't get any Ad in return then we will show the dummy Ad
//        We show computer perfect images when new Image is same as Previous Image
        if (!Constant.localgetAdRequestModel.isEmpty() && Constant.localgetAdRequestModel!=null){
            if (Constant.localgetAdRequestModel.get(0).getCreative().getName().equals(getAdRequestModel.get(0).getCreative().getName())) {
                Constant.iscomefrom_adrequest_Same = true;
                callcomputerperfectimage();
            }
        }else {
//            END
//            We clear the local model before adding the new model in it
            if ( Constant.localgetAdRequestModel!=null && !Constant.localgetAdRequestModel.isEmpty()){
                Constant.localgetAdRequestModel.clear();
            }
//            END

            if (getAdRequestModel != null) {
                Constant.localgetAdRequestModel.addAll(getAdRequestModel);
//                if (numberofcall == num) {
//////                Calling the fucntion to display the images
////                    displayQT(Constant.localgetAdRequestModel, ad_unit_name);
////                }
                displayQT(Constant.localgetAdRequestModel, ad_unit_name);
            } else {

                if (Constant.localgetAdRequestModel == null || Constant.localgetAdRequestModel.isEmpty()) {
                    if (loading != null && loading.isShowing()) {
                        loading.dismiss();
                    }
//    If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                        setnoimage();
                        Constant.iscomefrom_adrequest_Same =true;
                        callcomputerperfectimage();
                        if (Constant.STOREID.equals("707") || Constant.STOREID.equals("7365")) {
                            Toast.makeText(context, "No ads found on the ad server, calling the default images.", Toast.LENGTH_LONG).show();
                        }
//                        END
                    } else {
//                    Calling the fucntion to display the images
                        displayQT(Constant.localgetAdRequestModel, ad_unit_name);
                    }

//                END
            }
        }
    }

    private void callcomputerperfectimage() {
        // Check if the function has already been called
        if (computerPerfectImageCalled) {
            return; // Do nothing if already called
        }

        if (Constant.Computerperfect != null && !Constant.Computerperfect.isEmpty()) {
            Constant.iscomefrom_adrequest_Same = false;
            int selectedImageIndex = Constant.count;
            if (selectedImageIndex >= Constant.Computerperfect.size()) {
                Constant.count = 0;
                selectedImageIndex = Constant.count;
            }
            Constant.count++;
            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Collections.singletonList(Constant.Computerperfect.get(selectedImageIndex)), true);
            mViewPager.setAdapter(myCustomPagerAdapter);

            call_delay_again();

            // Set the flag to true to indicate that the function has been called
            computerPerfectImageCalled = true;
        } else {
            // If Computerperfect is null or empty, callImagesWs
            callImagesWs();
        }
    }

    private void call_delay_again() {

        // Delay in milliseconds (15 seconds in this case)
        long delayMillis = (long) (15 * 1000);

        delayedRunnable = new Runnable() {
            @Override
            public void run() {
                // Reset the flag so the function can be called again
                computerPerfectImageCalled = false;

                if (Constant.localgetAdRequestModel != null && !Constant.localgetAdRequestModel.isEmpty()) {
                    Constant.localgetAdRequestModel.clear();
                }
                Toast.makeText(MainActivity.this, "Calling AGAIN", Toast.LENGTH_SHORT).show();

                // Checking if the store time is closed or not
                if (llBillingLayout.getVisibility() == View.GONE) {
                    if (isCallingAllowed()) {
                        callgetAdunit(StoreNoPref + "_" + StationNoPref);
//                        callQT_Token();
                    } else {
                        Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                        // Call the function again
                        callcomputerperfectimage();
                    }
                    // END
                }
            }
        };
        handler.postDelayed(delayedRunnable, delayMillis);

    }

    private void displayQT(List<GetAdRequestModel> getAdRequestModel, String ad_unit_name) {


        if (!Constant.ImageDetalList3.isEmpty() && Constant.ImageDetalList3!=null){
            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList3, true);
            mViewPager.setAdapter(myCustomPagerAdapter);
            mViewPager.setOffscreenPageLimit(Constant.ImageDetalList3.size());
            mViewPager.startAutoScroll();
            mViewPager.setCurrentItem(0);
            if (mViewPager.getVisibility() == View.VISIBLE) {
                mViewPager.startAutoScroll();
            }
        }else{
//     If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//               setnoimage();
            Constant.iscomefrom_adrequest_Same=true;
            callcomputerperfectimage();
            if (Constant.STOREID.equals("707") || Constant.STOREID.equals("7365")) {
                Toast.makeText(context, "No ads found on the ad server, calling the default images.", Toast.LENGTH_LONG).show();
            }
//                END
        }

        for (int i = 0; i < getAdRequestModel.size(); i++) {
            if (getAdRequestModel.get(i).getCreative() != null) {
                double min_duration = getAdRequestModel.get(i).getCreative().getDuration();
//                        ?used to call the proof of play function
                callplays(getAdRequestModel.get(i).getContext(),ad_unit_name,min_duration);
            }
        }
//            END
    }

    private void callplays(String scontext, String ad_unit_name, double min_duration) {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        String url = null;
        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.WS_AD_UNIT + "/" +ad_unit_name +Constant.WS_PLAYS;
        TaskPlay taskPlay = new TaskPlay(scontext,this,getApplicationContext(),Constant.localAuthQTlist.getAccess_token(),ts,min_duration);
        taskPlay.execute(url);

    }

    @Override
    public void onGetplay(Object o, double min_duration) {

        // Delay in milliseconds (15 seconds in this case)
        long delayMillis = (long) (min_duration * 1000);

        delayedRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Calling AGAIN", Toast.LENGTH_SHORT).show();
//                  Checking if the store time is closed or not
                if (llBillingLayout.getVisibility()==View.GONE) {
                    if (isCallingAllowed()) {
//                    callgetAdunit(StoreNoPref+"_"+StationNoPref);
                        callQT_Token();
                    } else {
                        Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                        callcomputerperfectimage();
                    }
                }
//                END
            }
        };

        handler.postDelayed(delayedRunnable, delayMillis);

    }


    private void callfirebasechildfun() {
      /*  if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
            setHeader();
        }
        if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
            setfooter();
        }*/
        try {
            ListElementsArrayList = new ArrayList<CustomerOrderModel>();
            adapter = new PosOrderAdapter(MainActivity.this,
                    ListElementsArrayList);
        } catch (Exception e) {
        }
//        listview.setAdapter(adapter);
//         firebase childeventListener method        // firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        ////       Constant.OrderKeyList.clear();

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mFirebaseDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                        Log.e("firebase:",  "added");

                        CustomerOrderModel customerOrderModel = new CustomerOrderModel();
                        if (dataSnapshot.child("custid").getValue() != null) {
                            customerOrderModel.setCustid(String.valueOf(dataSnapshot.child("custid").getValue()));
                        }
                        if (dataSnapshot.child("description").getValue() != null) {
                            customerOrderModel.setDescription(String.valueOf(dataSnapshot.child("description").getValue()));
                        }
                        if (dataSnapshot.child("IsEndTransaction").getValue() != null) {
                            customerOrderModel.setEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsEndTransaction").getValue())));
                        }
                        if (dataSnapshot.child("price").getValue() != null) {
                            customerOrderModel.setPrice(String.valueOf(dataSnapshot.child("price").getValue()));
                        }
                        if (dataSnapshot.child("qty").getValue() != null) {
                            customerOrderModel.setQty(String.valueOf(dataSnapshot.child("qty").getValue()));
                        }
                        if (dataSnapshot.child("size").getValue() != null) {
                            customerOrderModel.setSize(String.valueOf(dataSnapshot.child("size").getValue()));
                        }
                        if (dataSnapshot.child("sizeColumnFlag").getValue() != null) {
                            customerOrderModel.setSizeColumnFlag(String.valueOf(dataSnapshot.child("sizeColumnFlag").getValue()));
                        }
                        if (dataSnapshot.child("station").getValue() != null) {
                            customerOrderModel.setStation(String.valueOf(dataSnapshot.child("station").getValue()));
                        }
                        if (dataSnapshot.child("store").getValue() != null) {
                            customerOrderModel.setStore(String.valueOf(dataSnapshot.child("store").getValue()));
                        }
                        if (dataSnapshot.child("subTotal").getValue() != null) {
                            customerOrderModel.setSubTotal(String.valueOf(dataSnapshot.child("subTotal").getValue()));
                        }
                        if (dataSnapshot.child("tax").getValue() != null) {
                            customerOrderModel.setTax(String.valueOf(dataSnapshot.child("tax").getValue()));
                        }
                        if (dataSnapshot.child("tax3").getValue() != null) {
                            customerOrderModel.setTax3(String.valueOf(dataSnapshot.child("tax3").getValue()));
                        }
                        if (dataSnapshot.child("IsConvFee").getValue() != null) {
                            customerOrderModel.setIsConvFee(String.valueOf(dataSnapshot.child("IsConvFee").getValue()));
                        }
                        if (dataSnapshot.child("ConvFeeName").getValue() != null) {
                            customerOrderModel.setConvFeeName(String.valueOf(dataSnapshot.child("ConvFeeName").getValue()));
                        }
                        if (dataSnapshot.child("BottleDipositAmt").getValue() != null) {
                            customerOrderModel.setBottleDipositAmt(String.valueOf(dataSnapshot.child("BottleDipositAmt").getValue()));
                        }
                        if (dataSnapshot.child("total").getValue() != null) {
                            customerOrderModel.setTotal(String.valueOf(dataSnapshot.child("total").getValue()));
                        }
                        if (dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue() != null) {
                            customerOrderModel.setDisplayItemOnEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue())));
                        }
                        if (dataSnapshot.child("IsTenderScreen").getValue() != null) {
                            customerOrderModel.setTenderScreen(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsTenderScreen").getValue())));
                        }
                        if (customerOrderModel != null) {
                            customerOrderModel.setFirebasekey(dataSnapshot.getKey());
                        }
                        if (customerOrderModel != null) {
                            if (customerOrderModel.getStation() != null && customerOrderModel.getStation().equalsIgnoreCase(StationNoPref)
                                    && customerOrderModel.getStore() != null && customerOrderModel.getStore().equalsIgnoreCase(StoreNoPref)) {
                                Constant.customerOrderList.add(customerOrderModel);
                                firebaseKey = dataSnapshot.getKey();
                            }
                        }
                        if (Constant.customerOrderList != null && Constant.customerOrderList.size() > 0) {
                            if (customerOrderModel != null && customerOrderModel.getStation() != null && customerOrderModel.getStation().equalsIgnoreCase(StationNoPref)
                                    && customerOrderModel.getStore() != null && customerOrderModel.getStore().equalsIgnoreCase(StoreNoPref)) {
                                CustomerOrderModel lastOrderModel = Constant.customerOrderList.get(Constant.customerOrderList.size() - 1);
                                String lastItemDesc = lastOrderModel.getDescription();
                                //this is for displayendtransction (true/false) new chnages start -------
//                        if (lastOrderModel.isDisplayItemOnEndTransaction()
//                                && !lastOrderModel.getPrice().trim().isEmpty()
//                                && !lastOrderModel.getQty().equals("0")) {
//
//                            displaydetails(lastOrderModel);
//
//                        } else if(!lastOrderModel.isDisplayItemOnEndTransaction() && !lastOrderModel.getPrice().trim().isEmpty()
//                                && !lastOrderModel.getQty().equals("0")){
//                            //this is for one by one transaction
//                            displayPreviousWorkingdetails(lastOrderModel, lastItemDesc);
//
//                        }else{
//                            if (llBillingLayout != null && llBillingLayout.getVisibility() == View.VISIBLE) {
//                                llBillingLayout.setVisibility(View.GONE);
//                                llposOrderMainLayout.setVisibility(View.GONE);
//                                llOrderfooter.setVisibility(View.GONE);
//                                llOrderTotal.setVisibility(View.GONE);
//                                lladv.setVisibility(View.VISIBLE);
//                                if(mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0)
//                                    mViewPager.startAutoScroll();
//                            }
//                        }
                                //end *******************
                                //this is for displayendtransction old working code start-----
                                if (lastOrderModel.isDisplayItemOnEndTransaction()) {
                                    displaydetails(lastOrderModel);

                                } else {
                                    //this is for one by one transaction
                                    displayPreviousWorkingdetails(lastOrderModel, lastItemDesc);
                                }
                                //end -----
                            } else {
                                if (Constant.customerOrderList == null || Constant.customerOrderList.size() == 0) {
                                    lladv.setVisibility(View.VISIBLE);
                                    //mViewPager.startAutoScroll();
                                    if (!Constant.isFromQT) {
                                        mViewPager.startAutoScroll();
                                    }
                                    llBillingLayout.setVisibility(View.GONE);
                                    llposOrderMainLayout.setVisibility(View.GONE);
                                    llOrderfooter.setVisibility(View.GONE);
                                    llOrderTotal.setVisibility(View.GONE);
                                    if (ListElementsArrayList != null)
                                        ListElementsArrayList.clear();
                                    if (Constant.customerOrderList != null)
                                        Constant.customerOrderList.clear();
                                    if (Constant.descList != null)
                                        Constant.descList.clear();
                                    istenderedVal = false;
//
                                }
                            }
                        } else {
                            if (llBillingLayout.getVisibility() == View.VISIBLE) {
                                lladv.setVisibility(View.VISIBLE);
                                // mViewPager.startAutoScroll();
                                if (!Constant.isFromQT) {
                                    mViewPager.startAutoScroll();
                                }
                                llposOrderMainLayout.setVisibility(View.GONE);
                                llOrderfooter.setVisibility(View.GONE);
                                llOrderTotal.setVisibility(View.GONE);
                                llBillingLayout.setVisibility(View.GONE);
                                if (ListElementsArrayList != null)
                                    ListElementsArrayList.clear();
                                if (Constant.customerOrderList != null)
                                    Constant.customerOrderList.clear();
                                istenderedVal = false;

                                if (Constant.descList != null)
                                    Constant.descList.clear();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        Log.e("firebase:",  "changed");

                        String commentKey = dataSnapshot.getKey();

                        CustomerOrderModel customerOrderModelchanged = new CustomerOrderModel();
                        if (dataSnapshot.child("custid").getValue() != null) {
                            customerOrderModelchanged.setCustid(String.valueOf(dataSnapshot.child("custid").getValue()));
                        }
                        if (dataSnapshot.child("description").getValue() != null) {
                            customerOrderModelchanged.setDescription(String.valueOf(dataSnapshot.child("description").getValue()));
                        }
                        if (dataSnapshot.child("IsEndTransaction").getValue() != null) {
                            customerOrderModelchanged.setEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsEndTransaction").getValue())));
                        }
                        if (dataSnapshot.child("price").getValue() != null) {
                            customerOrderModelchanged.setPrice(String.valueOf(dataSnapshot.child("price").getValue()));
                        }
                        if (dataSnapshot.child("qty").getValue() != null) {
                            customerOrderModelchanged.setQty(String.valueOf(dataSnapshot.child("qty").getValue()));
                        }
                        if (dataSnapshot.child("size").getValue() != null) {
                            customerOrderModelchanged.setSize(String.valueOf(dataSnapshot.child("size").getValue()));
                        }
                        if (dataSnapshot.child("sizeColumnFlag").getValue() != null) {
                            customerOrderModelchanged.setSizeColumnFlag(String.valueOf(dataSnapshot.child("sizeColumnFlag").getValue()));
                        }
                        if (dataSnapshot.child("station").getValue() != null) {
                            customerOrderModelchanged.setStation(String.valueOf(dataSnapshot.child("station").getValue()));
                        }
                        if (dataSnapshot.child("store").getValue() != null) {
                            customerOrderModelchanged.setStore(String.valueOf(dataSnapshot.child("store").getValue()));
                        }
                        if (dataSnapshot.child("subTotal").getValue() != null) {
                            customerOrderModelchanged.setSubTotal(String.valueOf(dataSnapshot.child("subTotal").getValue()));
                        }
                        if (dataSnapshot.child("tax").getValue() != null) {
                            customerOrderModelchanged.setTax(String.valueOf(dataSnapshot.child("tax").getValue()));
                        }
                        if (dataSnapshot.child("tax3").getValue() != null) {
                            customerOrderModelchanged.setTax3(String.valueOf(dataSnapshot.child("tax3").getValue()));
                        }

                        if (dataSnapshot.child("IsConvFee").getValue() != null) {
                            customerOrderModelchanged.setIsConvFee(String.valueOf(dataSnapshot.child("IsConvFee").getValue()));
                        }
                        if (dataSnapshot.child("ConvFeeName").getValue() != null) {
                            customerOrderModelchanged.setConvFeeName(String.valueOf(dataSnapshot.child("ConvFeeName").getValue()));
                        }

                        if (dataSnapshot.child("BottleDipositAmt").getValue() != null) {
                            customerOrderModelchanged.setBottleDipositAmt(String.valueOf(dataSnapshot.child("BottleDipositAmt").getValue()));
                        }
                        if (dataSnapshot.child("total").getValue() != null) {
                            customerOrderModelchanged.setTotal(String.valueOf(dataSnapshot.child("total").getValue()));
                        }
                        if (dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue() != null) {
                            customerOrderModelchanged.setDisplayItemOnEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue())));
                        }
                        if (dataSnapshot.child("IsTenderScreen").getValue() != null) {
                            customerOrderModelchanged.setTenderScreen(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsTenderScreen").getValue())));
                        }
                        if (customerOrderModelchanged != null) {
                            customerOrderModelchanged.setFirebasekey(dataSnapshot.getKey());
                        }
                        //old working code for update start
//                        if (customerOrderModelchanged != null && customerOrderModelchanged.getStation() != null && customerOrderModelchanged.getStation().equalsIgnoreCase(StationNoPref)
//                                && customerOrderModelchanged.getStore() != null && customerOrderModelchanged.getStore().equalsIgnoreCase(StoreNoPref)) {
//                            if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
//                                for (int i = 0; i < ListElementsArrayList.size(); i++) {
//                                    if (ListElementsArrayList.get(i).getFirebasekey().equals(dataSnapshot.getKey())) {
//                                        //below if condition is update only when display endtransaction na hoy
////                                jo  bane fetures ma update fun joye to niche ni if condition remove karvi
////                                if(!customerOrderModelchanged.isDisplayItemOnEndTransaction()){
//                                        updateChangesInListview(i, customerOrderModelchanged);
////                                }
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                        // end working code for update

                        //new start
                        if (customerOrderModelchanged != null && customerOrderModelchanged.getStation() != null && customerOrderModelchanged.getStation().equalsIgnoreCase(StationNoPref)
                                && customerOrderModelchanged.getStore() != null
                                && customerOrderModelchanged.getStore().equalsIgnoreCase(StoreNoPref)) {

                            if (customerOrderModelchanged.getQty().equals("0")) {
                                //Log.d(TAG, "testing");
                                if (customerOrderModelchanged.isEndTransaction()) { //clear screen and rrrrrrrrrrrr
                                    //Log.d(TAG, "remove:" + "");
                                    Log.e("", "Going for remove:7 " );
                                    removeDataforSpecificSttion();
                                    if (Constant.isFromQT) {
                                        call_delay_again();
                                    }
                                }

                            }else{
                                if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                                    for (int i = 0; i < ListElementsArrayList.size(); i++) {
                                        if (ListElementsArrayList.get(i).getFirebasekey().equals(dataSnapshot.getKey())) {
                                            //below if condition is update only when display endtransaction na hoy
//                                jo  bane fetures ma update fun joye to niche ni if condition remove karvi
//                                if(!customerOrderModelchanged.isDisplayItemOnEndTransaction()){
                                            updateChangesInListview(i, customerOrderModelchanged);
//                                }
                                            break;
                                        }
                                    }
                                }
                            }

                        }
                        // end new
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                        Log.e("firebase:",  "removed");

                        String lastRemovedchildFirebasekey = dataSnapshot.getKey();
                        Constant.isComeFromRemove = true;
                        Constant.lastRemovedchildFirebasekey = dataSnapshot.getKey();
                        //Log.d("lastremovedkey:", "" + lastRemovedchildFirebasekey);
//                CustomerOrderModel customerOrderModelVal = new CustomerOrderModel();
//                customerOrderModelVal = dataSnapshot.getValue(CustomerOrderModel.class);
                        CustomerOrderModel customerOrderModelVal = new CustomerOrderModel();
                        if (dataSnapshot.child("custid").getValue() != null) {
                            customerOrderModelVal.setCustid(String.valueOf(dataSnapshot.child("custid").getValue()));
                        }
                        if (dataSnapshot.child("description").getValue() != null) {
                            customerOrderModelVal.setDescription(String.valueOf(dataSnapshot.child("description").getValue()));
                        }
                        if (dataSnapshot.child("IsEndTransaction").getValue() != null) {
                            customerOrderModelVal.setEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsEndTransaction").getValue())));
                        }
                        if (dataSnapshot.child("price").getValue() != null) {
                            customerOrderModelVal.setPrice(String.valueOf(dataSnapshot.child("price").getValue()));
                        }
                        if (dataSnapshot.child("qty").getValue() != null) {
                            customerOrderModelVal.setQty(String.valueOf(dataSnapshot.child("qty").getValue()));
                        }
                        if (dataSnapshot.child("size").getValue() != null) {
                            customerOrderModelVal.setSize(String.valueOf(dataSnapshot.child("size").getValue()));
                        }
                        if (dataSnapshot.child("sizeColumnFlag").getValue() != null) {
                            customerOrderModelVal.setSizeColumnFlag(String.valueOf(dataSnapshot.child("sizeColumnFlag").getValue()));
                        }
                        if (dataSnapshot.child("station").getValue() != null) {
                            customerOrderModelVal.setStation(String.valueOf(dataSnapshot.child("station").getValue()));
                        }
                        if (dataSnapshot.child("store").getValue() != null) {
                            customerOrderModelVal.setStore(String.valueOf(dataSnapshot.child("store").getValue()));
                        }
                        if (dataSnapshot.child("subTotal").getValue() != null) {
                            customerOrderModelVal.setSubTotal(String.valueOf(dataSnapshot.child("subTotal").getValue()));
                        }
                        if (dataSnapshot.child("tax").getValue() != null) {
                            customerOrderModelVal.setTax(String.valueOf(dataSnapshot.child("tax").getValue()));
                        }
                        if (dataSnapshot.child("tax3").getValue() != null) {
                            customerOrderModelVal.setTax3(String.valueOf(dataSnapshot.child("tax3").getValue()));
                        }
                        if (dataSnapshot.child("IsConvFee").getValue() != null) {
                            customerOrderModelVal.setIsConvFee(String.valueOf(dataSnapshot.child("IsConvFee").getValue()));
                        }
                        if (dataSnapshot.child("ConvFeeName").getValue() != null) {
                            customerOrderModelVal.setConvFeeName(String.valueOf(dataSnapshot.child("ConvFeeName").getValue()));
                        }
                        if (dataSnapshot.child("BottleDipositAmt").getValue() != null) {
                            customerOrderModelVal.setBottleDipositAmt(String.valueOf(dataSnapshot.child("BottleDipositAmt").getValue()));
                        }
                        if (dataSnapshot.child("total").getValue() != null) {
                            customerOrderModelVal.setTotal(String.valueOf(dataSnapshot.child("total").getValue()));
                        }
                        if (dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue() != null) {
                            customerOrderModelVal.setDisplayItemOnEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue())));
                        }
                        if (dataSnapshot.child("IsTenderScreen").getValue() != null) {
                            customerOrderModelVal.setTenderScreen(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsTenderScreen").getValue())));
                        }
                        if (customerOrderModelVal != null) {
                            customerOrderModelVal.setFirebasekey(dataSnapshot.getKey());
                        }
                        if (Constant.lastRemovedchildkey != null && !Constant.lastRemovedchildkey.isEmpty()) {
                            if (Constant.lastRemovedchildkey.equals(lastRemovedchildFirebasekey)) { // means removed all data
                                if (llBillingLayout.getVisibility() == View.VISIBLE) {
                                    llposOrderMainLayout.setVisibility(View.GONE);
                                    llOrderfooter.setVisibility(View.GONE);
                                    llOrderTotal.setVisibility(View.GONE);
                                    llBillingLayout.setVisibility(View.GONE);
                                    if (ListElementsArrayList != null)
                                        ListElementsArrayList.clear();
                                    istenderedVal = false;
                                    if (Constant.descList != null)
                                        Constant.descList.clear();
                                    Constant.lastRemovedchildkey = "";
                                    lladv.setVisibility(View.VISIBLE);
                                    if (!Constant.isFromQT) {
                                        mViewPager.startAutoScroll();
                                    }
//                                    mViewPager.startAutoScroll();
                                } else if (customerOrderModelVal.isDisplayItemOnEndTransaction()) {
                                    if (ListElementsArrayList != null)
                                        ListElementsArrayList.clear();
                                    istenderedVal = false;
                                    if (Constant.descList != null)
                                        Constant.descList.clear();
                                    Constant.lastRemovedchildkey = "";
                                    //added by kaveri
//                            mViewPager.startAutoScroll();
                                }
                            }
                        }
                        //new code start
//                        if (customerOrderModelVal.getStation() != null && customerOrderModelVal.getStation().equalsIgnoreCase(StationNoPref) && customerOrderModelVal.getStore() != null && customerOrderModelVal.getStore().equalsIgnoreCase(StoreNoPref)) {
//                            if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
//                                for (int i = 0; i < ListElementsArrayList.size(); i++) {
//                                    if (ListElementsArrayList.get(i).getFirebasekey().equals(dataSnapshot.getKey())) {
//                                        //below if condition is update only when display endtransaction na hoy
////                                jo  bane fetures ma update fun joye to niche ni if condition remove karvi
////                                if(!customerOrderModelchanged.isDisplayItemOnEndTransaction()){
//                                        removeChildRowFromListview(i);
////                                }
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                        //new code end

                        //start working code
                        else if (customerOrderModelVal != null && customerOrderModelVal.getStation() != null && customerOrderModelVal.getStation().equalsIgnoreCase(StationNoPref)
                                && customerOrderModelVal.getStore() != null && customerOrderModelVal.getStore().equalsIgnoreCase(StoreNoPref)) {
                            if (customerOrderModelVal.isEndTransaction()) {
//                        removeDataforSpecificSttion(Constant.customerOrderList);
                                Log.e("", "Going for remove:1 " );
                                removeDataforSpecificSttion();
                                if (Constant.isFromQT) {
                                    call_delay_again();
                                }
                            } else {
                                if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                                    for (int i = 0; i < ListElementsArrayList.size(); i++) {
                                        if (ListElementsArrayList.get(i).getFirebasekey().equals(dataSnapshot.getKey())) {
                                            //Log.d(TAG, "equal key:" + ListElementsArrayList.get(i).getFirebasekey());
                                            Log.e("", "Going for remove:2 " );
                                            removeChildRowFromListview(i);
                                            break;
                                        }
                                    }
                                } else {
                                    //do here in other project
                                }
                            }
                        }
                        //end remove working code
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // for previously working code uncomment below line and uncomment above valueEventListener
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                //Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
//                // A new comment has been added, add it to the displayed list
////                cu comment = dataSnapshot.getValue(Comment.class);
////                CustomerOrderModel customerOrderModel = dataSnapshot.getValue(CustomerOrderModel.class);
//                //set customer model value  ***********
//                CustomerOrderModel customerOrderModel = new CustomerOrderModel();
//                if (dataSnapshot.child("custid").getValue() != null) {
//                    customerOrderModel.setCustid(String.valueOf(dataSnapshot.child("custid").getValue()));
//                }
//                if (dataSnapshot.child("description").getValue() != null) {
//                    customerOrderModel.setDescription(String.valueOf(dataSnapshot.child("description").getValue()));
//                }
//                if (dataSnapshot.child("IsEndTransaction").getValue() != null) {
//                    customerOrderModel.setEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsEndTransaction").getValue())));
//                }
//                if (dataSnapshot.child("price").getValue() != null) {
//                    customerOrderModel.setPrice(String.valueOf(dataSnapshot.child("price").getValue()));
//                }
//                if (dataSnapshot.child("qty").getValue() != null) {
//                    customerOrderModel.setQty(String.valueOf(dataSnapshot.child("qty").getValue()));
//                }
//                if (dataSnapshot.child("size").getValue() != null) {
//                    customerOrderModel.setSize(String.valueOf(dataSnapshot.child("size").getValue()));
//                }
//                if (dataSnapshot.child("sizeColumnFlag").getValue() != null) {
//                    customerOrderModel.setSizeColumnFlag(String.valueOf(dataSnapshot.child("sizeColumnFlag").getValue()));
//                }
//                if (dataSnapshot.child("station").getValue() != null) {
//                    customerOrderModel.setStation(String.valueOf(dataSnapshot.child("station").getValue()));
//                }
//                if (dataSnapshot.child("store").getValue() != null) {
//                    customerOrderModel.setStore(String.valueOf(dataSnapshot.child("store").getValue()));
//                }
//                if (dataSnapshot.child("subTotal").getValue() != null) {
//                    customerOrderModel.setSubTotal(String.valueOf(dataSnapshot.child("subTotal").getValue()));
//                }
//                if (dataSnapshot.child("tax").getValue() != null) {
//                    customerOrderModel.setTax(String.valueOf(dataSnapshot.child("tax").getValue()));
//                }
//                if (dataSnapshot.child("BottleDipositAmt").getValue() != null) {
//                    customerOrderModel.setBottleDipositAmt(String.valueOf(dataSnapshot.child("BottleDipositAmt").getValue()));
//                }
//                if (dataSnapshot.child("total").getValue() != null) {
//                    customerOrderModel.setTotal(String.valueOf(dataSnapshot.child("total").getValue()));
//                }
//                if (dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue() != null) {
//                    customerOrderModel.setDisplayItemOnEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue())));
//                }
//                if (dataSnapshot.child("IsTenderScreen").getValue() != null) {
//                    customerOrderModel.setTenderScreen(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsTenderScreen").getValue())));
//                }
//                if (customerOrderModel != null) {
//                    customerOrderModel.setFirebasekey(dataSnapshot.getKey());
//                }
//                if (customerOrderModel != null) {
//                    if (customerOrderModel.getStation() != null && customerOrderModel.getStation().equalsIgnoreCase(StationNoPref)
//                            && customerOrderModel.getStore() != null && customerOrderModel.getStore().equalsIgnoreCase(StoreNoPref)) {
//                        Constant.customerOrderList.add(customerOrderModel);
//                        firebaseKey = dataSnapshot.getKey();
//                    }
//                }
//                if (Constant.customerOrderList != null && Constant.customerOrderList.size() > 0) {
//                    if (customerOrderModel != null && customerOrderModel.getStation() != null && customerOrderModel.getStation().equalsIgnoreCase(StationNoPref)
//                            && customerOrderModel.getStore() != null && customerOrderModel.getStore().equalsIgnoreCase(StoreNoPref)) {
//                        CustomerOrderModel lastOrderModel = Constant.customerOrderList.get(Constant.customerOrderList.size() - 1);
//                        String lastItemDesc = lastOrderModel.getDescription();
//                        //this is for displayendtransction (true/false) new chnages start -------
////                        if (lastOrderModel.isDisplayItemOnEndTransaction()
////                                && !lastOrderModel.getPrice().trim().isEmpty()
////                                && !lastOrderModel.getQty().equals("0")) {
////
////                            displaydetails(lastOrderModel);
////
////                        } else if(!lastOrderModel.isDisplayItemOnEndTransaction() && !lastOrderModel.getPrice().trim().isEmpty()
////                                && !lastOrderModel.getQty().equals("0")){
////                            //this is for one by one transaction
////                            displayPreviousWorkingdetails(lastOrderModel, lastItemDesc);
////
////                        }else{
////                            if (llBillingLayout != null && llBillingLayout.getVisibility() == View.VISIBLE) {
////                                llBillingLayout.setVisibility(View.GONE);
////                                llposOrderMainLayout.setVisibility(View.GONE);
////                                llOrderfooter.setVisibility(View.GONE);
////                                llOrderTotal.setVisibility(View.GONE);
////                                lladv.setVisibility(View.VISIBLE);
////                                if(mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0)
////                                    mViewPager.startAutoScroll();
////                            }
////                        }
//                        //end *******************
//                        //this is for displayendtransction old working code start-----
//                        if (lastOrderModel.isDisplayItemOnEndTransaction()) {
//                            displaydetails(lastOrderModel);
//
//                        } else {
//                            //this is for one by one transaction
//                            displayPreviousWorkingdetails(lastOrderModel, lastItemDesc);
//                        }
//                        //end -----
//                    } else {
//                        if (Constant.customerOrderList == null || Constant.customerOrderList.size() == 0) {
//                            lladv.setVisibility(View.VISIBLE);
//                            //mViewPager.startAutoScroll();
//                            //Log.d("Image", "IN SCROLL2");
//                            mViewPager.startAutoScroll();
//                            llBillingLayout.setVisibility(View.GONE);
//                            llposOrderMainLayout.setVisibility(View.GONE);
//                            llOrderfooter.setVisibility(View.GONE);
//                            llOrderTotal.setVisibility(View.GONE);
////                            llWelcomeScreen.setVisibility(View.VISIBLE);
////                            llWelcomeFooter.setVisibility(View.VISIBLE);
//                            if (ListElementsArrayList != null)
//                                ListElementsArrayList.clear();
//                            if (Constant.customerOrderList != null)
//                                Constant.customerOrderList.clear();
//                            if (Constant.descList != null)
//                                Constant.descList.clear();
//                            istenderedVal = false;
////                            Constant.colorName = "";
//                           /* if (welcomedialog != null && !welcomedialog.isShowing()) {
//                                WelcomeAdsDialog();
//                            }*/
//                        }
//                    }
//                } else {
//                    if (llBillingLayout.getVisibility() == View.VISIBLE) {
//                        lladv.setVisibility(View.VISIBLE);
//                        // mViewPager.startAutoScroll();
//                        //Log.d("Image", "IN SCROLL3");
//                        // mViewPager.startAutoScroll();
//                        llposOrderMainLayout.setVisibility(View.GONE);
//                        llOrderfooter.setVisibility(View.GONE);
//                        llOrderTotal.setVisibility(View.GONE);
//                        llBillingLayout.setVisibility(View.GONE);
////                        llWelcomeScreen.setVisibility(View.VISIBLE);
////                        llWelcomeFooter.setVisibility(View.VISIBLE);
//                        if (ListElementsArrayList != null)
//                            ListElementsArrayList.clear();
//                        if (Constant.customerOrderList != null)
//                            Constant.customerOrderList.clear();
//                        istenderedVal = false;
////                        Constant.colorName = "";
//                        if (Constant.descList != null)
//                            Constant.descList.clear();
//                        /*if (welcomedialog != null && !welcomedialog.isShowing()) {
//                            WelcomeAdsDialog();
//                        }*/
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                //Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
//                // A comment has changed, use the key to determine if we are displaying this
//                // comment and if so displayed the changed comment.
////                Comment newComment = dataSnapshot.getValue(Comment.class);
//                String commentKey = dataSnapshot.getKey();
////                CustomerOrderModel customerOrderModelchanged = dataSnapshot.getValue(CustomerOrderModel.class);
//                CustomerOrderModel customerOrderModelchanged = new CustomerOrderModel();
//                if (dataSnapshot.child("custid").getValue() != null) {
//                    customerOrderModelchanged.setCustid(String.valueOf(dataSnapshot.child("custid").getValue()));
//                }
//                if (dataSnapshot.child("description").getValue() != null) {
//                    customerOrderModelchanged.setDescription(String.valueOf(dataSnapshot.child("description").getValue()));
//                }
//                if (dataSnapshot.child("IsEndTransaction").getValue() != null) {
//                    customerOrderModelchanged.setEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsEndTransaction").getValue())));
//                }
//                if (dataSnapshot.child("price").getValue() != null) {
//                    customerOrderModelchanged.setPrice(String.valueOf(dataSnapshot.child("price").getValue()));
//                }
//                if (dataSnapshot.child("qty").getValue() != null) {
//                    customerOrderModelchanged.setQty(String.valueOf(dataSnapshot.child("qty").getValue()));
//                }
//                if (dataSnapshot.child("size").getValue() != null) {
//                    customerOrderModelchanged.setSize(String.valueOf(dataSnapshot.child("size").getValue()));
//                }
//                if (dataSnapshot.child("sizeColumnFlag").getValue() != null) {
//                    customerOrderModelchanged.setSizeColumnFlag(String.valueOf(dataSnapshot.child("sizeColumnFlag").getValue()));
//                }
//                if (dataSnapshot.child("station").getValue() != null) {
//                    customerOrderModelchanged.setStation(String.valueOf(dataSnapshot.child("station").getValue()));
//                }
//                if (dataSnapshot.child("store").getValue() != null) {
//                    customerOrderModelchanged.setStore(String.valueOf(dataSnapshot.child("store").getValue()));
//                }
//                if (dataSnapshot.child("subTotal").getValue() != null) {
//                    customerOrderModelchanged.setSubTotal(String.valueOf(dataSnapshot.child("subTotal").getValue()));
//                }
//                if (dataSnapshot.child("tax").getValue() != null) {
//                    customerOrderModelchanged.setTax(String.valueOf(dataSnapshot.child("tax").getValue()));
//                }
//                if (dataSnapshot.child("BottleDipositAmt").getValue() != null) {
//                    customerOrderModelchanged.setBottleDipositAmt(String.valueOf(dataSnapshot.child("BottleDipositAmt").getValue()));
//                }
//                if (dataSnapshot.child("total").getValue() != null) {
//                    customerOrderModelchanged.setTotal(String.valueOf(dataSnapshot.child("total").getValue()));
//                }
//                if (dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue() != null) {
//                    customerOrderModelchanged.setDisplayItemOnEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue())));
//                }
//                if (dataSnapshot.child("IsTenderScreen").getValue() != null) {
//                    customerOrderModelchanged.setTenderScreen(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsTenderScreen").getValue())));
//                }
//                if (customerOrderModelchanged != null) {
//                    customerOrderModelchanged.setFirebasekey(dataSnapshot.getKey());
//                }
//                if (customerOrderModelchanged != null && customerOrderModelchanged.getStation() != null && customerOrderModelchanged.getStation().equalsIgnoreCase(StationNoPref)
//                        && customerOrderModelchanged.getStore() != null && customerOrderModelchanged.getStore().equalsIgnoreCase(StoreNoPref)) {
//                    if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
//                        for (int i = 0; i < ListElementsArrayList.size(); i++) {
//                            if (ListElementsArrayList.get(i).getFirebasekey().equals(dataSnapshot.getKey())) {
//                                //below if condition is update only when display endtransaction na hoy
////                                jo  bane fetures ma update fun joye to niche ni if condition remove karvi
////                                if(!customerOrderModelchanged.isDisplayItemOnEndTransaction()){
//                                updateChangesInListview(i, customerOrderModelchanged);
////                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//                // ...
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                //Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//                // A comment has changed, use the key to determine if we are displaying this
//                // comment and if so remove it.
//                String lastRemovedchildFirebasekey = dataSnapshot.getKey();
//                Constant.isComeFromRemove = true;
//                Constant.lastRemovedchildFirebasekey = dataSnapshot.getKey();
//                //Log.d("lastremovedkey:", "" + lastRemovedchildFirebasekey);
////                CustomerOrderModel customerOrderModelVal = new CustomerOrderModel();
////                customerOrderModelVal = dataSnapshot.getValue(CustomerOrderModel.class);
//                CustomerOrderModel customerOrderModelVal = new CustomerOrderModel();
//                if (dataSnapshot.child("custid").getValue() != null) {
//                    customerOrderModelVal.setCustid(String.valueOf(dataSnapshot.child("custid").getValue()));
//                }
//                if (dataSnapshot.child("description").getValue() != null) {
//                    customerOrderModelVal.setDescription(String.valueOf(dataSnapshot.child("description").getValue()));
//                }
//                if (dataSnapshot.child("IsEndTransaction").getValue() != null) {
//                    customerOrderModelVal.setEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsEndTransaction").getValue())));
//                }
//                if (dataSnapshot.child("price").getValue() != null) {
//                    customerOrderModelVal.setPrice(String.valueOf(dataSnapshot.child("price").getValue()));
//                }
//                if (dataSnapshot.child("qty").getValue() != null) {
//                    customerOrderModelVal.setQty(String.valueOf(dataSnapshot.child("qty").getValue()));
//                }
//                if (dataSnapshot.child("size").getValue() != null) {
//                    customerOrderModelVal.setSize(String.valueOf(dataSnapshot.child("size").getValue()));
//                }
//                if (dataSnapshot.child("sizeColumnFlag").getValue() != null) {
//                    customerOrderModelVal.setSizeColumnFlag(String.valueOf(dataSnapshot.child("sizeColumnFlag").getValue()));
//                }
//                if (dataSnapshot.child("station").getValue() != null) {
//                    customerOrderModelVal.setStation(String.valueOf(dataSnapshot.child("station").getValue()));
//                }
//                if (dataSnapshot.child("store").getValue() != null) {
//                    customerOrderModelVal.setStore(String.valueOf(dataSnapshot.child("store").getValue()));
//                }
//                if (dataSnapshot.child("subTotal").getValue() != null) {
//                    customerOrderModelVal.setSubTotal(String.valueOf(dataSnapshot.child("subTotal").getValue()));
//                }
//                if (dataSnapshot.child("tax").getValue() != null) {
//                    customerOrderModelVal.setTax(String.valueOf(dataSnapshot.child("tax").getValue()));
//                }
//                if (dataSnapshot.child("BottleDipositAmt").getValue() != null) {
//                    customerOrderModelVal.setBottleDipositAmt(String.valueOf(dataSnapshot.child("BottleDipositAmt").getValue()));
//                }
//                if (dataSnapshot.child("total").getValue() != null) {
//                    customerOrderModelVal.setTotal(String.valueOf(dataSnapshot.child("total").getValue()));
//                }
//                if (dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue() != null) {
//                    customerOrderModelVal.setDisplayItemOnEndTransaction(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsDisplayItemOnEndTransaction").getValue())));
//                }
//                if (dataSnapshot.child("IsTenderScreen").getValue() != null) {
//                    customerOrderModelVal.setTenderScreen(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("IsTenderScreen").getValue())));
//                }
//                if (customerOrderModelVal != null) {
//                    customerOrderModelVal.setFirebasekey(dataSnapshot.getKey());
//                }
//                if (Constant.lastRemovedchildkey != null && !Constant.lastRemovedchildkey.isEmpty()) {
//                    if (Constant.lastRemovedchildkey.equals(lastRemovedchildFirebasekey)) { // means removed all data
//                        if (llBillingLayout.getVisibility() == View.VISIBLE) {
//                            llposOrderMainLayout.setVisibility(View.GONE);
//                            llOrderfooter.setVisibility(View.GONE);
//                            llOrderTotal.setVisibility(View.GONE);
//                            llBillingLayout.setVisibility(View.GONE);
//                            if (ListElementsArrayList != null)
//                                ListElementsArrayList.clear();
//                            istenderedVal = false;
//                            if (Constant.descList != null)
//                                Constant.descList.clear();
//                            Constant.lastRemovedchildkey = "";
//                            lladv.setVisibility(View.VISIBLE);
//                            mViewPager.startAutoScroll();
//                        } else if (customerOrderModelVal.isDisplayItemOnEndTransaction()) {
//                            if (ListElementsArrayList != null)
//                                ListElementsArrayList.clear();
//                            istenderedVal = false;
//                            if (Constant.descList != null)
//                                Constant.descList.clear();
//                            Constant.lastRemovedchildkey = "";
//                            //added by kaveri
////                            mViewPager.startAutoScroll();
//                        }
//                    }
//                } else if (customerOrderModelVal != null && customerOrderModelVal.getStation() != null && customerOrderModelVal.getStation().equalsIgnoreCase(StationNoPref)
//                        && customerOrderModelVal.getStore() != null && customerOrderModelVal.getStore().equalsIgnoreCase(StoreNoPref)) {
//                    if (customerOrderModelVal.isEndTransaction()) {
////                        removeDataforSpecificSttion(Constant.customerOrderList);
//                        removeDataforSpecificSttion();
//                    } else {
//                        if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
//                            for (int i = 0; i < ListElementsArrayList.size(); i++) {
//                                if (ListElementsArrayList.get(i).getFirebasekey().equals(dataSnapshot.getKey())) {
//                                    //Log.d(TAG, "equal key:" + ListElementsArrayList.get(i).getFirebasekey());
//                                    removeChildRowFromListview(i);
//                                    break;
//                                }
//                            }
//                        } else {
//                            //do here in other project
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                //Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
//                // A comment has changed position, use the key to determine if we are
//                // displaying this comment and if so move it.
////                Comment movedComment = dataSnapshot.getValue(Comment.class);
//                String commentKey = dataSnapshot.getKey();
//                // ...
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
//
//            }
//        };
//        mFirebaseDatabase.addChildEventListener(childEventListener);
        //end ***************
    }

    private void displaydetails(CustomerOrderModel lastOrderModel) {
//        if(Constant.itemListToshowAfter != null && Constant.itemListToshowAfter.size() > 0) {
        if (Constant.customerOrderList != null && Constant.customerOrderList.size() > 0) {
//            for (int i = 0; i < Constant.itemListToshowAfter.size(); i++) {
//                CustomerOrderModel lastOrderModel = Constant.itemListToshowAfter.get(i);
            String lastItemDesc = lastOrderModel.getDescription();
            if (!lastOrderModel.getQty().equals("0") && !lastOrderModel.getQty().equals("+1")
                    && !istenderedVal) {
//                        && !lastOrderModel.getDescription().equalsIgnoreCase("Total $ ")) {
                handler.removeCallbacks(delayedRunnable);
                savefirebaseData(lastOrderModel, firebaseKey);
                if (lastOrderModel.IsTenderScreen && ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                    if (lladv != null && lladv.getVisibility() == View.VISIBLE) {
                        //lladv.setVisibility(View.GONE);
                        mViewPager.stopAutoScroll();
                    }
                    if (lladv != null) {
                        lladv.setVisibility(View.GONE);
                    }
                    llBillingLayout.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Visible : 1" );
                    llposOrderMainLayout.setVisibility(View.VISIBLE);
                    llOrderfooter.setVisibility(View.VISIBLE);
                    llOrderTotal.setVisibility(View.VISIBLE);
                    if (lastItemDesc.startsWith("Tendered")) {
                        istenderedVal = true;
                        //changes for counter start
                        Constant.TransactionCounterForImage = Constant.TransactionCounterForImage + 1;
                        if (Constant.TransactionCounterForImage == Constant.CountForRefresh) {
                            //Log.d("counter", "counter:");
//                                    Constant.TransactionCounterForImage = 0;
                            //commented for version 1.6 refresh images
                            Constant.TransactionCounterForImage = 0;
                            Constant.isTimeToRefreshImg = true;
//                            if (Constant.isFromBlipBoard) {
//                                mViewPager.setVisibility(View.VISIBLE);
//                                mViewPager.startAutoScroll();
//                            } else {
//                            }
                            if (!Constant.isFromQT){
                                callImagesWs();
//                                Log.e(TAG, "displaydetails: QT3" );
//                                Checking if the store time is closed or not
//                                if (isCallingAllowed()) {
//                                    callQT_Token();
//                                }else{
//                                    Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
//                                    callcomputerperfectimage();
//                                }
//                                END
                            }
//                            else{
//
//                            }
                        }

                        if (Constant.isFromQT){
                                Log.e(TAG, "displaydetails: QT3" );
//                                Checking if the store time is closed or not
                                if (isCallingAllowed()) {
                                    callQT_Token();
                                }else{
                                    Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                                    callcomputerperfectimage();
                                }
//                                END
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (llBillingLayout.getVisibility() == View.VISIBLE) {
                                            lladv.setVisibility(View.VISIBLE);
                                            if (!Constant.isFromQT) {
                                                mViewPager.startAutoScroll();
                                            }
                                            // mViewPager.startAutoScroll();
                                            //Log.d("Image", "IN SCROLL3");
                                            // mViewPager.startAutoScroll();
                                            llposOrderMainLayout.setVisibility(View.GONE);
                                            llOrderfooter.setVisibility(View.GONE);
                                            llOrderTotal.setVisibility(View.GONE);
                                            llBillingLayout.setVisibility(View.GONE);
//                        llWelcomeScreen.setVisibility(View.VISIBLE);
//                        llWelcomeFooter.setVisibility(View.VISIBLE);
                                            if (ListElementsArrayList != null)
                                                ListElementsArrayList.clear();
                                            if (Constant.customerOrderList != null)
                                                Constant.customerOrderList.clear();
                                            istenderedVal = false;
//                        Constant.colorName = "";
                                            if (Constant.descList != null)
                                                Constant.descList.clear();
                        /*if (welcomedialog != null && !welcomedialog.isShowing()) {
                            WelcomeAdsDialog();
                        }*/
                                        }
                                    }
                                });
                            }
//                        }, 1000);
                        }, 1000);
                        //end ********
                    } else {
                        istenderedVal = false;
                    }

                } else {
                }

            } else {
                if (!lastOrderModel.isTenderScreen()) {
                    if (lastOrderModel.getQty().equals("0")) {
                        Log.d(TAG, "testing");
                        if (lastOrderModel.isEndTransaction()) {
                            Log.d(TAG, "removeagain:" + "");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                                    removeDataforSpecificSttion(Constant.itemListToshowAfter);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e("", "Going for remove:4 " );
                                            removeDataforSpecificSttion();
                                            if (Constant.isFromQT) {
                                                call_delay_again();
                                            }
                                        }
                                    });

                                }
                            }, 1000);
                        } else {
                            if (llBillingLayout != null &&
                                    llBillingLayout.getVisibility() == View.VISIBLE
                            ) {
                                llBillingLayout.setVisibility(View.GONE);
                                llposOrderMainLayout.setVisibility(View.GONE);
                                llOrderfooter.setVisibility(View.GONE);
                                llOrderTotal.setVisibility(View.GONE);
                                lladv.setVisibility(View.VISIBLE);
                                if (!Constant.isFromQT) {
                                    if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0)
                                        mViewPager.startAutoScroll();
                                }
                            }
                        }

                    }
                } else {
                    if (llBillingLayout != null &&
                            llBillingLayout.getVisibility() == View.VISIBLE
                    ) {
                        llBillingLayout.setVisibility(View.GONE);
                        llposOrderMainLayout.setVisibility(View.GONE);
                        llOrderfooter.setVisibility(View.GONE);
                        llOrderTotal.setVisibility(View.GONE);
                        lladv.setVisibility(View.VISIBLE);
                        if (!Constant.isFromQT) {
                            mViewPager.startAutoScroll();
                        }
                    }
                }

            }

        }

    }

    private void displayPreviousWorkingdetails(CustomerOrderModel lastOrderModel, String lastItemDesc) {

        Log.e(TAG, "123456 displayPreviousWorkingdetails: "+lastOrderModel.getFirebasekey());
        Log.e(TAG, "12345 displayPreviousWorkingdetails: "+lastOrderModel.getDescription());

        if (!lastOrderModel.getQty().equals("0") && !lastOrderModel.getQty().equals("+1") && !istenderedVal
                && !lastOrderModel.getDescription().equalsIgnoreCase("Total $ ")) {

            handler.removeCallbacks(delayedRunnable);

            savefirebaseData(lastOrderModel, firebaseKey);
//                            llWelcomeViewpagerLayout.setVisibility(View.GONE);
                           /* try {
                                if (welcomedialog != null && welcomedialog.isShowing()) {
                                    welcomedialog.dismiss();
                                }

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }*/
            //lladv.setVisibility(View.GONE);
            //mViewPager.stopAutoScroll();
            //Log.d("Image", "IN SCROLL STOP");
            mViewPager.stopAutoScroll();
            if (lladv != null) {
                lladv.setVisibility(View.GONE);
            }
            llBillingLayout.setVisibility(View.VISIBLE);
            Log.e(TAG, "Visible : 2" );
            llposOrderMainLayout.setVisibility(View.VISIBLE);
            llOrderfooter.setVisibility(View.VISIBLE);
            llOrderTotal.setVisibility(View.VISIBLE);
            //add below toady
            if (lastItemDesc.startsWith("Tendered")) {
                istenderedVal = true;
                //changes for counter start
                Constant.TransactionCounterForImage = Constant.TransactionCounterForImage + 1;
                //Log.d("end_transaction", "end_transaction:");
                                /*if (Constant.TransactionCounterForImage == Constant.TransactionCountForRefresh-1) {
                                    String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_IMAGES_DETAIL + Constant.STOREID;
                                    TaskImagesAdvance taskImagesAdvance = new TaskImagesAdvance(MainActivity.this);
                                    taskImagesAdvance.execute(Urlban);
                                }*/
//                Edited by Varun because of tom email
//                For reload the API when 20 transaction is complted
                if (!Constant.STOREID.equals("707")&&!Constant.STOREID.equals("7365")){
                    Constant.CountForRefresh=20;
                }
                if (Constant.TransactionCounterForImage == Constant.CountForRefresh) {
                    //Log.d("counter", "counter:");
//                                    Constant.TransactionCounterForImage = 0;
                    //commented for version 1.6 refresh images
                    Constant.TransactionCounterForImage = 0;
                    Constant.isTimeToRefreshImg = true;
                    if (!Constant.isFromQT){
                        callImagesWs();
//                        Log.e(TAG, "displayPreviousWorkingdetails: QT4" );
////                        Checking if the store time is closed or not
//                        if (isCallingAllowed()) {
//                            callQT_Token();
//                        }else{
//                            Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
//                            callcomputerperfectimage();
//                        }
////                          END
                    }
//                    else{
//
//                    }
//                    if (Constant.isFromBlipBoard) {
//                        mViewPager.setVisibility(View.VISIBLE);
//                        mViewPager.startAutoScroll();
//                    }
                }

                if (Constant.isFromQT){
                    Log.e(TAG, "displayPreviousWorkingdetails: QT4" );
//                        Checking if the store time is closed or not
                    if (isCallingAllowed()) {
                        callQT_Token();
                    }else{
                        Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                        callcomputerperfectimage();
                    }
//                          END
                }
                new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (llBillingLayout.getVisibility() == View.VISIBLE) {
                                        lladv.setVisibility(View.VISIBLE);
                                        if (!Constant.isFromQT) {
                                            mViewPager.startAutoScroll();
                                        }
                                        // mViewPager.startAutoScroll();
                                        //Log.d("Image", "IN SCROLL3");
                                        // mViewPager.startAutoScroll();
                                        llposOrderMainLayout.setVisibility(View.GONE);
                                        llOrderfooter.setVisibility(View.GONE);
                                        llOrderTotal.setVisibility(View.GONE);
                                        llBillingLayout.setVisibility(View.GONE);
//                        llWelcomeScreen.setVisibility(View.VISIBLE);
//                        llWelcomeFooter.setVisibility(View.VISIBLE);
                                        if (ListElementsArrayList != null)
                                            ListElementsArrayList.clear();
                                        if (Constant.customerOrderList != null)
                                            Constant.customerOrderList.clear();
                                        istenderedVal = false;
//                        Constant.colorName = "";
                                        if (Constant.descList != null)
                                            Constant.descList.clear();
                        /*if (welcomedialog != null && !welcomedialog.isShowing()) {
                            WelcomeAdsDialog();
                        }*/
                                    }
                                }
                            });
                        }
//                    }, 1000);
                    }, 1000);

                //end ********
            } else {
                istenderedVal = false;
            }

        }

        else if (!lastOrderModel.getQty().equals("0") && !lastOrderModel.getQty().equals("+1") && !istenderedVal
                && lastOrderModel.getIsConvFee() != null && lastOrderModel.getIsConvFee().equalsIgnoreCase("True")
                && !lastOrderModel.getSubTotal().equals("0.00")) {

            savefirebaseData(lastOrderModel, firebaseKey);

            mViewPager.stopAutoScroll();
            if (lladv != null) {
                lladv.setVisibility(View.GONE);
            }
            llBillingLayout.setVisibility(View.VISIBLE);
            Log.e(TAG, "Visible : 3" );
            llposOrderMainLayout.setVisibility(View.VISIBLE);
            llOrderfooter.setVisibility(View.VISIBLE);
            llOrderTotal.setVisibility(View.VISIBLE);
            //add below toady

            if (lastItemDesc.startsWith("Tendered")) {
                istenderedVal = true;
                //changes for counter start
                Constant.TransactionCounterForImage = Constant.TransactionCounterForImage + 1;

                if (Constant.TransactionCounterForImage == Constant.CountForRefresh) {

                    Constant.TransactionCounterForImage = 0;
                    Constant.isTimeToRefreshImg = true;
                    if (!Constant.isFromQT){
                        callImagesWs();
//                        Log.e(TAG, "displayPreviousWorkingdetails: QT5");
////                          Checking if the store time is closed or not
//                        if (isCallingAllowed()) {
//                            callQT_Token();
//                        }else{
//                            Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
//                            callcomputerperfectimage();
//                        }
//                        END
                    }
//                    else{
//
//                    }
//                    if (Constant.isFromBlipBoard) {
//                        mViewPager.setVisibility(View.VISIBLE);
//                        mViewPager.startAutoScroll();
//                    }
                }

                if (Constant.isFromQT){
                    Log.e(TAG, "displayPreviousWorkingdetails: QT5");
//                          Checking if the store time is closed or not
                    if (isCallingAllowed()) {
                        callQT_Token();
                    }else{
                        Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                        callcomputerperfectimage();
                    }
//                        END
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (llBillingLayout.getVisibility() == View.VISIBLE) {
                                    lladv.setVisibility(View.VISIBLE);
                                    if (!Constant.isFromQT) {
                                        mViewPager.startAutoScroll();
                                    }

                                    llposOrderMainLayout.setVisibility(View.GONE);
                                    llOrderfooter.setVisibility(View.GONE);
                                    llOrderTotal.setVisibility(View.GONE);
                                    llBillingLayout.setVisibility(View.GONE);

                                    if (ListElementsArrayList != null)
                                        ListElementsArrayList.clear();
                                    if (Constant.customerOrderList != null)
                                        Constant.customerOrderList.clear();
                                    istenderedVal = false;

                                    if (Constant.descList != null)
                                        Constant.descList.clear();
                                }
                            }
                        });
                    }
//                    }, 1000);
                }, 1000);

                //end ********
            } else {
                istenderedVal = false;
            }

        }

        else if(lastOrderModel.getDescription().equalsIgnoreCase("Back to RingSale")
                && !lastOrderModel.isDisplayItemOnEndTransaction()){

            totalValue.setText("$" + lastOrderModel.getTotal());
            SubtotalValue.setText("$" + lastOrderModel.getSubTotal());
            taxValue.setText("$" + lastOrderModel.getTax());

            // conv fee value
            if(lastOrderModel.getTax3() != null && !lastOrderModel.getTax3().isEmpty()
                    && !lastOrderModel.getTax3().equalsIgnoreCase("0.00")){

                tax3Value.setVisibility(View.VISIBLE);
                tax3Value.setText("$" + lastOrderModel.getTax3());
                tax3.setVisibility(View.VISIBLE);

                //Conv fee name
                if(lastOrderModel.getConvFeeName() != null && lastOrderModel.getConvFeeName().isEmpty()){
//                tax3.setText("" + lastOrderModel.getConvFeeName());
                    tax3.setText("" + "Tax3:");
                }else{
                    tax3.setText("" + lastOrderModel.getConvFeeName() + ":");
                }
                //end

            }else {

                tax3.setVisibility(View.GONE);
                tax3Value.setVisibility(View.GONE);
            }
            //end

        } else if (lastOrderModel.getQty().equals("0")) {
            //Log.d(TAG, "testing");
            if (lastOrderModel.isEndTransaction()) { //clear screen and rrrrrrrrrrrr
                //Log.d(TAG, "removeagain:" + "");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
                Log.e("", "Going for remove:5 " );
                                removeDataforSpecificSttion();
                                if (Constant.isFromQT) {
                                    call_delay_again();
                                }
//                            }
//                        });
//                    }
//                }, 1000); // change here
            }

        } else if (lastOrderModel.getQty().equals("+1")) {

            if(Constant.isComeFromRemove){
                if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                    for (int i = 0; i < ListElementsArrayList.size(); i++) {
                        if (ListElementsArrayList.get(i).getFirebasekey().equals(Constant.lastRemovedchildFirebasekey)) {
                            //Log.d(TAG, "equal key:" + ListElementsArrayList.get(i).getFirebasekey());
                            Log.e("", "Going for remove:3 " );
                            removeChildRowFromListview(i);
                            break;
                        }
                    }
                }
                Constant.isComeFromRemove = false;
            }

            totalValue.setText("$" + lastOrderModel.getTotal());
            SubtotalValue.setText("$" + lastOrderModel.getSubTotal());
            taxValue.setText("$" + lastOrderModel.getTax());

//             old Code

            /*if(lastOrderModel.getConvFeeName().isEmpty()){
                tax3.setText("" + lastOrderModel.getConvFeeName());
            }else{
                tax3.setText("" + lastOrderModel.getConvFeeName() + ":");
            }
//            tax3.setText("" + lastOrderModel.getConvFeeName());
            tax3Value.setText("$" + lastOrderModel.getTax3());*/

//             END

            //commented below code for reduce crashed issue

            if(lastOrderModel.getTax3() != null && !lastOrderModel.getTax3().isEmpty()
                    && !lastOrderModel.getTax3().equalsIgnoreCase("0.00")){

                tax3Value.setVisibility(View.VISIBLE);
                tax3Value.setText("$" + lastOrderModel.getTax3());
                tax3.setVisibility(View.VISIBLE);

                //Conv fee name
                if(lastOrderModel.getConvFeeName() != null && lastOrderModel.getConvFeeName().isEmpty()){
//                tax3.setText("" + lastOrderModel.getConvFeeName());
                    tax3.setText("" + "Tax3:");
                }else{
                    tax3.setText("" + lastOrderModel.getConvFeeName() + ":");
                }
                //end

            }else {

                tax3.setVisibility(View.GONE);
                tax3Value.setVisibility(View.GONE);
            }

            //            tax3Value.setText("$" + lastOrderModel.getTax3());
// end ******


//            if(lastOrderModel.getBottleDipositAmt() != null && !lastOrderModel.getBottleDipositAmt().isEmpty()
//                && Float.parseFloat(lastOrderModel.getBottleDipositAmt()) > 0.00) {
//                lldeposit.setVisibility(View.VISIBLE);
//                tvdepositValue.setText("$" + df.format(Float.parseFloat(lastOrderModel.getBottleDipositAmt())));
//            }else{
//                lldeposit.setVisibility(View.GONE);
//            }
        }

    }

    private void setHeader() {
        try {
            if (Constant.ImageHeaderList.get(0).getImage().contains("/")) {
                Headerimageview.setImageURI(Uri.parse(Constant.ImageHeaderList.get(0).getImage()));
                File file = new File(Uri.parse(Constant.ImageHeaderList.get(0).getImage()).getPath());
                if (file.length() <= 0) {
                    Headerimageview.setImageResource(R.drawable.noimagebanner);
                }
            } else {
                if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
                    Glide.with(this).load(Constant.ImageHeaderList.get(0).getImagepath().toString())
                            .placeholder(R.drawable.noimagebanner)
                            .dontAnimate()
                            .dontTransform()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(Headerimageview);
                }
            }

           /* if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
                Glide.with(this).load(imgUrlPath + Constant.ImageHeaderList.get(0).getStoreNo() + "/" + Constant.ImageHeaderList.get(0).getImage())
                        .placeholder(R.drawable.noimagebanner)
                        .dontAnimate()
                        .dontTransform()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(Headerimageview);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }
//        else {
//            llHeader.setBackgroundColor(getResources().getColor(R.color.store_104color));
//        }
    }

    private void setfooter() {
        try {
            if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
                if (Constant.ImageFooterList.get(0).getImage().contains("/")) {
                    llFooterimageview.setImageURI(Uri.parse(Constant.ImageFooterList.get(0).getImage()));
                    File file = new File(Uri.parse(Constant.ImageFooterList.get(0).getImage()).getPath());
                    if (file.length() <= 0) {
                        llFooterimageview.setImageResource(R.drawable.noimagebanner);
                    }
                } else {
//                    Glide.with(this).load(imgUrlPath + Constant.ImageFooterList.get(0).getStoreNo() + "/" + Constant.ImageFooterList.get(0).getImage())
                    Glide.with(this).load(Constant.ImageFooterList.get(0).getImagepath().toString())
                            .placeholder(R.drawable.noimagebanner)
                            .dontAnimate()
                            .dontTransform()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(llFooterimageview);
                }

            }

           /* if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
                Glide.with(this).load(imgUrlPath + Constant.ImageFooterList.get(0).getStoreNo() + "/" + Constant.ImageFooterList.get(0).getImage())
                        .placeholder(R.drawable.noimagebanner)
                        .dontAnimate()
                        .dontTransform()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(llFooterimageview);
            }*/
        } catch (Exception e) {
        }
    }

    private void updateChangesInListview(int pos, CustomerOrderModel customerOrderModelchanged) {
        if (customerOrderModelchanged != null) {
//            ListElementsArrayList.add(pos,customerOrderModelchanged);
            ListElementsArrayList.set(pos, customerOrderModelchanged);
            adapter.setData(ListElementsArrayList);
            Log.e("Update", " Update ");
            totalValue.setText("$" + customerOrderModelchanged.getTotal());
            SubtotalValue.setText("$" + customerOrderModelchanged.getSubTotal());
            taxValue.setText("$" + customerOrderModelchanged.getTax());
//            tax3.setText("" + customerOrderModelchanged.getConvFeeName());
            if(customerOrderModelchanged.getConvFeeName().isEmpty()){
                tax3.setText("" + customerOrderModelchanged.getConvFeeName());
            }else{
                tax3.setText("" + customerOrderModelchanged.getConvFeeName() + ":");
            }

            tax3Value.setText("$" + customerOrderModelchanged.getTax3());
            if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                listview.smoothScrollToPosition(ListElementsArrayList.size()-1);
            }
        }
    }

    private void removeChildRowFromListview(int pos) {
        //Log.d("removed single row", "removed single row");
        try {
            ListElementsArrayList.remove(pos);
            adapter.setData(ListElementsArrayList);
            Log.e("Remove", "Remove ");
            if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                listview.smoothScrollToPosition(ListElementsArrayList.size()-1);
            }
        } catch (Exception e) {
        }
        if (ListElementsArrayList.size() == 0) {
            if (llBillingLayout.getVisibility() == View.VISIBLE) {
                llposOrderMainLayout.setVisibility(View.GONE);
                llOrderfooter.setVisibility(View.GONE);
                llOrderTotal.setVisibility(View.GONE);
                llBillingLayout.setVisibility(View.GONE);
                lladv.setVisibility(View.VISIBLE);
                if (Constant.isFromQT){
                    call_delay_again();
                }else{
                    mViewPager.startAutoScroll();
                }
                //mViewPager.startAutoScroll();
                //Log.d("Image", "IN SCROLL5");
//                mViewPager.startAutoScroll();
//                llWelcomeScreen.setVisibility(View.VISIBLE);
//                llWelcomeFooter.setVisibility(View.VISIBLE);
                if (ListElementsArrayList != null)
                    ListElementsArrayList.clear();
                istenderedVal = false;
                if (Constant.descList != null)
                    Constant.descList.clear();
                Constant.lastRemovedchildkey = "";
                //WelcomeAdsDialog();
            }
        }
    }

    private void savefirebaseData(CustomerOrderModel customerOrderModel, String firebaseKey) {
        /*if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
            setHeader();
        }
        if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
            setfooter();
        }*/
        if (customerOrderModel.isDisplayItemOnEndTransaction()
                && customerOrderModel.isTenderScreen() && customerOrderModel.getDescription().equalsIgnoreCase("Total $ ")) {
            totalValue.setText("$" + customerOrderModel.getTotal());
            SubtotalValue.setText("$" + customerOrderModel.getSubTotal());
            taxValue.setText("$" + customerOrderModel.getTax());


//            if(customerOrderModel.getConvFeeName().isEmpty()){
//                tax3.setText("" + customerOrderModel.getConvFeeName());
//            }else{
//                tax3.setText("" + customerOrderModel.getConvFeeName() + ":");
//            }
////            tax3.setText("" + customerOrderModel.getConvFeeName());
//            tax3Value.setText("$" + customerOrderModel.getTax3());


            if(customerOrderModel.getTax3() != null && !customerOrderModel.getTax3().isEmpty()
                    && !customerOrderModel.getTax3().equalsIgnoreCase("0.00")){

                tax3Value.setVisibility(View.VISIBLE);
                tax3Value.setText("$" + customerOrderModel.getTax3());
                tax3.setVisibility(View.VISIBLE);

                //Conv fee name
                if(customerOrderModel.getConvFeeName() == null && customerOrderModel.getConvFeeName().isEmpty()){
//                tax3.setText("" + lastOrderModel.getConvFeeName());
                    tax3.setText("" + "Tax3:");
                }else{
                    tax3.setText("" + customerOrderModel.getConvFeeName() + ":");
                }
                //end

            }else {

                tax3.setVisibility(View.GONE);
                tax3Value.setVisibility(View.GONE);
            }

//            if(customerOrderModel.getBottleDipositAmt() != null && !customerOrderModel.getBottleDipositAmt().isEmpty()
//                    && Float.parseFloat(customerOrderModel.getBottleDipositAmt()) > 0.00) {
//                lldeposit.setVisibility(View.VISIBLE);
//                tvdepositValue.setText("$" + df.format(Float.parseFloat(customerOrderModel.getBottleDipositAmt())));
//            }else{
//                lldeposit.setVisibility(View.GONE);
//            }
        } else{
            //below if condition newaly added
            if(!customerOrderModel.getDescription().equalsIgnoreCase("Total $ ")) {
                ListElementsArrayList.add(customerOrderModel);
                if (ListElementsArrayList != null && ListElementsArrayList.size() > 0) {
                    LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(MainActivity.this);
                    adapter = new PosOrderAdapter(MainActivity.this,
                            ListElementsArrayList);
                    listview.setLayoutManager(mLayoutManager1);
                    listview.setNestedScrollingEnabled(false);
                    listview.setHasFixedSize(true);
                    listview.setItemViewCacheSize(20);
                    listview.setDrawingCacheEnabled(true);
                    listview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    listview.setAdapter(adapter);
                    Log.e("Save", "Save ");
                    try {
                        listview.smoothScrollToPosition(ListElementsArrayList.size() - 1);
                    } catch (Exception e) {

                    }
                    Constant.isLightcolor = true;
                    if (customerOrderModel.getSizeColumnFlag().equals("Y")) {
                        tvSize.setVisibility(View.VISIBLE);
                    } else {
                        tvSize.setVisibility(View.GONE);
//                        tvSize.setVisibility(View.INVISIBLE);
                    }
                }
            }
                totalValue.setText("$" + customerOrderModel.getTotal());
                SubtotalValue.setText("$" + customerOrderModel.getSubTotal());
                taxValue.setText("$" + customerOrderModel.getTax());
//                tax3.setText("" + customerOrderModel.getConvFeeName());

            //commented below code for reduce crashed issue

            if(customerOrderModel.getTax3() != null && !customerOrderModel.getTax3().isEmpty()
                    && !customerOrderModel.getTax3().equalsIgnoreCase("0.00")){

                tax3Value.setVisibility(View.VISIBLE);
                tax3Value.setText("$" + customerOrderModel.getTax3());
                tax3.setVisibility(View.VISIBLE);

                //Conv fee name
                if(customerOrderModel.getConvFeeName() == null && customerOrderModel.getConvFeeName().isEmpty()){
//                tax3.setText("" + lastOrderModel.getConvFeeName());
                    tax3.setText("" + "Tax3:");
                }else{
                    tax3.setText("" + customerOrderModel.getConvFeeName() + ":");
                }
                //end

            }else {

                tax3.setVisibility(View.GONE);
                tax3Value.setVisibility(View.GONE);
            }

//            tax3Value.setText("$" + customerOrderModel.getTax3());

            // end ******

//                if(customerOrderModel.getBottleDipositAmt() != null && !customerOrderModel.getBottleDipositAmt().isEmpty()
//                        && Float.parseFloat(customerOrderModel.getBottleDipositAmt()) > 0.00) {
//                    lldeposit.setVisibility(View.VISIBLE);
//                    tvdepositValue.setText("$" + df.format(Float.parseFloat(customerOrderModel.getBottleDipositAmt())));
//                }else{
//                    lldeposit.setVisibility(View.GONE);
//                }
        }
        //End transaction
        if (customerOrderModel.isEndTransaction()) {
//                        removeDataforSpecificSttion(Constant.customerOrderList);
//
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    removeDataforSpecificSttion(Constant.customerOrderList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("", "Going for remove:6 " );
                            removeDataforSpecificSttion();
                            if (Constant.isFromQT) {
                                call_delay_again();
                            }
                        }
                    });
                }
            }, 700);

        }

    }

    //    private void removeDataforSpecificSttion(List<CustomerOrderModel> customerOrderList) {
    private void removeDataforSpecificSttion() {
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Constant.tempitemlistforRemoveitems != null) {
                    Constant.tempitemlistforRemoveitems.clear();
                }
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    String key = postsnapshot.getKey();
                    //Log.d(TAG, key);
                    String station = postsnapshot.child("station").getValue(String.class);
                    String store = postsnapshot.child("store").getValue(String.class);
                    if (station != null && station.equals(StationNoPref) && store != null && store.equalsIgnoreCase(StoreNoPref)) {
                        Constant.tempitemlistforRemoveitems.add(postsnapshot.getKey());
                        String lastkey = Constant.tempitemlistforRemoveitems.get(Constant.tempitemlistforRemoveitems.size() - 1);
                        //Log.d("lastremovekeyFromList:", "" + lastkey);
                        Constant.lastRemovedchildkey = postsnapshot.getKey();
                        //Log.d("lastremovekey:", "" + postsnapshot.getKey());
                        postsnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
//        Toast.makeText(MainActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }

    /*public void WelcomeAdsDialog() {
        welcomedialog = new Dialog(MainActivity.this, R.style.DialogSlideAnim);
        //below single line for dialog animation
//        welcomedialog = new Dialog(MainActivity.this, R.style.PauseDialog);
        //end animation
        WindowManager.LayoutParams params = null;
        View viewWelcome = LayoutInflater.from(MainActivity.this).inflate(R.layout.welcome_layout, null);

        welcomedialog.setContentView(viewWelcome);
        if (welcomedialog.getWindow() != null) {
            params = welcomedialog.getWindow().getAttributes();
        }
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            welcomedialog.getWindow().setAttributes(params);
            welcomedialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        welcomedialog.setCancelable(false);
        welcomedialog.setCanceledOnTouchOutside(false);

//        if(!((Activity) context).isFinishing())
//        {
        //show dialog
        try {
            if (welcomedialog != null && !welcomedialog.isShowing()) {
                //below line is for animation
//                welcomedialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
                // end animation
                welcomedialog.show();
               //Log.e("check", "welcomeDialog:");
               //Log.e("activityname", "activityname:" + this.getClass().getSimpleName());
            }
        } catch (WindowManager.BadTokenException e) {
           //Log.e("activityname", "activityname:" + this.getClass().getSimpleName());
            e.printStackTrace();
        }
    }*/

    private void logoffFromWelcome() {
//        Constant.AppPref.edit().putString("store", "").putString("station", "").apply();
        boolean storeRemember = Constant.AppPref.getBoolean("storeRemember", false);
        if (!storeRemember) {
            Constant.AppPref.edit().putString("store", "").putString("station", "").putString("appname", "").putString("appNickname", "")
                    .putString("storeType", "").apply();
            Constant.AppPref.edit().putBoolean("storeRemember", false).putBoolean("stationRemember", false).apply();
        }
        //added by kaveri
        if (ListElementsArrayList != null) {
            ListElementsArrayList.clear();
        }

//        Edityed by Varun
        handler.removeCallbacks(delayedRunnable);
        if (Constant.Computerperfect!=null && !Constant.Computerperfect.isEmpty()){
            Constant.Computerperfect.clear();
        }
        Constant.isFromQT=false;
        Constant.AppPref.edit().clear().commit();
//        END


        ApiClientBillBoard.signId = "";
        Constant.customerOrderList.clear();
        Constant.stationList.clear();
        finish();

    }

    public void callImagesWs() {
        //call delete files here
        if (Constant.isNetworkAvailable(MainActivity.this)) {

            mViewPager.stopAutoScroll();
//                if (lladv != null) {
//                    lladv.setVisibility(View.GONE);
//                }
            showProgressBar(MainActivity.this);


            Constant.deleteFiles();

//            Setting the Industry type in the Store type for getting the Computer perfect images
            if (Constant.Computerperfect!=null && !Constant.Computerperfect.isEmpty()){
                Constant.Computerperfect.clear();
            }
            if (Constant.iscomefrom_adrequest_Same || Constant.isFromQT){
                Constant.storetype = "Computer Perfect-Wine & Spirit";
//                Constant.storetype = Constant.Industry_Type;
            }
//            END

            String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_IMAGES_DETAIL + Constant.STOREID + "/" +Constant.storetype;
//            String Urlban = Constant.WS_BASE_URL + Constant.GETPOLE_DISPLAY_IMAGE_DATA + Constant.storetype;
            TaskImages taskImages = new TaskImages(MainActivity.this, MainActivity.this);
            taskImages.execute(Urlban);
        }

    }

    @Override
    public void onGetImagesDetailsResult(List<ImagesDetailModel> ImageDetalList1) {

//        Checking it for if thsi come for the QT same images if Previous image and new images of QT is same then we show the computer perfect
        if (Constant.iscomefrom_adrequest_Same ){
           callcomputerperfectimage();
        }else {
//          END
            Log.i("Image", "Post execute adapter");
            Constant.LocalImageHeaderList = Constant.getImages(MainActivity.this, "Header", Constant.STOREID);
            Constant.LocalImageFooterList = Constant.getImages(MainActivity.this, "Footer", Constant.STOREID);
            if (!Constant.isFromQT) {
                Constant.LocalImageDetalList = Constant.getImages(MainActivity.this, "Home", Constant.STOREID);
                if (Constant.LocalImageDetalList != null) {
                    if (Constant.LocalImageDetalList.size() > 0) {
                        Constant.ImageDetalList = new ArrayList<>();
                        Collections.sort(Constant.LocalImageDetalList, new Comparator<ImagesDetailModel>() {
                            public int compare(ImagesDetailModel obj1, ImagesDetailModel obj2) {
                                return obj1.getImage().compareToIgnoreCase(obj2.getImage()); // To compare string values
                            }
                        });
                        Constant.ImageDetalList.addAll(Constant.LocalImageDetalList);
                    }
                } else { // else edited by janvi
                    if (Constant.ImageDetalList == null || Constant.ImageDetalList.size() == 0) {
//                        setDummyAdvertisement(); // corporate
                        setnoimage(); // corporate
                    }
                } //end ****
                myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, true);
                mViewPager.setAdapter(myCustomPagerAdapter);
                mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
                mViewPager.startAutoScroll();
                mViewPager.setCurrentItem(0);
                //mViewPager.setIndeterminate(true);
                if (mViewPager.getVisibility() == View.VISIBLE) {
                    mViewPager.startAutoScroll();
                }
            }else if (!isCallingAllowed()){
                callcomputerperfectimage();
            }

            Log.i("Image", "Post execute adapter done");
            if (Constant.isTimeToRefreshImg) {
                Constant.isTimeToRefreshImg = false;
                Constant.TransactionCounterForImage = 0;
            }
            //commented by janvi
//        Constant.ImageHeaderList = new ArrayList<>();
            // end
            if (Constant.LocalImageHeaderList == null) {
                ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
                imagesDetailModel.setBannerType("Header");
                imagesDetailModel.setImage("noimagebanner.jpg");
                imagesDetailModel.setImagepath("noimagebanner.jpg");
                Constant.ImageHeaderList.add(imagesDetailModel);
            } else {
                Constant.ImageHeaderList.addAll(Constant.LocalImageHeaderList);
            }
            if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
                setHeader();
            }
            //commented by janvi
//        Constant.ImageFooterList = new ArrayList<>();
            // end
            if (Constant.LocalImageFooterList == null) {
                ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
                imagesDetailModel.setBannerType("Footer");
                imagesDetailModel.setImage("noimagebanner.jpg");
                imagesDetailModel.setImagepath("noimagebanner.jpg");
                Constant.ImageFooterList.add(imagesDetailModel);
            } else {
                Constant.ImageFooterList.addAll(Constant.LocalImageFooterList);
            }
            if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
                setfooter();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(delayedRunnable);

      /*  if ((welcomedialog != null) && welcomedialog.isShowing()) {
            welcomedialog.dismiss();
        }*/
        //Log.e("activityname", "destroyed:");
    }

    public static void showProgressBar(Context context) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dismissProgressBar();
            }
        }
        try {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.place_autocomplete_progress);
            dialog.setContentView(R.layout.support_simple_spinner_dropdown_item);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.show();
        } catch (Exception e) {
        }
    }

    public static void dismissProgressBar() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

//    private void scheduleAsveCalls(boolean isFirst) {
//        if (Constant.isNetworkAvailable(MainActivity.this))
//            if (Constant.isFromQT){
//                Log.e(TAG, "scheduleAsveCalls: QT6");
//                callQT_Token();
//            }else{
//                callImagesWs();
//            }
//        Sign sign = Constant.getSign(MainActivity.this);
//        //get from shared preferance
//        if (sign != null) {
//            if (sign.getId() != null) {
//                if (sign.getId().trim().length() > 0) {
//                    ApiClientBillBoard.signId = sign.getId();
//                    try {
//                        tvbottomAppname.setText(Constant.STOREID + " - " + ApiClientBillBoard.signId + " - " + Constant.appNickname);
//                    } catch (Exception e) {
//                    }
//
//                }
//            }
//        }
//        if (ApiClientBillBoard.signId == null) {
//            //get signid
//            if (Constant.isNetworkAvailable(MainActivity.this))
//                fetchAllSigns();
//            else
//                setDummyAdvertisement(); // blipboard
//        } else if (ApiClientBillBoard.signId.trim().length() <= 0) {
//            //get signid
//            if (Constant.isNetworkAvailable(MainActivity.this))
//                fetchAllSigns();
//            else
//                setDummyAdvertisement(); // blipboard
//
//        } else {
//            ////update sign
//            ScheduledExecutorService exec2 = Executors.newSingleThreadScheduledExecutor();
//            exec2.scheduleWithFixedDelay(new Runnable() {
//                @Override
//                public void run() {
//                    if (Constant.isFromBlipBoard) {
//                        if (Constant.isNetworkAvailable(MainActivity.this)) {
//                            isInternetAvail = true;
//                            precacheBlipAdv();
//                        }
//                    }
//
//                }
//            }, 0, 30, TimeUnit.SECONDS);
//            ScheduledExecutorService exec1 = Executors.newSingleThreadScheduledExecutor();
//            exec1.scheduleWithFixedDelay(new Runnable() {
//                @Override
//                public void run() {
//                    // do stuff
//                    if (isAllowLoading) {
//                        if (Constant.isFromBlipBoard) {
//                            if (Constant.getTimeToRefresh(MainActivity.this, false)) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (Constant.isNetworkAvailable(MainActivity.this)) {
//                                            if (ApiClientBillBoard.signId != null) {
//                                                if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                                    fetchAdvFromBlip();
//                                                } else {
//                                                    setDummyAdvertisement(); // blipboard
//                                                }
//                                            } else {
//                                                setDummyAdvertisement(); // blipboard
//                                            }
//                                        } else {
//                                            isInternetAvail = false;
//                                            setDummyAdvertisement(); // blipboard
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//
//                }
//            }, 0, 1, TimeUnit.MINUTES);
//
//        }
//    }
//
//    public void fetchAdvFromBlip() {
//        //Log.d("TEST", "FLIP");
//        isAllowLoading = false;
//        mViewPager.stopAutoScroll();
//        ArrayList<String> dateList = new ArrayList<>();
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        DateFormat dfParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());
//        Date seldate = new Date();
//        dateList.add(nowAsISO);
//        Calendar cal = Calendar.getInstance(Locale.US);
//        cal.setTimeZone(tz);
//        cal.setTime(seldate);
//        cal.add(Calendar.SECOND, 10);
//        seldate = cal.getTime();
//        nowAsISO = df.format(seldate);
//        try {
//            seldate = dfParse.parse(nowAsISO);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            seldate = new Date();
//        }
//        dateList.add(nowAsISO);
//        Calendar cal1 = Calendar.getInstance(Locale.US);
//        cal1.setTimeZone(tz);
//        cal1.setTime(seldate);
//        cal1.add(Calendar.SECOND, 10);
//        seldate = cal1.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add((nowAsISO + "+0000"));
//        Calendar cal2 = Calendar.getInstance(Locale.US);
//        cal2.setTimeZone(tz);
//        cal2.setTime(seldate);
//        cal2.add(Calendar.SECOND, 10);
//        seldate = cal2.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add(nowAsISO + "+0000");
//        Calendar cal3 = Calendar.getInstance(Locale.US);
//        cal3.setTimeZone(tz);
//        cal3.setTime(seldate);
//        cal3.add(Calendar.SECOND, 10);
//        seldate = cal3.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add(nowAsISO + "+0000");
//        Calendar cal4 = Calendar.getInstance(Locale.US);
//        cal4.setTimeZone(tz);
//        cal4.setTime(seldate);
//        cal4.add(Calendar.SECOND, 10);
//        seldate = cal4.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add(nowAsISO + "+0000");
//        Constant.saveTimeToRefresh(MainActivity.this, nowAsISO);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(dateList);
//        Log.d("ms", "" + jsonStr);
//        RequestBody body = RequestBody.create(jsonStr,
//                MediaType.parse("application/json")
//        );
//        ApiInterface apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
//        Call<ArrayList<AdvSign>> call1 = apiServiceBillboard.signSignIdImagesGetschedule(ApiClientBillBoard.signId, Constant.authHeader, body);
//        call1.enqueue(new Callback<ArrayList<AdvSign>>() {
//            @Override
//            public void onResponse(Call<ArrayList<AdvSign>> call, Response<ArrayList<AdvSign>> response) {
//                try {
//                    advCount = 0;
//                    try {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "Refresh Adv!", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    } catch (Exception e) {
//                    }
//                    isAllowLoading = true;
//                    Constant.ImageDetalList = new ArrayList<>();
//                    if (response.body() != null) {
//                        if (response.body().size() > 0) {
//                            ArrayList<AdvSign> advLists = response.body();
//                            for (int i = 0; i < advLists.size(); i++) {
//                                ImagesDetailModel model = new ImagesDetailModel();
//                                model.setImage(advLists.get(i).getUrl());
//                                model.setImagepath(advLists.get(i).getUrl());
//                                model.setId(advLists.get(i).getId());
//                                Constant.ImageDetalList.add(model);
//                                try {
//                                    Glide.with(getApplicationContext())
//                                            .load(advLists.get(i).getUrl())
//                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
//                                            .priority(Priority.IMMEDIATE)
//                                            .preload(800, 1280);
//                                } catch (Exception e) {
//                                }
//
//                            }
//                            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, false);
//                            mViewPager.setAdapter(myCustomPagerAdapter);
//                            //mViewPager.startAutoScroll();
//                            mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
//                            //Log.d("Image", "IN SCROLL1" + Constant.ImageDetalList.get(0).getImage());
//                            mViewPager.startAutoScroll();
//                            mViewPager.setVisibility(View.VISIBLE);
//
//                        } else {
//                            //no image
//                            Constant.saveTimeToRefresh(MainActivity.this, "");
//                            setDummyAdvertisement(); // blipboard
//                        }
//                    } else {
//                        //no image
//                        Constant.saveTimeToRefresh(MainActivity.this, "");
//                        setDummyAdvertisement(); // blipboard
//                    }
//
//                } catch (Exception e) {
//                    //no img
//                    Constant.ImageDetalList = new ArrayList<>();
//                    setDummyAdvertisement(); //blipborad error
//                }
//                //mViewPager.setIndeterminate(true);
//                String responseVals = "No Ads";
//                String isPassFail = "fail";
//                try {
//                    if (response.body() == null) {
//                        if (response.errorBody().string() != null) {
//                            if (response.errorBody().string().trim().length() > 0) {
//                                responseVals = response.errorBody().string();
//                            } else {
//                                responseVals = "No ads retrived!";
//                            }
//                        } else {
//                            responseVals = "No ads retrived!";
//                        }
//                    } else if (response.body().size() <= 0) {
//                        responseVals = "No ads retrived!";
//                    } else {
//                        isPassFail = "pass";
//                        responseVals = gson.toJson(response.body());
//                    }
//                } catch (Exception e) {
//                }
//                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//                Call<Result> call1 = apiService.SaveRequestResponse_Log(Constant.STOREID, jsonStr, responseVals, android_id, ApiClientBillBoard.signId, isPassFail);
//                call1.enqueue(new Callback<Result>() {
//                    @Override
//                    public void onResponse(Call<Result> call, Response<Result> response) {
//                    }
//
//                    @Override
//                    public void onFailure(Call<Result> call, Throwable t) {
//                        call.cancel();
//
//                    }
//                });
//                Constant.saveImages(MainActivity.this, Constant.ImageDetalList, "Blip", Constant.selected_station_no);
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<AdvSign>> call, Throwable t) {
//                call.cancel();
//                //no img
//                setDummyAdvertisement(); // blipboard
//                try {
//                    String responseVals = "In Failure: " + t.getMessage();
//                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//                    Call<Result> call1 = apiService.SaveRequestResponse_Log(Constant.STOREID, jsonStr, responseVals, android_id, ApiClientBillBoard.signId, "fail");
//                    call1.enqueue(new Callback<Result>() {
//                        @Override
//                        public void onResponse(Call<Result> call, Response<Result> response) {
//                        }
//
//                        @Override
//                        public void onFailure(Call<Result> call, Throwable t) {
//                            call.cancel();
//
//                        }
//                    });
//                } catch (Exception e) {
//                }
//            }
//        });
//    }
//
    public void setDummyAdvertisement() {
        Constant.ImageDetalList = new ArrayList<>();
        ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
        imagesDetailModel.setBannerType("Home");
        imagesDetailModel.setId("-1");
        imagesDetailModel.setImage("dummy1");
        imagesDetailModel.setImagepath("dummy1");
        Constant.ImageDetalList.add(imagesDetailModel);
        imagesDetailModel = new ImagesDetailModel();
        imagesDetailModel.setBannerType("Home");
        imagesDetailModel.setId("-1");
        imagesDetailModel.setImage("dummy2");
        imagesDetailModel.setImagepath("dummy2");
        Constant.ImageDetalList.add(imagesDetailModel);
        imagesDetailModel = new ImagesDetailModel();
        imagesDetailModel.setBannerType("Home");
        imagesDetailModel.setId("-1");
        imagesDetailModel.setImage("dummy3");
        imagesDetailModel.setImagepath("dummy3");
        Constant.ImageDetalList.add(imagesDetailModel);
        myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, false);
        mViewPager.setAdapter(myCustomPagerAdapter);
        //mViewPager.startAutoScroll();
        mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
        //Log.d("Image", "IN SCROLL1" + Constant.ImageDetalList.get(0).getImage());
        mViewPager.startAutoScroll();
        try {
            if (llBillingLayout.getVisibility() != View.VISIBLE) {
                mViewPager.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }

//    public void createNewSignID() {
//        Sign signNew = new Sign();
//        signNew.setEnabled(true);
//        signNew.setName(signName);
//        try {
//            signNew.setDescription("CP for Dashboard " + " (" + " -" + Constant.appname + "-" + Constant.selected_station_no + ")_" + buildNo_Name);
//        } catch (Exception e) {
//            signNew.setDescription("CP for Dashboard " + " ( -" + Constant.appname + ")");
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
//                            Constant.saveSign(MainActivity.this, response.body());
//                            if (ApiClientBillBoard.signId != null) {
//                                if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                    try {
//                                        tvbottomAppname.setText(Constant.STOREID + " - " + ApiClientBillBoard.signId + " - " + Constant.appNickname.trim());
//                                    } catch (Exception e) {
//                                    }
//                                }
//                            }
//                            scheduleAsveCalls(false);
//                        }
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Sign> call, Throwable t) {
//                call.cancel();
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
//                            if (response.body().get(i).getName().contains("PD_" + android_id)) {
//                                ApiClientBillBoard.signId = response.body().get(i).getId();
//                                Constant.saveSign(MainActivity.this, response.body().get(i));
//                                break;
//                            }
//                        }
//                        if (ApiClientBillBoard.signId != null) {
//                            if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                try {
//                                    tvbottomAppname.setText(Constant.STOREID + " - " + ApiClientBillBoard.signId + " - " + appNicknamepref.trim());
//
//                                } catch (Exception e) {
//                                }
//                                //scheduleAsveCalls(false);
//                                if (Constant.isNetworkAvailable(MainActivity.this)) {
//                                    scheduleAsveCalls(false);
//                                } else {
//                                    isInternetAvail = false;
//                                    setDummyAdvertisement(); // blipboard
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
//
//            }
//        });
//
//    }
//
//    public void getLocationFromAddress(String strAddress) {
//        Geocoder coder = new Geocoder(MainActivity.this);
//        List<Address> address;
//        try {
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address != null) {
//                Address location = address.get(0);
//                if (location.getLatitude() > 0) {
//                    latitude = String.valueOf(Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
//                    longitude = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
//                    Log.d("TEST", "LAT " + latitude);
//                    Log.d("TEST", "Long " + longitude);
//                }
//                createNewSignID();
//            } else {
//                createNewSignID();
//            }
//
//        } catch (Exception e) {
//            createNewSignID();
//        }
//    }
//
//    public void precacheBlipAdv() {
//        //Log.d("TEST", "Pre FLIP");
//        ApiInterface apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
//        Call<ArrayList<AdvSign>> call1 = apiServiceBillboard.signSignIdImagesGet(ApiClientBillBoard.signId, Constant.authHeader);
//        call1.enqueue(new Callback<ArrayList<AdvSign>>() {
//            @Override
//            public void onResponse(Call<ArrayList<AdvSign>> call, Response<ArrayList<AdvSign>> response) {
//                try {
//                    if (response.body() != null) {
//                        if (response.body().size() > 0) {
//                            ArrayList<AdvSign> advLists = response.body();
//                            int count = advLists.size();
//                            for (int i = 0; i < count; i++) {
//                                try {
//                                    Glide.with(getApplicationContext())
//                                            .load(advLists.get(i).getUrl())
//                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
//                                            .signature(new ObjectKey(advLists.get(i).getId()))
//                                            .priority(Priority.LOW)
//                                            .preload(800, 1280);
//                                } catch (Exception e) {
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<AdvSign>> call, Throwable t) {
//                call.cancel();
//            }
//        });
//    }

//    public void tapToRefresh() {
//        {
//            isTouchVer = true;
//            long time = System.currentTimeMillis();
//            //if it is the first time, or if it has been more than 5 seconds since the first tap ( so it is like a new try), we reset everything
//            if (startMillis == 0 || (time - startMillis > 3000)) {
//                startMillis = time;
//                count = 1;
//            }
//            //it is not the first, and it has been  less than 3 seconds since the first
//            else { //  time-startMillis< 3000
//                count++;
//            }
////            if (Constant.isFromBlipBoard) {
////                if (count == 3 && isTouchVer) {
////                    isTouchVer = false;
////                    count = 0;
////                    Intent intent = new Intent(MainActivity.this, ResponseListActivity.class);
////                    intent.putExtra("androidID", android_id);
////                    startActivity(intent);
////
////                }
////            }
//        }
//    }

//    public void recordFlips(String advID) {
//        ArrayList<AdvSign> flipDataList = new ArrayList<>();
//        AdvSign sign = new AdvSign();
//        sign.setId(advID);
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        sign.setDuration("" + ApiClientBillBoard.timeForFlips);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());
//        Log.d("TEST", "Checking time flip DATE IS: " + nowAsISO);
//        sign.setStart(nowAsISO);
//        flipDataList.add(sign);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(flipDataList);
//        RequestBody body = RequestBody.create(jsonStr,
//                MediaType.parse("application/json")
//        );
//        ApiInterface apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
//        Call<Result> call1 = apiServiceBillboard.recordFlips(ApiClientBillBoard.signId, Constant.authHeader, body);
//        call1.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                call.cancel();
//            }
//        });
//
//    }

    public void checkInternetAvaibilityLoop() {
        ScheduledExecutorService exec_int_check = Executors.newSingleThreadScheduledExecutor();
        exec_int_check.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // do stuff
                if (!isInternetAvail) {
                    if (Constant.isNetworkAvailable(MainActivity.this)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Re-connecting...", Toast.LENGTH_SHORT).show();
                                isInternetAvail = true;

//                                if (Constant.isFromBlipBoard) {
//                                    if (Constant.getTimeToRefresh(MainActivity.this, false)) {
//                                        if (ApiClientBillBoard.signId != null) {
//                                            if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                                Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
//                                                fetchAdvFromBlip();
//                                            } else {
//                                                setDummyAdvertisement(); // blipboard
//                                            }
//                                        } else {
//                                            setDummyAdvertisement(); //blipboard
//                                        }
//
//                                    }
//                                } else {
                                    Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                                    if (Constant.isFromQT){
                                        Log.e(TAG, "run: QT8");
//                                          Checking if the store time is closed or not
                                        if (isCallingAllowed()) {
                                            callQT_Token();
                                        }else{
                                            Toast.makeText(MainActivity.this, "Store Timing Is Closed !", Toast.LENGTH_SHORT).show();
                                            callcomputerperfectimage();
                                        }
//                                        END
                                    }else{
                                        callImagesWs();
                                    }
                                }
//                            }
                        });

                    }
                }
            }
        }, 0, offlineDelay, TimeUnit.SECONDS);
    }


//    Edited by Varun for to set timing while calling the QT (11 PM to 8 AM)
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
//        int Close_Time_hour = 12;
        int Close_Time_minutes = localTime1.getMinute();

        Log.e(TAG, "isCallingAllowed:open  "+Open_time_hour );
        Log.e(TAG, "isCallingAllowed:close "+Close_Time_hour);

        if (currentHour >= Open_time_hour && currentHour < Close_Time_hour ){
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


//    ------------------------------------ Un Wanted Code ----------------------------

    public static void AdApprovals(){
        String url = null;
        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.AD_APPROVALS;
        TaskAdApprovals taskAdApprovals = new TaskAdApprovals(context,Constant.localAuthQTlist.getAccess_token());
        taskAdApprovals.execute(url);
    }

    @Override
    public void onAdApprovals(List<AdApprovalModel> adApprovalModel) {
        if (adApprovalModel!=null && !adApprovalModel.isEmpty()){
            Toast.makeText(context, "AdApproval Success", Toast.LENGTH_LONG).show();
            Log.e(TAG, "onAdApprovals: " + adApprovalModel.size() );
        }
    }

    public void callSubmitAdRequest(String name) {

        String url = null;
        url = Constant.BASE_URL_QT+Constant.ORGS +Constant.QT_ID + Constant.WS_AD_UNIT + "/" +name +Constant.WS_SUBMIT_AD_REQUEST;
        TaskSubmitAdRequest taskSubmitAdRequest = new TaskSubmitAdRequest(this,getApplicationContext(),auth_qt.getAccess_token());
        taskSubmitAdRequest.execute(url);
    }

    @Override
    public void onSubmitAdRequstResult(List<SubmitAdRequestModel> submitAdRequest) {

        for (int i=0 ; i<submitAdRequest.size(); i++) {
            if (submitAdRequest.get(i).getCreative() != null) {
                Constant.localSubmitAdRequestlist.add(submitAdRequest.get(i));
            }
        }
    }

    public void setnoimage() {

        Constant.ImageDetalList = new ArrayList<>();
        ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
        imagesDetailModel.setBannerType("Home");
        imagesDetailModel.setId("-1");
        imagesDetailModel.setImage("no image");
        imagesDetailModel.setImagepath("no image");
        Constant.ImageDetalList.add(imagesDetailModel);

        myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, Constant.ImageDetalList, false);
        mViewPager.setAdapter(myCustomPagerAdapter);
        mViewPager.setOffscreenPageLimit(Constant.ImageDetalList.size());
        try {
            if (llBillingLayout.getVisibility() != View.VISIBLE) {
                mViewPager.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }

//    public void Adunitnotfound_pop_up(MainActivity context) {
//
//        Dialog dialog;
//        View view;
//        dialog = new Dialog(context, R.style.DialogSlideAnim_login);
//        dialog.setCanceledOnTouchOutside(false);
//        view = LayoutInflater.from(context).inflate(R.layout.adunit_error_pop_up, null);
//
//        // set the custom dialog components - text, image and button
//        TextView txtErrorName = (TextView) view.findViewById(R.id.txtErrorName);
//
//        txtErrorName.setText("This station requires action, as this Enhanced Pole Display unit is not defined on the QT system, etc.");
//
//        txtErrorName.setGravity(Gravity.CENTER);
//
//        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);
////        Button btnCancel = (Button) view.findViewById(R.id.btn_Cancel);
//
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                logoffFromWelcome();
//            }
//        });
//
//
//        dialog.setContentView(view);
//        dialog.getWindow().setAttributes(params);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//
//        dialog.show();
//    }


}

