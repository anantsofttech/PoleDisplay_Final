package com.poledisplayapp.Task;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import poledisplayapp.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
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

public class TaskImagesAdvance extends AsyncTask<String, Void, String> {
    public static Dialog dialog;
    // ProgressDialog loading = null;
    Context context;
    ProgressDialog loading = null;
    int count = 0, countTotal = 0;
    String storeno;
    boolean toShowLoader = true;
    TaskImagesAdvEvent taskImagesEvent;

    @SuppressLint("ResourceType")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (toShowLoader) {
            if (context != null) {
                loading = new ProgressDialog(context, R.style.MyprogressDTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }
        }
    }

    public TaskImagesAdvance(TaskImagesAdvEvent taskImagesEvent, Context context, String storeno) {
        toShowLoader = true;
        this.taskImagesEvent = taskImagesEvent;
        this.context = context;
        this.storeno = storeno;
    }

    public TaskImagesAdvance(Context context, String storeno) {
        toShowLoader = false;
        this.context = context;
        this.storeno = storeno;
    }

    public interface TaskImagesAdvEvent {
        void onGetImagesDetailsResult(List<ImagesDetailModel> ImageDetalList);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.i("web service--Cart", "request url : " + strings[0]);
        int countInner = 0;
        boolean retry = false;
        StringBuilder responseStrBuilder = new StringBuilder();
        do {
            retry = false;
            try {
                NetworkUtil.doNetworkProcessGet(strings[0], responseStrBuilder);
                String response = responseStrBuilder.toString();
                if (response != null) {
                    if (!Constant.isFromBlipBoard) {
                        Constant.ImageDetalList = new ArrayList<>();
                    }
                    Constant.ImageHeaderList = new ArrayList<>();
                    Constant.ImageFooterList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    countTotal = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        // commented below line for displaying centralized img with imgpath
                        String url_img = explrObject.getString("Image");
                        String url = explrObject.getString("ImagePath");
                        //end
                        if (url == null) {
                            url = "";
                        }
                        if (url.trim().length() > 0) {
                            //Commented for storetype img
//                            String storeno = explrObject.getString("StoreNo");
                            String storeno = Constant.STOREID;
                            // end ***********
                            String BannerType = explrObject.getString("BannerType");
                            ImagesDetailModel model = new ImagesDetailModel();
                            model.setImage(url_img);
                            model.setImagepath(url);
                            model.setStoreNo(storeno);
                            model.setBannerType(BannerType);
                            if (BannerType.equals("Home") && !Constant.isFromBlipBoard) {
//                                Edited by Varun
                                if (model.getImagepath()!=null && !model.getImagepath().equals("")
                                        && model.getImage()!=null && !model.getImage().equals("")) {
//                                  END
                                    Constant.ImageDetalList.add(model);

                                }
//                                Constant.ImageDetalList.add(model);
                                // commented below line for displaying centralized img with imgpath
//                                String urlImageinner = Constant.IMG_BASE + Constant.IMG_BANNER_URL + storeno + "/" + url;
                                String urlImageinner = url;
                                //end******
                                Glide.with(context).asDrawable().load(urlImageinner).dontTransform().priority(Priority.LOW).fitCenter().preload(800, 1280);
                                countTotal++;
                            } else if (BannerType.equalsIgnoreCase( "Header")) {
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

                }
                return response;

            } catch (JsonParseException e) {
                e.printStackTrace();
                Log.d("kaveriImage", "1 Ex");
            } catch (JsonGenerationException e) {
                e.printStackTrace();
                Log.d("kaveriImage", "2 Ex");
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("kaveriImage", "3 Ex");
            } catch (JsonMappingException e) {
                e.printStackTrace();
                Log.d("kaveriImage", "4 Ex");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("kaveriImage", "5 Ex");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("kaveriImage", "6 Ex");
            }
            retry = true;
            countInner += 1;
        } while (countInner < 3 && retry);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        if (Constant.isFromBlipBoard) {
//            if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
//                Constant.LocalImageHeaderList = new ArrayList<>();
//                setheaderimg();
//            }
//            if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
//                Constant.LocalImageFooterList = new ArrayList<>();
//                setFooterimg();
//            }
//            if (loading != null && loading.isShowing()) {
//                loading.dismiss();
//            }
//            if(toShowLoader)
//                taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
//        } else {
            if (Constant.ImageDetalList.size() == 0) {
//                ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
//                imagesDetailModel.setBannerType("Home");
//                imagesDetailModel.setId("-1");
//                imagesDetailModel.setImage("dummy1");
//                imagesDetailModel.setImagepath("dummy1");
//                Constant.ImageDetalList.add(imagesDetailModel);
//
//                imagesDetailModel = new ImagesDetailModel();
//                imagesDetailModel.setBannerType("Home");
//                imagesDetailModel.setId("-1");
//                imagesDetailModel.setImage("dummy2");
//                imagesDetailModel.setImagepath("dummy2");
//                Constant.ImageDetalList.add(imagesDetailModel);
//
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
                Constant.Computerperfect.addAll(Constant.ImageDetalList);
//            edited by janvi
                if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
                    Constant.LocalImageHeaderList = new ArrayList<>();
                    setheaderimg();
                }
                if (Constant.ImageFooterList != null && Constant.ImageFooterList.size() > 0) {
                    Constant.LocalImageFooterList = new ArrayList<>();
                    setFooterimg();
                }
//           end
                if (loading != null && loading.isShowing()) {
                    //Log.d("kaveriImage", "Stop loader");
                    loading.dismiss();
                }
                if(toShowLoader)
                    taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);

            } else {
                Log.d("TEST", "" + Constant.ImageDetalList.size());
                Constant.saveImages(context, Constant.ImageDetalList, "Home", storeno);

                Constant.Computerperfect.addAll(Constant.ImageDetalList);

                if (!Constant.isFromBlipBoard) {
                    Constant.LocalImageDetalList = new ArrayList<>();
                    //comes here
                }
                Constant.LocalImageHeaderList = new ArrayList<>();
                Constant.LocalImageFooterList = new ArrayList<>();
                for (int i = 0; i < Constant.ImageDetalList.size(); i++) {
                    ImagesDetailModel imagesDetailModel = Constant.ImageDetalList.get(i);
                    // commented below line for displaying centralized img with imgpath
//                    String urlDetail = Constant.IMG_BASE + Constant.IMG_BANNER_URL + imagesDetailModel.getStoreNo() + "/" + imagesDetailModel.getImage();
                    String urlDetail = imagesDetailModel.getImagepath().toString().trim();
                    //end ******
                    Log.d("TEST", "uRL " + urlDetail);

                    // aaded to prevent app crashes when image=""
                    String imgName = "";
                    String extention = "";

                    if(imagesDetailModel.getImage() != null && !imagesDetailModel.getImage().isEmpty()){
                        String[] split = imagesDetailModel.getImage().split("\\.");
//                    String[] split = imagesDetailModel.getImagepath().toString().split("\\.");
                        imgName = split[0];
                        extention = split[1];
                    }
                    // end **********************

                    // start old working code ************
//                    String[] split = imagesDetailModel.getImage().split("\\.");
////                    String[] split = imagesDetailModel.getImagepath().toString().split("\\.");
//                    String imgName = split[0];
//                    String extention = split[1];
                    // end working code ****************

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
                    // check below for existing images
                    try {
                        if (tempFile.exists()) {
                            ImagesDetailModel model = new ImagesDetailModel();
                            model.setBannerType("Home");
                            model.setImage(tempFile.getAbsolutePath());
                            model.setImagepath(tempFile.getAbsolutePath());
                            Constant.LocalImageDetalList.add(i, model);
                            if (Constant.LocalImageDetalList.size() >= Constant.ImageDetalList.size()) {
                                Constant.saveImages(context, Constant.LocalImageDetalList, "Home", storeno);
                            }
                            //Log.d("kaveriImage", "SAVED IN LOCAL home");
                            saveImage(context, urlDetail, tempFile);
                        } else {
                            if (loading != null && loading.isShowing()) {
                                loading.dismiss();
                            }
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //header start
                if (Constant.ImageHeaderList != null && Constant.ImageHeaderList.size() > 0) {
                    //above if condition added by janvi due to avoid crash
                    // commented below line for displaying centralized img with imgpath
//                    String urlDetailHeader = Constant.IMG_BASE + Constant.IMG_BANNER_URL + Constant.ImageHeaderList.get(0).getStoreNo() + "/" + Constant.ImageHeaderList.get(0).getImage();
                    String urlDetailHeader = Constant.ImageHeaderList.get(0).getImagepath().toString();
                    // end************

                    if (Constant.ImageHeaderList.get(0).getImage() != null && !Constant.ImageHeaderList.get(0).getImage().isEmpty()) {
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
                            Log.d("kaveriImage", "9 Ex");
                            e.printStackTrace();

                        }
                        if (tempFile.exists()) {
                            ImagesDetailModel model = new ImagesDetailModel();
                            model.setBannerType("Header");
                            model.setImage(tempFile.getAbsolutePath());
                            model.setImagepath(tempFile.getAbsolutePath());
                            Constant.LocalImageHeaderList.add(0, model);
                            if (Constant.LocalImageHeaderList.size() >= Constant.ImageHeaderList.size()) {
                                Constant.saveImages(context, Constant.LocalImageHeaderList, "Header", storeno);
                            }
                            //Log.d("kaveriImage", "SAVED IN LOCAL header");
                            saveImage(context, urlDetailHeader, tempFile);
                        } else {
                            if (loading != null && loading.isShowing()) {
                                loading.dismiss();
                            }
                        }
                    }

                }
                //header end
                //footer
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
                            Log.d("kaveriImage", "11 Ex");
                            e.printStackTrace();

                        }

                        ImagesDetailModel model1 = new ImagesDetailModel();
                        model1.setBannerType("Footer");
                        model1.setImage(tempFile1.getAbsolutePath());
                        Constant.LocalImageFooterList.add(0, model1);
                        if (Constant.LocalImageFooterList.size() >= Constant.ImageFooterList.size()) {
                            Constant.saveImages(context, Constant.LocalImageFooterList, "Footer", storeno);
                        }
                        //Log.d("kaveriImage", "SAVED IN LOCAL footer");
                        // commented below line for displaying centralized img with imgpath
//                    String urlDetailFooter = Constant.IMG_BASE + Constant.IMG_BANNER_URL + Constant.ImageFooterList.get(0).getStoreNo() + "/" + Constant.ImageFooterList.get(0).getImage();
                        String urlDetailFooter = Constant.ImageFooterList.get(0).getImagepath().toString();
                        // end
                        saveImage(context, urlDetailFooter, tempFile1);
                    }
                }
//            if (loading != null && loading.isShowing()) {
//                //Log.d("Img", "Stop loader");
//                loading.dismiss();
//            }
            }
//        }
    }

    private void setFooterimg() {
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
                Log.d("Image", "11 Ex");
                e.printStackTrace();

            }

            ImagesDetailModel model1 = new ImagesDetailModel();
            model1.setBannerType("Footer");
            model1.setImage(tempFile1.getAbsolutePath());
            Constant.LocalImageFooterList.add(0, model1);
            if (Constant.LocalImageFooterList.size() >= Constant.ImageFooterList.size()) {
                Constant.saveImages(context, Constant.LocalImageFooterList, "Footer", storeno);
            }
            //Log.d("images", "SAVED IN LOCAL footer");
            // commented below line for displaying centralized img with imgpath
//        String urlDetailFooter = Constant.IMG_BASE + Constant.IMG_BANNER_URL + Constant.ImageFooterList.get(0).getStoreNo() + "/" + Constant.ImageFooterList.get(0).getImage();
            String urlDetailFooter = Constant.ImageFooterList.get(0).getImagepath().toString();
            //end *************
            saveImage(context, urlDetailFooter, tempFile1);
        }
    }

    private void setheaderimg() {
        // commented below line for displaying centralized img with imgpath
//        String urlDetailHeader = Constant.IMG_BASE + Constant.IMG_BANNER_URL + Constant.ImageHeaderList.get(0).getStoreNo() + "/" + Constant.ImageHeaderList.get(0).getImage();

            String urlDetailHeader = Constant.ImageHeaderList.get(0).getImagepath().toString();
            // end
        if (Constant.ImageHeaderList.get(0).getImage() != null && !Constant.ImageHeaderList.get(0).getImage().isEmpty()) {

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
                Log.d("Image", "9 Ex");

            }
            if (tempFile.exists()) {
                ImagesDetailModel model = new ImagesDetailModel();
                model.setBannerType("Header");
                model.setImage(tempFile.getAbsolutePath());
                model.setImagepath(tempFile.getAbsolutePath());
                Constant.LocalImageHeaderList.add(0, model);
                if (Constant.LocalImageHeaderList.size() >= Constant.ImageHeaderList.size()) {
                    Constant.saveImages(context, Constant.LocalImageHeaderList, "Header", storeno);
                }
                //Log.d("images", "SAVED IN LOCAL header");
                saveImage(context, urlDetailHeader, tempFile);
            } else {
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
            }
        }
    }

    public void saveImage(Context mContext, String urlDetail, File tempFile) {
        try {
            Glide.with(mContext)
                    .asBitmap()
                    .timeout(10000)
                    .load(urlDetail)
                    .dontTransform()
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Log.d("kaveriImage", "Url Inner" + urlDetail);
                            if (resource == null) {
                                count++;
                                Log.d("kaveriImage", "" + count + " total " + countTotal);
                                if (count >= countTotal) {
                                    if (loading != null && loading.isShowing()) {
                                        Log.d("kaveriImage", "Stop loader");
                                        loading.dismiss();
                                    }
                                    if(toShowLoader)
                                        taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
                                }
                            } else {
                                write(resource, tempFile);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            count++;
                            Log.d("kaveriImage", "" + count + " total " + countTotal);
                            if (count >= countTotal) {
                                if (loading != null && loading.isShowing()) {
                                    Log.d("kaveriImage", "Stop loader");
                                    loading.dismiss();

                                }
                                if(toShowLoader)
                                    taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            count++;
                            Log.d("kaveriImage", "" + count + " total " + countTotal);
                            if (count >= countTotal) {
                                if (loading != null && loading.isShowing()) {
                                    Log.d("kaveriImage", "Stop loader");
                                    loading.dismiss();

                                }
                                if(toShowLoader)
                                    taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(Bitmap bitmap, File tempFile) {
        OutputStream outStream = null;
        count++;
        try {
            outStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            outStream.close();
            Log.d("kaveriImage", "" + count + " total " + countTotal);
            if (count >= countTotal) {
                if (loading != null && loading.isShowing()) {
                    Log.d("kaveriImage", "Stop loader");
                    loading.dismiss();

                }
                if(toShowLoader)
                    taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("kaveriImage", "13 Ex");
            if (count >= countTotal) {

            if (loading != null && loading.isShowing()) {
                Log.d("kaveriImage", "Stop loader");
                loading.dismiss();
            }
                if (toShowLoader)
                    taskImagesEvent.onGetImagesDetailsResult(Constant.ImageDetalList);
            }
        }

    }

}
