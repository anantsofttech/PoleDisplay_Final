package com.poledisplayapp.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.poledisplayapp.Constant;
import com.poledisplayapp.XMLParser;
import com.poledisplayapp.models.StationModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import poledisplayapp.R;

public class TaskGetStation extends AsyncTask<String, Void, String> {

    XMLParser xmlparser;
    List<NameValuePair> nameValuePairList;
    String response;
//    ProgressDialog progressBar;
    private Context mContext;

    ProgressDialog loading = null;

    TaskGetStationEvent taskGetStationEvent;

    public TaskGetStation(TaskGetStationEvent taskGetStationEvent, Context context) {
        this.taskGetStationEvent = taskGetStationEvent;
        this.mContext = context;
    }

    public interface TaskGetStationEvent {
        void onGetStationDetailsResult(List<StationModel> stationModelList, String s);
    }

    @Override
    protected String doInBackground(String... params) {

        xmlparser = new XMLParser();
        BasicNameValuePair BasicNameValuePair0 = new BasicNameValuePair(
                "StoreNo", params[0]); // Make7

        nameValuePairList = new ArrayList<NameValuePair>();

        nameValuePairList.add(BasicNameValuePair0);

       Log.d("TEST", "url : " + Constant.LighningURL
                + Constant.WS_ANDROID_TABLET_VALIDATION);

       Log.d("TEST", "PARAMS : " + nameValuePairList);
        response = xmlparser.getXmlFromUrl(Constant.LighningURL
                + Constant.WS_ANDROID_TABLET_VALIDATION, nameValuePairList);

       Log.d("TEST", "HERE RESPONSE FROM LOGIN IS : " + response);

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar = new ProgressDialog(mContext);
//        progressBar.setCancelable(false);
////        progressBar.setMessage("Please Wait...");
//        progressBar.show();
        // start progressbar


        if(mContext != null) {
            loading = new ProgressDialog(mContext, R.style.MyprogressDTheme);
            loading.setCancelable(false);
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.show();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (response.length() > 0) {

            StationModel stationModel;
//
            Document doc = xmlparser.getDomElement(response); // getting DOM
//            // element

            Constant.stationList.clear();

            try{

            NodeList nl = doc.getElementsByTagName("Outermodule");

                // test it how data it gives us
                for (int i = 0; i < nl.getLength(); i++) {
//                // creating new HashMap
//
                    Element e = (Element) nl.item(i);

                    stationModel = new StationModel();

                    stationModel.setDisplayType(xmlparser.getValue(e, "DisplayType"));

                    stationModel.setStation_No(xmlparser.getValue(e, "Station_No"));

                    stationModel.setApp_name(xmlparser.getValue(e,"App_NickName"));

                    stationModel.setPoleDisplay(xmlparser.getValue(e,"PoleDisplay"));

                    stationModel.setStore_nickname(xmlparser.getValue(e,"StoreNick"));

                    stationModel.setIndustryType(xmlparser.getValue(e,"IndustryType"));

                    stationModel.setOpenTime(xmlparser.getValue(e,"OpenTime"));

                    stationModel.setCloseTime(xmlparser.getValue(e,"CloseTime"));
//
                    Constant.stationList.add(stationModel);

                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }


            String msg = "";// element

            try {

                NodeList n2 = doc.getElementsByTagName("Records");
                for (int i = 0; i < n2.getLength(); i++) {
                    // creating new HashMap

                    Element e = (Element) n2.item(i);
                    // adding each child node to HashMap key => value

                    msg = xmlparser.getValue(e, "Message");
                    //Log.d("msg","msg:" +msg);

                }
            }catch(NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            if(loading != null && loading.isShowing()){
                loading.dismiss();
            }

            if(taskGetStationEvent != null){

                if(Constant.stationList != null && Constant.stationList.size() == 0){

                    taskGetStationEvent.onGetStationDetailsResult(Constant.stationList,msg);
                }else{
                    taskGetStationEvent.onGetStationDetailsResult(Constant.stationList,"");
                }

            }

        }
        else {
            if(loading != null && loading.isShowing()){
                loading.dismiss();
            }
            taskGetStationEvent.onGetStationDetailsResult(new ArrayList<>(),"");
        }

    }

}
