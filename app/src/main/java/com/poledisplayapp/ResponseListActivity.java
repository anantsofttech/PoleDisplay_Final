package com.poledisplayapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poledisplayapp.Api.ApiClient;
import com.poledisplayapp.Api.ApiClientBillBoard;
import com.poledisplayapp.Api.ApiInterface;
import com.poledisplayapp.adapter.ResponseListAdapter;
import com.poledisplayapp.models.AdvRefreshCallsResponse;

import poledisplayapp.R;
import poledisplayapp.databinding.ActivityResponseListBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseListActivity extends AppCompatActivity {

    ActivityResponseListBinding binding;
    ApiInterface apiForCall;
    ResponseListActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_response_list);
        getSupportActionBar().hide();
        apiForCall = ApiClient.getClient().create(ApiInterface.class);
        context = ResponseListActivity.this;

        callLogsListAPI(getIntent().getExtras().getString("androidID"));
        binding.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        binding.btnrefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Constant.isFromBlipBoard) {
//                    if (Constant.getTimeToRefresh(ResponseListActivity.this, false)) {
//                        if(ApiClientBillBoard.signId!=null) {
//                            if(ApiClientBillBoard.signId.trim().length()>0) {
////                                MainActivity.context.fetchAdvFromBlip();
//                            }
//                        }
//
//                        new Handler().postDelayed(() -> {
//                            callLogsListAPI(getIntent().getExtras().getString("androidID"));
//                        }, 4000);
//
//                    } else {
//                        try {
//                            Double timeis = Double.parseDouble(String.valueOf(Constant.getTimeToDisplay(ResponseListActivity.this)));
//                            Toast.makeText(ResponseListActivity.this, "Refresh already requested, try after " + (timeis) + " minutes! ", Toast.LENGTH_LONG).show();
//                        } catch (Exception E) {
//                            Toast.makeText(ResponseListActivity.this, "Refresh already requested, try after few minutes!", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//            }
//        });
    }

    private void callLogsListAPI(String androidID) {
        Constant.showProgressBar(ResponseListActivity.this);
        Call<AdvRefreshCallsResponse> call1 = apiForCall.getResponseLogs(Constant.STOREID, androidID);
        call1.enqueue(new Callback<AdvRefreshCallsResponse>() {
            @Override
            public void onResponse(Call<AdvRefreshCallsResponse> call, Response<AdvRefreshCallsResponse> response) {
                if (response.body() != null) {
                    if (response.body().getAdvRefreshCalls() != null) {
                        if (response.body().getAdvRefreshCalls().size() > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ResponseListAdapter adapter = new ResponseListAdapter(context, response.body().getAdvRefreshCalls());
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                    binding.rvItemList.setLayoutManager(layoutManager);
                                    binding.rvItemList.setAdapter(adapter);
                                    binding.rvItemList.setNestedScrollingEnabled(false);
                                    binding.rvItemList.setVisibility(View.VISIBLE);
                                    Constant.dismissProgressBar();
                                }
                            });
                        } else {
                            Constant.dismissProgressBar();
                        }
                    } else {
                        Constant.dismissProgressBar();
                    }
                } else {
                    Constant.dismissProgressBar();
                }

            }

            @Override
            public void onFailure(Call<AdvRefreshCallsResponse> call, Throwable t) {
                call.cancel();
                Constant.dismissProgressBar();

            }
        });
    }
}