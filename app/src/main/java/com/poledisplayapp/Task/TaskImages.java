package com.poledisplayapp.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import poledisplayapp.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.poledisplayapp.Constant;
import com.poledisplayapp.NetworkUtil;
import com.poledisplayapp.models.ImagesDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class TaskImages extends AsyncTask<String, Void, String> {

    Context context;
    ProgressDialog loading = null;
    int countTotal = 0, countForImgae = 0;
    TaskImagesEvent taskImagesEvent;

    @SuppressLint("ResourceType")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (context != null) {
                if (!Constant.isFromBlipBoard) {
                    loading = new ProgressDialog(context, R.style.MyprogressDTheme);
                    loading.setCancelable(false);
                    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loading.show();
                }
//                if (! isFinishing()) {
//                    loading.show();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public TaskImages(TaskImagesEvent taskImagesEvent, Context context) {
        this.taskImagesEvent = taskImagesEvent;
        this.context = context;
    }

    public interface TaskImagesEvent {
        void onGetImagesDetailsResult(List<ImagesDetailModel> ImageDetalList);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.i("web service--Cart", "request url : " + strings[0]);
        int count = 0;
        boolean retry = false;
        StringBuilder responseStrBuilder = new StringBuilder();
        do {
            retry = false;
            try {
                NetworkUtil.doNetworkProcessGet(strings[0], responseStrBuilder);
                String response = responseStrBuilder.toString();
                if (response != null) {
                    if (!Constant.isFromBlipBoard)
                        Constant.ImageDetalList = new ArrayList<>();
                    Constant.ImageHeaderList = new ArrayList<>();
                    Constant.ImageFooterList = new ArrayList<>();
                    Constant.LocalImageHeaderList = new ArrayList<>();
                    Constant.LocalImageFooterList = new ArrayList<>();
                    if (!Constant.isFromBlipBoard)
                        Constant.LocalImageDetalList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    countTotal = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        // commented below line for displaying centralized img with imgpath
                        String url_img = explrObject.getString("Image");
                        String url = explrObject.getString("ImagePath");
                        // end **************
                        String storeno = explrObject.getString("StoreNo");
                        String BannerType = explrObject.getString("BannerType");
                        ImagesDetailModel model = new ImagesDetailModel();
                        model.setImage(url_img);
                        model.setImagepath(url);
                        model.setStoreNo(storeno);
                        model.setBannerType(BannerType);
                        try {
                            if (!Constant.isFromBlipBoard) {
//                                countTotal = jsonArray.length();
                            }
                            //Log.d("kaveriImage",""+countTotal);
                        } catch (Exception e) {
                        }

                        if (BannerType.equals("Home") && !Constant.isFromBlipBoard) {
//                          Edited by Varun for
                            if (model.getImagepath()!=null && !model.getImagepath().equals("")
                                    && model.getImage()!=null && !model.getImage().equals("")) {
                                countTotal++;
//                                ?END
                                Constant.ImageDetalList.add(model);

                            }
                            // commented below line for displaying centralized img with imgpath
                            String urlImageinner = url;
//                            String urlImageinner = Constant.IMG_BASE + Constant.IMG_BANNER_URL + storeno + "/" + url;
//                          end
                            try {
                                Glide.with(context).asDrawable().load(urlImageinner).fitCenter().preload(800, 1280);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (BannerType.equalsIgnoreCase("Header")) {

                            if (model.getImagepath()!=null && !model.getImagepath().equals("")
                                    && model.getImage()!=null && !model.getImage().equals("")) {
                                Constant.ImageHeaderList.add(model);
                                Constant.LocalImageHeaderList.add(model);
                            }
                        } else if (BannerType.equalsIgnoreCase("Footer")) {
                            if (model.getImagepath()!=null && !model.getImagepath().equals("")
                                    && model.getImage()!=null && !model.getImage().equals("")) {
                                Constant.ImageFooterList.add(model);
                                Constant.LocalImageFooterList.add(model);
                            }
                        }
                    }
                }
                return response;

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonGenerationException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            retry = true;
            count += 1;
        } while (count < 3 && retry);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        if (Constant.isFromBlipBoard) {
//            try {
//                if (loading != null && loading.isShowing()) {
//                    loading.dismiss();
//                }
//            } catch (Exception e) {
//            }
//            setHeaderFooter();
//        } else {
            if (Constant.ImageDetalList.size() == 0) {
//                ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
//                imagesDetailModel.setBannerType("Home");
//                imagesDetailModel.setId("-1");
//                imagesDetailModel.setImage("dummy1");
//                imagesDetailModel.setImagepath("dummy1");
//                Constant.ImageDetalList.add(imagesDetailModel);
//                imagesDetailModel = new ImagesDetailModel();
//                imagesDetailModel.setBannerType("Home");
//                imagesDetailModel.setId("-1");
//                imagesDetailModel.setImage("dummy2");
//                imagesDetailModel.setImagepath("dummy2");
//                Constant.ImageDetalList.add(imagesDetailModel);
//                imagesDetailModel = new ImagesDetailModel();
//                imagesDetailModel.setBannerType("Home");
//                imagesDetailModel.setId("-1");
//                imagesDetailModel.setImage("dummy3");
//                imagesDetailModel.setImagepath("dummy3");
                ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
                imagesDetailModel.setBannerType("Home");
                imagesDetailModel.setId("-1");
                imagesDetailModel.setImage("no image");
                imagesDetailModel.setImagepath("no image");
                Constant.ImageDetalList.add(imagesDetailModel);
                Constant.Computerperfect.addAll( Constant.ImageDetalList);
                if (!Constant.isFromBlipBoard) {
                    if (loading != null && loading.isShowing()) {
                        loading.dismiss();
                    }
                }
                if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0
                        || Constant.ImageFooterList !=null && Constant.ImageFooterList.size() > 0){
                    setHeaderFooter();
                }else{
                    taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
                }
//                setHeaderFooter();
                //taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
            } else {
                Constant.saveImages((Activity) context, Constant.ImageDetalList, "Home", Constant.STOREID);

                Constant.Computerperfect.addAll(Constant.ImageDetalList);

                Constant.LocalImageDetalList = new ArrayList<>();
                for (int i = 0; i < Constant.ImageDetalList.size(); i++) {
                    ImagesDetailModel imagesDetailModel = Constant.ImageDetalList.get(i);
                    // commented below line for displaying centralized img with imgpath
//                    String urlDetail = Constant.IMG_BASE + Constant.IMG_BANNER_URL + imagesDetailModel.getStoreNo() + "/" + imagesDetailModel.getImage();
                    String urlDetail = imagesDetailModel.getImagepath().toString().trim();

                    // aaded to prevent app crashes when image=""
                    String imgName = "";
                    String extention = "";

                    if (imagesDetailModel.getImage() != null && !imagesDetailModel.getImage().isEmpty()) {
                        String[] split = imagesDetailModel.getImage().split("\\.");
                        if (split.length >= 2) {
                            imgName = split[0];
                            extention = split[1];
                        } else {
                            // Handle the case when the split array doesn't have at least two elements
                            // You can throw an exception, set default values, or handle it in any other appropriate way
                        }
                    }
                    // end **********************

                    File tempFile = null;
                    File sdcard=null;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        //Do something
                        sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    }else {
                        sdcard = Environment.getExternalStorageDirectory();
                    }
                    tempFile = new File(sdcard + "/pos/" + i + "_" +
                            imgName +
                            "." + extention);
                    File dir = tempFile.getParentFile();
                    try {
                        if (!dir.exists())
                            dir.mkdirs();
                        if (!tempFile.exists()) {
                            tempFile.createNewFile();
                        } else {
                            tempFile.delete();
                            tempFile = new File(sdcard + "/pos/" + i + "_" +
                                    imgName +
                                    "." + extention);
                            tempFile.createNewFile();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    if(tempFile.exists()) {
                        ImagesDetailModel model = new ImagesDetailModel();
                        model.setBannerType("Home");
                        model.setImage(tempFile.getAbsolutePath());
                        model.setImagepath(tempFile.getAbsolutePath());
//                        Constant.LocalImageDetalList.add(i, model); //crashed here
                        Constant.LocalImageDetalList.add(model); //edited by janvi on 03/31/2023

                        if (Constant.LocalImageDetalList.size() >= Constant.ImageDetalList.size()) {
                            Constant.saveImages((Activity) context, Constant.LocalImageDetalList, "Home", Constant.STOREID);
                        }
                        //Log.d("kaveriImage", "SAVED IN LOCAL home");
                        saveImage(context, urlDetail, tempFile);
                    }else{
                        if (loading != null && loading.isShowing()) {
                            loading.dismiss();
                        }
                    }
                }
                setHeaderFooter();
            }
//        }
    }

    private void setHeaderFooter() {
        //header start
        Constant.LocalImageHeaderList = new ArrayList<>();
        if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
            //above if condition added by janvi due to avoid crash
            // commented below line for displaying centralized img with imgpath
//            String urlDetailHeader = Constant.IMG_BASE + Constant.IMG_BANNER_URL + Constant.ImageHeaderList.get(0).getStoreNo() + "/" + Constant.ImageHeaderList.get(0).getImage();
            String urlDetailHeader = Constant.ImageHeaderList.get(0).getImagepath().toString();
            //end *****************

            //check image null
            if(Constant.ImageHeaderList.get(0).getImage() != null && !Constant.ImageHeaderList.get(0).getImage().isEmpty()) {

                String[] split = Constant.ImageHeaderList.get(0).getImage().split("\\.");
                String imgName = split[0];
                String extention = split[1];
                File tempFile = null;
                File sdcard = null;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    //Do something
                    sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                } else {
                    sdcard = Environment.getExternalStorageDirectory();
                }
                tempFile = new File(sdcard + "/pos/" + 0 + "_" +
                        imgName +
                        "." + extention);
                File dir = tempFile.getParentFile();
                try {
                    if (!dir.exists())
                        dir.mkdirs();
                    if (!tempFile.exists()) {
                        tempFile.createNewFile();
                    } else {
                        tempFile.delete();
                        tempFile = new File(sdcard + "/pos/" + 0 + "_" +
                                imgName +
                                "." + extention);
                        tempFile.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(tempFile.exists()) {
                    ImagesDetailModel model = new ImagesDetailModel();
                    model.setBannerType("Header");
                    model.setImage(tempFile.getAbsolutePath());
                    model.setImagepath(tempFile.getAbsolutePath());
                    Constant.LocalImageHeaderList.add(0, model);
                    if (Constant.LocalImageHeaderList.size() >= Constant.ImageHeaderList.size()) {
                        Constant.saveImages((Activity) context, Constant.LocalImageHeaderList, "Header", Constant.STOREID);
                    }
                    //Log.d("kaveriImage", "SAVED IN LOCAL header");
                    saveImage(context, urlDetailHeader, tempFile);
                }else{
                    if (loading != null && loading.isShowing()) {
                        loading.dismiss();
                    }
                }
            }
        }
        //end ************* image check

        // header
        //footer start
        Constant.LocalImageFooterList = new ArrayList<>();
        if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {

            if (Constant.ImageFooterList.get(0).getImage() != null && !Constant.ImageFooterList.get(0).getImage().isEmpty()) {

                String[] split1 = Constant.ImageFooterList.get(0).getImage().split("\\.");
                String imgName1 = split1[0];
                String extention1 = split1[1];
                File tempFile1 = null;
                File sdcard = null;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    //Do something
                    sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                } else {
                    sdcard = Environment.getExternalStorageDirectory();
                }
                tempFile1 = new File(sdcard + "/pos/" + 0 + "_" +
                        imgName1 +
                        "." + extention1);
                File dir1 = tempFile1.getParentFile();
                try {
                    if (!dir1.exists())
                        dir1.mkdirs();
                    if (!tempFile1.exists()) {
                        tempFile1.createNewFile();
                    } else {
                        tempFile1.delete();
                        tempFile1 = new File(sdcard + "/pos/" + 0 + "_" +
                                imgName1 +
                                "." + extention1);
                        tempFile1.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

                ImagesDetailModel model1 = new ImagesDetailModel();
                model1.setBannerType("Footer");
                model1.setImage(tempFile1.getAbsolutePath());
                model1.setImagepath(tempFile1.getAbsolutePath());
                Constant.LocalImageFooterList.add(0, model1);
                if (Constant.LocalImageFooterList.size() >= Constant.ImageFooterList.size()) {
                    Constant.saveImages((Activity) context, Constant.LocalImageFooterList, "Footer", Constant.STOREID);
                }
                //Log.d("kaveriImage", "SAVED IN LOCAL footer");
                // commented below line for displaying centralized img with imgpath
//            String urlDetailFooter = Constant.IMG_BASE + Constant.IMG_BANNER_URL + Constant.ImageFooterList.get(0).getStoreNo() + "/" + Constant.ImageFooterList.get(0).getImage();
                String urlDetailFooter = Constant.ImageFooterList.get(0).getImagepath().toString();
                //end *************
                saveImage(context, urlDetailFooter, tempFile1);
            }
        }
        // end footer
    }

    public void saveImage(Context mContext, String urlDetail, File tempFile) {
        try {
            if (mContext != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(urlDetail)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                write(resource, tempFile);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        } catch (Exception e) {
        }

    }

    public void write(Bitmap bitmap, File tempFile) {
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            outStream.close();
            countForImgae++;
            Log.d("kaveriImage", "" + countForImgae + " total count" + countTotal);
            if (countForImgae >= countTotal) {
                //Log.d("kaveriImage","Stop loader");
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }

                Constant.ImageDetalList = new ArrayList<>();

                Constant.ImageHeaderList = new ArrayList<>();
                Constant.ImageFooterList = new ArrayList<>();

                Constant.ImageDetalList.addAll(Constant.LocalImageDetalList);

                Constant.ImageHeaderList.addAll(Constant.LocalImageHeaderList);
                Constant.ImageFooterList.addAll(Constant.LocalImageFooterList);
                Log.e("", "Modi1" );
                taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
            }
            //edited by janvi
          /*  else{
                if(!Constant.isFromBlipBoard) {
                    if(loading != null && loading.isShowing()) {
                        loading.dismiss();
                    }
                }
            }*/
            //
        } catch (Exception e) {
            e.printStackTrace();
            if (loading != null && loading.isShowing()) {
                //Log.d("kaveriImage","Stop loader");
                loading.dismiss();
            }
            Log.e("", "Modi 2 " );
            taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
        }

    }

}
