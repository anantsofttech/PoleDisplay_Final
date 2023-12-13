package com.poledisplayapp.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poledisplayapp.NetworkUtil;
import com.poledisplayapp.models.AdApprovalModel;
import com.poledisplayapp.models.RetrieveAdunitModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskAdApprovals extends AsyncTask<String, Void, List<AdApprovalModel>> {

    private TaskAdApprovalsEvent taskAdApprovalsEvent;
    private String authToken;
    AdApprovalModel adApprovalModel;

    public TaskAdApprovals(Context context, String access_token) {
        this.taskAdApprovalsEvent = (TaskAdApprovalsEvent) context;
        this.authToken = access_token;
    }

    public interface TaskAdApprovalsEvent {
        void onAdApprovals(List<AdApprovalModel> adApprovalModels);
    }

    @Override
    protected List<AdApprovalModel> doInBackground(String... strings) {
        Log.i("web service--GetAdunit", "request url : " + strings[0]);

        String response = performNetworkRequest(strings[0], authToken);

        Log.i("web service--GetAdunit", "response: " + response);

        // Parse the JSON response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<AdApprovalModel> resultList = new ArrayList<>();

        try {
            if (response == null || response.isEmpty()) {
                return Collections.emptyList();
            } else {
                JsonNode rootNode = objectMapper.readTree(response);
                if (rootNode.isArray()) {
                    // Deserialize as a list of AdApprovalModel objects
                    return objectMapper.readValue(response, new TypeReference<List<AdApprovalModel>>() {});
                } else {
                    // Deserialize as a single AdApprovalModel object
                    adApprovalModel = objectMapper.readValue(response, AdApprovalModel.class);
                    return Collections.singletonList(adApprovalModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;

    }

    private String performNetworkRequest(String string, String authToken) {

        String response = null;

        try {
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method and headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);


            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                response = stringBuilder.toString();

                // Close the streams
                bufferedReader.close();
                inputStream.close();
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(List<AdApprovalModel> adApprovalModels) {
        super.onPostExecute(adApprovalModels);

        if (taskAdApprovalsEvent != null && adApprovalModels != null) {
            taskAdApprovalsEvent.onAdApprovals(adApprovalModels);
        }
    }
}