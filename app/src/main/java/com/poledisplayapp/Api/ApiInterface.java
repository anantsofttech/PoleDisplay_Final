package com.poledisplayapp.Api;

import com.poledisplayapp.models.AdvRefreshCallsResponse;
import com.poledisplayapp.models.AdvSign;
import com.poledisplayapp.models.Auth_QTModel;
import com.poledisplayapp.models.Result;
import com.poledisplayapp.models.Sign;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //blip apis
    @POST("signs")
    Call<Sign> CreateSigns(@Header("Authorization") String authHeader, @Body RequestBody str);

    @PATCH("signs/{sign_id}")
    Call<Sign> updateSign(@Header("Authorization") String authHeader, @Path("sign_id") String sign_id,@Body RequestBody str);

    @GET("sign/{sign_id}/images")
    Call<ArrayList<AdvSign>> signSignIdImagesGet(@Path("sign_id") String sign_id, @Header("Authorization") String authHeader);

    @POST("sign/{sign_id}/schedule")
    Call<ArrayList<AdvSign>> signSignIdImagesGetschedule(@Path("sign_id") String sign_id, @Header("Authorization") String authHeader, @Body RequestBody str);

    @POST("sign/{sign_id}/flips")
    Call<Result> recordFlips(@Path("sign_id") String sign_id, @Header("Authorization") String authHeader, @Body RequestBody str);

    @GET("signs")
    Call<ArrayList<Sign>> GetSigns(@Header("Authorization") String authHeader);

    //our server apis
    @FormUrlEncoded
    @POST("PoleDisplayErrorLogSave")
    Call<Result> SaveRequestResponse_Log(@Field("Storeno") String StoreNo, @Field("strRequestXML") String strRequestXML, @Field("strResponseXML") String strResponseXML, @Field("DeviceID") String deviceID, @Field("SignID") String Signid, @Field("ErrorMessage") String ErrorMessage);

    @GET("GetPoleDisplayErrorLog")
    Call<AdvRefreshCallsResponse> getResponseLogs(@Query("Storeno") String Storeno, @Query("DeviceID") String DeviceID);

//  QT Apis
    @POST("token")
    Call<Auth_QTModel> AUTH_QT_CALL(@Body RequestBody str);

}