package com.poledisplayapp.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poledisplayapp.NetworkUtil;
import com.poledisplayapp.models.PlayModel;

import java.util.ArrayList;
import java.util.List;

public class TaskPlay extends AsyncTask<String, String, PlayModel> {

    TaskPlayEvent taskPlayEvent;
    Context context;
    PlayModel playModel;
    private String authToken;
    String response;;
    String scontext;;
    String ts;;
    double min_duration;



    public TaskPlay(String scontext, Context context, Context context1, String access_token, String ts, double min_duration) {

        this.context=context1;
        this.scontext=scontext;
        this.authToken=access_token;
        this.taskPlayEvent= (TaskPlayEvent) context;
        this.ts= ts ;
        this.min_duration=min_duration;
    }

    public interface TaskPlayEvent{
        void onGetplay(Object o, double min_duration);
    }



    @Override
    protected PlayModel doInBackground(String... params) {
        String url = params[0];
        // Append the parameters to the URL
        url += "?context="+scontext+"&duration="+min_duration+"&start="+ts;

        Log.i("web service--GetAdunit", "request url : " + url);

        response = NetworkUtil.performNetworkRequest(url, authToken);

        Log.i("web service--GetAdunit", "response: " + response);

        // Parse the JSON response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//        try {
//            // Deserialize the response into a List<AdUnit>
//            playModel = (PlayModel) objectMapper.readValue(response, new TypeReference<List<PlayModel>>() {
//            });
//            return playModel;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    protected void onPostExecute(PlayModel s) {
        super.onPostExecute(s);

        if (s!=null) {
            if (taskPlayEvent != null && taskPlayEvent != null) {
                // Convert the single object into a list
                List<PlayModel> resultList = new ArrayList<>();
                resultList.add(playModel);

                taskPlayEvent.onGetplay(resultList,min_duration);
            }
        }else {
            // Handle the case when the response is null
            taskPlayEvent.onGetplay(null,min_duration);
        }

    }

}
