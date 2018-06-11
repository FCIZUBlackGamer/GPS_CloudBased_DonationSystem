package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

/**
 * Created by fci on 05/03/17.
 */

public class fragA extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<blood_noti_item> bloodNotiItem;

    Button button;

    ConnectivityManager connManager;
    NetworkInfo mWifi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_a, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.frag_a_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        bloodNotiItem = new ArrayList<>();
        connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (isNetworkConnected()) {
            LoadRecyclerViewData();
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }
            builder.setTitle("Error Message!")
                    .setMessage("Make Sure You Are Connected To Wifi!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return v;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void LoadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Posts ...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://momenshaheen.16mb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fragA.bloodAPI bloodAPI = retrofit.create(fragA.bloodAPI.class);
        Call<fragA.ResultModel> connection = bloodAPI.getposts();
        connection.enqueue(new Callback<fragA.ResultModel>() {
            @Override
            public void onResponse(Call<fragA.ResultModel> call, retrofit2.Response<fragA.ResultModel> response) {
                List<blood_noti_item> item = response.body().getBlood_noti_items();
                for (int i = 0; i < item.size(); i++) {
                    blood_noti_item blood_noti = new blood_noti_item(
                            item.get(i).getTime(),
                            item.get(i).getBody(),
                            item.get(i).getDocName(),
                            item.get(i).getState(),
                            item.get(i).getHosname()
                    );
                    bloodNotiItem.add(blood_noti);
                }
                adapter = new Blood_Adapter(bloodNotiItem, getActivity());
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<fragA.ResultModel> call, Throwable t) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("Error Message!")
                        .setMessage("Make Sure You Have Strong Wifi Signal!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    public interface bloodAPI {
        @POST("GetBloodNotification.php")
        Call<fragA.ResultModel> getposts();
    }

    public class ResultModel {
        private List<blood_noti_item> blood_noti_data;

        public List<blood_noti_item> bloodNotiItems() {
            return blood_noti_data;
        }

        public void setBlood_noti_items(List<blood_noti_item> blood_noti) {
            this.blood_noti_data = blood_noti;
        }

        public List<blood_noti_item> getBlood_noti_items() {
            return blood_noti_data;
        }
    }

}
