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

import com.android.volley.AuthFailureError;
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
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

/**
 * Created by fci on 05/03/17.
 */

public class fragC extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<point_noti_item> pointNotiItems;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_c,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.frag_c_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pointNotiItems = new ArrayList<>();

        connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (isNetworkConnected()) {
            LoadRecyclerViewData();
        }else {
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

        adapter = new Point_Adapter(pointNotiItems,getActivity());
        recyclerView.setAdapter(adapter);

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
        pointAPI pointAPI = retrofit.create( fragC.pointAPI.class);
        Call<ResultModel> connection = pointAPI.getposts();
        connection.enqueue( new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, retrofit2.Response<ResultModel> response) {

                List<point_noti_item> item = response.body().getPointNotiListItems();
                for (int i=0; i<item.size();i++){
                    point_noti_item point_noti_items = new point_noti_item(
                    item.get(i).getTime(),
                    item.get(i).getBody(),
                    item.get(i).getSpon(),
                    item.get(i).getName()

                    );
                    pointNotiItems.add( point_noti_items );
                }
                adapter = new Point_Adapter(pointNotiItems,getActivity());
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
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
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        } );

    }
    public interface pointAPI{
        @POST("GetPointNotification.php")
        Call<ResultModel> getposts();
    }
    public class ResultModel{
        private List<point_noti_item> point_noti_data;

        public List<point_noti_item> pointNotiListItems() {
            return point_noti_data;
        }

        public void setPointNotiItems(List<point_noti_item> pointNotiListItems) {
            this.point_noti_data = pointNotiListItems;
        }

        public List<point_noti_item> getPointNotiListItems() {
            return point_noti_data;
        }
    }
}
