package com.poledisplayapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poledisplayapp.models.Auth_QTModel;
import com.poledisplayapp.models.CustomerOrderModel;
import com.poledisplayapp.models.GetAdRequestModel;
import com.poledisplayapp.models.ImagesDetailModel;
import com.poledisplayapp.models.RetrieveAdunitModel;
import com.poledisplayapp.models.Sign;
import com.poledisplayapp.models.StationModel;
import com.poledisplayapp.models.SubmitAdRequestModel;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import poledisplayapp.R;

import static android.content.ContentValues.TAG;

public class Constant {
    //git token
    //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
    public static Dialog dialog;
    public static int CountForRefresh = 2;
    public static int TransactionCounterForImage = 0;
    public static boolean isRadioButtonSelected = false;
    public static boolean isTimeToRefreshImg = false;
    public static final HttpClient httpClient = new DefaultHttpClient();
    public static boolean storeno_remember = false;
    public static boolean station_no_remember = false;
    public static String selected_station_no = "";
    public static String entered_storeno = "";
    public static String appname = "";
    public static String appNickname = "";
    public static String storetype = "";
    public static String storetypes = "";
    public static String track = "";
    public static boolean isComeFromRemove = false;
    public static boolean islist = false;
    public static String lastRemovedchildFirebasekey = "";

    //blipboard
    public static boolean isFromBlipBoard = false;
    public static boolean isFromQT = false;
    public static ProgressDialog loading = null;
//    public static boolean isFromBlipBoard = true;

    //local
    public static String IMG_BASE = "http://192.168.172.244:808/";
    public static String WS_BASE = "http://192.168.172.244:889";
    public static String LighningURL = "http://192.168.172.244:140/LigtningOnlineService.asmx/";
    public static String authHeader="Bearer 9ed36663c7905eb3dffe55d9ce79ef78cd30877a";
    public static final String BASE_URL_BLIP = "https://app.blipbillboards.com/external/";
    public static final String BASE_URL_QT = "https://api.placeexchange.com/v3/";

    //    Test
//    public static String IMG_BASE = "https://testimages.lightningpos.com/";
//    public static String WS_BASE = "https://ecomtestWCF.lightningpos.com";
//    public static String LighningURL = "http://evo.lightningpos.com/LigtningOnlineService.asmx/";
//    public static String authHeader="Bearer 9ed36663c7905eb3dffe55d9ce79ef78cd30877a";
//    public static final String BASE_URL_BLIP = "https://blipboards-tech.herokuapp.com/external/";
//    public static final String BASE_URL_QT = "https://api.placeexchange.com/v3/";

    //Secure
//    public static String IMG_BASE = "https://images.lightningpos.com/";
//    public static String WS_BASE = "https://ecomsecureWCF.lightningpos.com";
//    public static String LighningURL = "http://posservice.lightningpos.com/LigtningOnlineService.asmx/";
//    public static String authHeader="Bearer 971dcf8b19b3bc1cac56e40f5858bbad38823d4e";
//    public static final String BASE_URL_BLIP = "https://app.blipbillboards.com/external/";
//    public static final String BASE_URL_QT = "https://api.placeexchange.com/v3/";

    public static String WS_BASE_URL = WS_BASE + "/WebStoreRestService.svc/";
    public static String IMG_BANNER_URL = "WebStoreImages/PoleImages/";
    public static final String WS_ANDROID_TABLET_VALIDATION = "/AndroidTabletValidation";
    public static final String WS_send_message_for_store_login = "/sendmessageforstorelogin";

    public static final String ORGS = "orgs";
    public static final String WS_AD_UNIT = "/adunits";
    public static final String WS_SUBMIT_AD_REQUEST = "/adrequests";
    public static final String WS_PLAYS = "/plays";
    public static final String AD_APPROVALS = "/adapprovals";
    public static final String QT_USERNAME = "cp.support";
    public static final String QT_PASSWORD = "PWx9Mr;Z-n3%aD8";
    public static final String QT_ID = "/00cb716a-c7f8-4f14-8244-cf510531e696";

    public static List<ImagesDetailModel> Computerperfect = new ArrayList<ImagesDetailModel>();

    public static List<ImagesDetailModel> ImageDetalList = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> LocalImageDetalList = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> ImageHeaderList = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> LocalImageHeaderList = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> ImageFooterList = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> LocalImageFooterList = new ArrayList<ImagesDetailModel>();

    public static List<ImagesDetailModel> ImageDetalList2 = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> ImageDetalList3 = new ArrayList<ImagesDetailModel>();
    public static List<ImagesDetailModel> LocalImageDetalList2 = new ArrayList<ImagesDetailModel>();
    public static List<SubmitAdRequestModel> localSubmitAdRequestlist = new ArrayList<>();
    public static Auth_QTModel localAuthQTlist = new Auth_QTModel();
    public static List<RetrieveAdunitModel> localretrieveAdunitModelList = new ArrayList<RetrieveAdunitModel>();
    public static List<GetAdRequestModel> localgetAdRequestModel = new ArrayList<GetAdRequestModel>();

    static final List<CustomerOrderModel> customerOrderList = new ArrayList<>();
    static final List<String> tempitemlistforRemoveitems = new ArrayList<>();
    public static final List<String> descList = new ArrayList<>();
    //    static final List<String> OrderKeyList = new ArrayList<>();
    public static boolean isLightcolor = false;
    public static SharedPreferences AppPref = null;
    public static String PrefName = "POSPref";
    public static String lastRemovedchildkey = "";
//    public static String GETPOLE_IMAGES_DETAIL = "GetPoleImagesDetail/";
    public static String GETPOLE_IMAGES_DETAIL = "GetPoleDisplayImageForMobile/";
//    public static String GETPOLE_DISPLAY_IMAGE_DATA = "GetPoleDisplayImageData/";
    public static String STOREID = "";
    public static String address = "175 Memorial Hwy # 2-7, New Rochelle, NY 10801, United States";

    public static ArrayList<StationModel> stationList = new ArrayList<StationModel>();
    public static boolean isgetAdreponesnull = false;
    public static boolean isfromlogin = false;
    public static boolean isfromsplash = false;
    public static String imageURL = "";
    public static String imageMime = "";
//    public static boolean isCalledAlready = false;
//    public static String colorName = "";
//    public static int perviousval = 0;p
    public static boolean iscomefrom_adrequest_null_response = false;
    public static boolean iscomefrom_adrequest_Same = false;
    public static int count = 0;
    public static String Industry_Type = "";
    public static String Open_Time = "";
    public static String Close_Time = "";
    public static int adunit_height = 0;
    public static int adunit_width = 0;


    public static void saveImages(Context activity, List<ImagesDetailModel> progress, String type, String storeno) {
        try {
            SharedPreferences pref;
            Gson gson = new Gson();
            if (type.equalsIgnoreCase("Blip")) {
                pref = activity.getApplicationContext().getSharedPreferences("Images" + type, 0);
            }
//            Edited by Varun for QT
            else if (type.equalsIgnoreCase("QT")){
                pref = activity.getApplicationContext().getSharedPreferences( "Images" + type, 0);
            }
//            END
            else {
                pref = activity.getApplicationContext().getSharedPreferences(storeno + "Images" + type, 0);
            }
            SharedPreferences.Editor editor = pref.edit();
            if (pref.contains("imageList")) {
                editor.remove("imageList");
            }
            editor.putString("imageList", gson.toJson(progress)); // Storing string
            editor.commit();
            //Log.d("kaveriImage","SAVED IN LOCAL");
        } catch (Exception e) {
        }
    }

    public static List<ImagesDetailModel> getImages(Activity activity, String type, String storeno) {
        Gson gson = new Gson();
        SharedPreferences pref;
        if (type.equalsIgnoreCase("Blip")) {
            pref = activity.getApplicationContext().getSharedPreferences("Images" + type, 0);
        }
//        Edited by Varun for QT
        else if (type.equalsIgnoreCase("QT")){
            pref = activity.getApplicationContext().getSharedPreferences("Images" + type, 0);
        }
//        END
        else {
            pref = activity.getApplicationContext().getSharedPreferences(storeno + "Images" + type, 0);
        }
        Type type1 = new TypeToken<ArrayList<ImagesDetailModel>>() {
        }.getType();
        List<ImagesDetailModel> userValue = gson.fromJson(pref.getString("imageList", null), type1);
        return userValue;
    }

    public static void saveSign(Activity activity, Sign Sign) {
        Gson gson = new Gson();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Sign", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userSign", gson.toJson(Sign)); // Storing string
        editor.commit();
    }

    public static Sign getSign(Activity activity) {
        try {
            Gson gson = new Gson();
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Sign", 0);
            Type type = new TypeToken<Sign>() {
            }.getType();
            Sign Sign = gson.fromJson(pref.getString("userSign", null), type);
            return Sign;
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveFlagForBlipBoard(Activity activity, boolean isFromBlipBoard, String storeNo) {
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("FlagMain_" + storeNo, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("Flag", isFromBlipBoard); // Storing string
        editor.commit();
    }

    public static boolean getFlagForBlipBoard(Context activity, String storeNo) {
        try {
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("FlagMain_" + storeNo, 0);
            boolean Sign = pref.getBoolean("Flag", false);
            return Sign;
        } catch (Exception e) {
            return false;
        }
    }

    public static void saveTimeToRefresh(Activity activity, String strDate) {
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("SignTime", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SignTimeSave", strDate); // Storing string
        editor.apply();
    }

    public static boolean getTimeToRefresh(Activity activity, boolean isPreCache) {
        try {
            Log.d("KAVERI LOGS", "Checking time");
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("SignTime", 0);
            //match two times
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            String timeToCheck = pref.getString("SignTimeSave", null);
            if (timeToCheck == null) {
                return true;
            } else if (timeToCheck.length() <= 0) {
                return true;
            } else {
                Date seldate = null, checkdate = null;
                try {
                    DateFormat dfNew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    seldate = dfNew.parse(nowAsISO);
                    checkdate = dfNew.parse(timeToCheck);
                    Log.d("KAVERI LOGS", "Checking time: " + seldate);
                    Log.d("KAVERI LOGS", "Checking time: " + checkdate);
                } catch (ParseException e) {
                }
                assert seldate != null;
                if (seldate.after(checkdate)) {
                    Log.d("KAVERI LOGS", "Checking time: Match for load ");
                    return true;
                } else {
                    Log.d("KAVERI LOGS", "Checking time: Not Match for load ");
                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static String getTimeToDisplay(Activity activity) {
        try {
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("SignTime", 0);
            //match two times
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            String timeToCheck = pref.getString("SignTimeSave", null);
            if (timeToCheck == null) {
                return "0";
            } else if (timeToCheck.length() <= 0) {
                return "0";
            } else {
                Date seldate = null, checkdate = null;
                try {
                    DateFormat dfNew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    seldate = dfNew.parse(nowAsISO);
                    checkdate = dfNew.parse(timeToCheck);
                    Log.d("KAVERI LOGS", "Checking time: " + seldate);
                    Log.d("KAVERI LOGS", "Checking time: " + checkdate);
                } catch (ParseException e) {
                }
                if (seldate.after(seldate)) {
                    Log.d("KAVERI LOGS", "Checking time: Match for load ");
                    return "0";
                } else {
                    long diff = checkdate.getTime() - seldate.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    if (minutes == 0)
                        return "0.5";
                    else
                        return String.valueOf(minutes);
                }
            }

        } catch (Exception e) {
            return "0";
        }
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String getUUId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
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
            dialog.setContentView(R.layout.progress_custom_dialog);
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
            dialog.dismiss();
        } catch (Exception e) {
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isConnected = true;
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        } catch (Exception e) {
        }
        return isConnected;
    }

    public static void deleteFiles() {
        String path="";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            //Do something
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/pos/";
        }else {
            path = Environment.getExternalStorageDirectory()+"/pos/";
        }
        File file = new File(path);

        if (file.exists()) {
            file.delete();
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                Log.d("TEST","ERROR");
            }
        }
    }

    public static void deleteFile() {
        String path = "";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Do something
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/NewQT/";
        } else {
            path = Environment.getExternalStorageDirectory() + "/NewQT/";
        }
        File file = new File(path);

        if (file.exists()) {
            file.delete();
            String deleteCmd = "rm -r " + path;
            try {
                Runtime.getRuntime().exec(deleteCmd);
            } catch (IOException e) {
                Log.d("TEST", "ERROR: " + e.getMessage());
            }
        }
    }

}
