package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

/**
 * Created by fci on 05/03/17.
 */

public class fragMoney extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<money_noti_item> moneyNotiItems;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_b,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.frag_b_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        moneyNotiItems = new ArrayList<>();

//        connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        assert connManager != null;
//        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//        if (isNetworkConnected()) {
            LoadRecyclerViewData();
//        }else {
//            AlertDialog.Builder builder;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
//            } else {
//                builder = new AlertDialog.Builder(getActivity());
//            }
//            builder.setTitle("Error Message!")
//                    .setMessage("Make Sure You Are Connected To Wifi!")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // continue with delete
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }

        return v;
    }
//    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null;
//    }

    private void LoadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Posts ...");
        progressDialog.show();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://gradproject2018.000webhostapp.com/Donation%20System/GetMoneyNotification.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("money_noti_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                money_noti_item blood_noti = new money_noti_item(
                                        jsonObject1.getString("date"),
                                        jsonObject1.getString("content"),
                                        jsonObject1.getString("Name")
                                );
                                moneyNotiItems.add(blood_noti);
                            }
                            adapter = new Money_Adapter(moneyNotiItems, getActivity());
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "Server Error!", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "Bad Network!", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "Connection Timeout!", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest1);
    }
//    private void LoadRecyclerViewData() {
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading Posts ...");
//        progressDialog.show();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://gradproject2018.000webhostapp.com/")
//                .addConverterFactory( GsonConverterFactory.create())
//                .build();
//        fragMoney.moneyAPI moneyAPI = retrofit.create( fragMoney.moneyAPI.class);
//        Call<fragMoney.ResultModel> connection = moneyAPI.getposts();
//        connection.enqueue( new Callback<fragMoney.ResultModel>() {
//            @Override
//            public void onResponse(Call<fragMoney.ResultModel> call, retrofit2.Response<fragMoney.ResultModel> response) {
//                List<money_noti_item> item = response.body().getMoney_noti_data();
//                for (int i=0; i<item.size();i++){
//                    money_noti_item money_noti = new money_noti_item(
//                            item.get(i).getTime(),
//                            item.get(i).getBody(),
//                            item.get(i).getName()
//                    );
//                    moneyNotiItems.add( money_noti );
//                }
//                adapter = new Money_Adapter(moneyNotiItems,getActivity());
//                recyclerView.setAdapter(adapter);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<fragMoney.ResultModel> call, Throwable t) {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(getActivity());
//                }
//                builder.setTitle("Error Message!")
//                        .setMessage("Make Sure You Have Strong Wifi Signal!")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        } );
//
//    }
//    public interface moneyAPI{
//        @POST("Donation System/GetMoneyNotification.php")
//        Call<fragMoney.ResultModel> getposts();
//    }
//    public class ResultModel{
//        private List<money_noti_item> money_noti_data;
//
//        public List<money_noti_item> moneyNotiItems() {
//            return money_noti_data;
//        }
//
//        public void setMoney_noti_data(List<money_noti_item> money_noti_data) {
//            this.money_noti_data = money_noti_data;
//        }
//
//        public List<money_noti_item> getMoney_noti_data() {
//            return money_noti_data;
//        }
//    }

}
