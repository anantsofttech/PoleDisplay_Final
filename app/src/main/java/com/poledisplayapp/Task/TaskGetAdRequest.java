package com.poledisplayapp.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poledisplayapp.Constant;
import com.poledisplayapp.NetworkUtil;
import com.poledisplayapp.models.GetAdRequestModel;
import com.poledisplayapp.models.ImagesDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class TaskGetAdRequest extends AsyncTask<String, Void, GetAdRequestModel> {

        TaskGetAdRequestEvent taskGetAdRequestEvent;
        Context context;
        GetAdRequestModel getAdRequestModel;
        ProgressDialog loading = null;
        private String authToken;
        int countTotal =0;
        String response;;
        String ad_unit_name;;

    String imageUrl;
    String imagemime;

    public TaskGetAdRequest(Context splashActivity, Context context, String access_token, String name) {
        this.taskGetAdRequestEvent = (TaskGetAdRequestEvent) splashActivity;
        this.context = context;
        this.authToken=access_token;
        this.ad_unit_name = name;
    }

    public interface TaskGetAdRequestEvent{
            void onGetAdRequestResult(List<GetAdRequestModel> getAdRequestModel, String ad_unit_name);
        }


    @Override
    protected GetAdRequestModel doInBackground(String... strings) {
        Log.i("web service--GetAdunit", "request url : " + strings[0]);

        response = NetworkUtil.performNetworkRequest(strings[0], authToken);

        //        Edited by varun
        //        Added this code for only to take 1920*1080 size image to display.

        try {
            if (response != null && !response.isEmpty()) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray snapshotsArray = jsonObject.getJSONObject("creative").getJSONArray("snapshots");

                JSONObject desiredSnapshot = null;
                Constant.imageMime="";
                Constant.imageURL="";

                if (snapshotsArray != null && snapshotsArray.length() >= 0) {

                    for (int i = 0; i < snapshotsArray.length(); i++) {
                        JSONObject snapshot = snapshotsArray.getJSONObject(i);
                        double scalingFactor = snapshot.getDouble("scaling_factor");
                        int height = snapshot.getInt("h");
                        int width = snapshot.getInt("w");
                        String mime = snapshot.getString("mime");

//                    if (height == 1920 && width == 1080 && mime.contains("jpeg")) {
//                        desiredSnapshot = snapshot;
//                        Log.e("", "taken:1");
//                        break;
//                    }
                        if (height == Constant.adunit_height && width == Constant.adunit_width && mime.contains("jpeg")) {
                            Log.e("", "desired snapshot: from comparing width and height" );
                            desiredSnapshot = snapshot;
                            break;
                        } else if (scalingFactor == 1.0 && mime.equals("image/jpeg")) {
                            Log.e("", "desired snapshot: from comparing Scaling factor" );
                            desiredSnapshot = snapshot;
                            break;
                        }
//                        else{
//                            Log.e("", "desired snapshot: from end" );
//                            desiredSnapshot=snapshot;
//                            break;
//                        }
                    }

                }

                if (desiredSnapshot != null) {
                    if (desiredSnapshot.has("iurl")) {
                        imageUrl = desiredSnapshot.getString("iurl");
                        Log.e("", "doInBackground: 123 " + imageUrl);
                    } else if (desiredSnapshot.has("curl")) {
                        imageUrl = desiredSnapshot.getString("curl");
                    }
                    imagemime = desiredSnapshot.getString("mime");

                    Constant.imageURL = imageUrl;
                    Constant.imageMime = imagemime;

                }
//                else {
////                    This is called when the desired snapshot will not be there
//                    Log.e("", "Doing repsonse null because desired snap shot is not found ");
//                    response = "";
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //        END

        Log.i("web service--GetAdunit", "response: " + response);

        // Parse the JSON response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            if (response != null && !response.isEmpty()) {
                getAdRequestModel = objectMapper.readValue(response, GetAdRequestModel.class);
            } else {
                // Handle the case when the response is null
                getAdRequestModel = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getAdRequestModel;
    }


    @Override
    protected void onPostExecute(GetAdRequestModel s) {
        super.onPostExecute(s);

        if (s!=null) {
            ImagesDetailModel imagesDetailModel = new ImagesDetailModel();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imagesDetailModel.setImagepath(imageUrl);
            } else {
                imagesDetailModel.setImagepath(imageUrl);
            }
            imagesDetailModel.setImage(imagemime);
//        Constant.ImageDetalList2.clear();
            Constant.ImageDetalList2.add(imagesDetailModel);
            Constant.ImageDetalList3.add(imagesDetailModel);
            String urlImageinner = "";
            if (imageUrl != null && !imageUrl.isEmpty()) {
                urlImageinner = imageUrl;
            } else {
                urlImageinner = imageUrl;
            }
            try {
                Glide.with(context).asDrawable().load(urlImageinner).dontTransform().priority(Priority.LOW).fitCenter().preload(800, 1280);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Constant.saveImages(context, Constant.ImageDetalList2, "QT", Constant.STOREID);

            Constant.LocalImageDetalList2 = new ArrayList<>();

            if (!Constant.LocalImageDetalList2.isEmpty() && Constant.LocalImageDetalList2!=null){
                Constant.LocalImageDetalList2.clear();
            }

            for (int j = 0; j < Constant.ImageDetalList2.size(); j++) {
                ImagesDetailModel imagesDetailModel1 = Constant.ImageDetalList2.get(j);
                // commented below line for displaying centralized img with imgpath
//                    String urlDetail = Constant.IMG_BASE + Constant.IMG_BANNER_URL + imagesDetailModel.getStoreNo() + "/" + imagesDetailModel.getImage();
                String urlDetail = imagesDetailModel1.getImagepath().toString().trim();

                // aaded to prevent app crashes when image=""
                String imgName = "";
                String extention = "";

                if (imagesDetailModel1.getImage() != null && !imagesDetailModel1.getImage().isEmpty()) {
                    String[] split = imagesDetailModel1.getImage().split("/");
//                    String[] split = imagesDetailModel.getImagepath().toString().split("\\.");
                    imgName = split[0];
                    extention = split[1];
                }
                // end **********************

                File tempFile = null;
                File sdcard = null;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    //Do something
                    sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                } else {
                    sdcard = Environment.getExternalStorageDirectory();
                }
                tempFile = new File(sdcard + "/NewQT/" + j + "_" +
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
                        tempFile = new File(sdcard + "/NewQT/" + j + "_" +
                                imgName +
                                "." + extention);
                        tempFile.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                if (tempFile.exists()) {
                    ImagesDetailModel model = new ImagesDetailModel();
                    model.setBannerType("Home");
                    model.setImage(tempFile.getAbsolutePath());
                    model.setImagepath(tempFile.getAbsolutePath());
//                        Constant.LocalImageDetalList.add(i, model); //crashed here
                    Constant.LocalImageDetalList2.add(model); //edited by janvi on 03/31/2023

                    if (Constant.LocalImageDetalList2.size() >= Constant.ImageDetalList2.size()) {
                        Constant.saveImages(context, Constant.LocalImageDetalList2, "QT", Constant.STOREID);
                    }
                    //Log.d("kaveriImage", "SAVED IN LOCAL home");
                    saveImage(context, urlDetail, tempFile);
                }
            }

            if (taskGetAdRequestEvent != null && taskGetAdRequestEvent != null) {
                // Convert the single object into a list
                List<GetAdRequestModel> resultList = new ArrayList<>();
                resultList.add(getAdRequestModel);

                taskGetAdRequestEvent.onGetAdRequestResult(resultList,ad_unit_name);
            }
        }else {
            // Handle the case when the response is null
            taskGetAdRequestEvent.onGetAdRequestResult(null,ad_unit_name);
        }

    }

    public void saveImage(Context mContext, String urlDetail, File tempFile) {
        if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(urlDetail)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            write(resource, tempFile);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                        }
                    });
        }
    }

    public void write(Bitmap bitmap, File tempFile) {
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            outStream.close();

            Constant.ImageDetalList2 = new ArrayList<>();
            Constant.ImageDetalList2.addAll(Constant.LocalImageDetalList2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
