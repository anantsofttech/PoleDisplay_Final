package com.poledisplayapp.Task;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.poledisplayapp.Constant;
import com.poledisplayapp.XMLParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TaskSendmsg extends AsyncTask<String, Void, String> {

    XMLParser xmlparser;
    List<NameValuePair> nameValuePairList;
    String response;
    private Context mContext;
    TaskSendmsgEvent taskSendmsgEvent;

    public TaskSendmsg(TaskSendmsgEvent taskSendmsgEvent, Context mContext) {
        this.taskSendmsgEvent=taskSendmsgEvent;
        this.mContext=mContext;
    }

    public interface TaskSendmsgEvent {
        void onSendmsgResult(String s);
    }

    /**
     * @param params
     * @deprecated
     */
    @Override
    protected String doInBackground(String... params) {

        xmlparser = new XMLParser();

// Create BasicNameValuePair for each parameter
        BasicNameValuePair storeNoPair = new BasicNameValuePair("storeNo", params[0]); // Make7
        BasicNameValuePair stationNoPair = new BasicNameValuePair("stationNo", params[1]); // Assuming params[1] is the stationNo
        BasicNameValuePair resultPair = new BasicNameValuePair("Result", params[2]); // Assuming params[2] is the Result

        nameValuePairList = new ArrayList<NameValuePair>();

// Add all parameters to the list
        nameValuePairList.add(storeNoPair);
        nameValuePairList.add(stationNoPair);
        nameValuePairList.add(resultPair);

        Log.d("TEST", "url : " + Constant.LighningURL + Constant.WS_send_message_for_store_login);
        Log.d("TEST", "PARAMS : " + nameValuePairList);

        response = xmlparser.getXmlFromUrl(Constant.LighningURL + Constant.WS_send_message_for_store_login, nameValuePairList);

        Log.d("TEST", "HERE RESPONSE FROM LOGIN IS : " + response);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            // Assuming 'response' is the JSON string you received
            JSONObject jsonResponse = new JSONObject(response);

            // Extract the 'result' value from the JSON object
            String resultValue = jsonResponse.getString("result");

            // Now you can use 'resultValue' as needed, such as displaying it in a TextView
            // For example, if you have a TextView named 'resultTextView':
            // resultTextView.setText(resultValue);

            Log.d("TEST", "Parsed Result: " + resultValue);

            if (taskSendmsgEvent!=null){
                if (!resultValue.equals("") || !resultValue.isEmpty()){
                    taskSendmsgEvent.onSendmsgResult(resultValue);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TEST", "Error parsing JSON response");
        }
    }


}
