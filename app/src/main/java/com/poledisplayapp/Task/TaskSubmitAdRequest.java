package com.poledisplayapp.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poledisplayapp.NetworkUtil;
import com.poledisplayapp.models.SubmitAdRequestModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskSubmitAdRequest extends AsyncTask<String, Void, SubmitAdRequestModel> {

    TaskSubmitAdRequestEvent taskSubmitAdRequestEvent;
    Context context;
    SubmitAdRequestModel submitAdRequest;
    private String authToken;

    public TaskSubmitAdRequest(Context context1, Context context, String access_token) {
        this.taskSubmitAdRequestEvent = (TaskSubmitAdRequestEvent) context1;
        this.context = context;
        this.authToken = access_token;
    }

    public interface TaskSubmitAdRequestEvent {
        void onSubmitAdRequstResult(List<SubmitAdRequestModel> submitAdRequest);
    }

    @Override
    protected SubmitAdRequestModel doInBackground(String... strings) {

        Log.i("web service--GetAdunit", "request url : " + strings[0]);

        String response = NetworkUtil.performNetworkRequestforPost(strings[0], authToken);

        Log.i("web service--GetAdunit", "response: " + response);

        // Parse the JSON response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // Deserialize the response into a SubmitAdRequest object
            if (response != null && !response.isEmpty()) {
                submitAdRequest = objectMapper.readValue(response, SubmitAdRequestModel.class);
            }
            return submitAdRequest;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return submitAdRequest;
    }



    @Override
    protected void onPostExecute(SubmitAdRequestModel s) {
        super.onPostExecute(s);

        if (taskSubmitAdRequestEvent != null && submitAdRequest != null) {
            // Convert the single object into a list
            List<SubmitAdRequestModel> resultList = new ArrayList<>();
            resultList.add(submitAdRequest);

            taskSubmitAdRequestEvent.onSubmitAdRequstResult(resultList);
        }
    }

}
