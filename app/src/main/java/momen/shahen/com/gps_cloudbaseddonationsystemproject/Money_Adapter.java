package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by fci on 16/01/18.
 */

public class Money_Adapter extends RecyclerView.Adapter<Money_Adapter.ViewHolder> {
    private List<money_noti_item> money_noti_it;
    private Context context;
    byte[] paramtersbyt;
    String connectionparamters;
    String url;
    String datekey;
    String Lang, Lat, hos_name;

    public Money_Adapter(List<money_noti_item> blood_noti_it, Context context) {
        this.money_noti_it = blood_noti_it;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.money_nto,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final money_noti_item money_noti = money_noti_it.get(position);

        hos_name = money_noti.getName();
        holder.tim.setText(money_noti.getTime());
        holder.nam.setText(money_noti.getName());
        holder.body.setText(money_noti.getBody());
        holder.don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donate(money_noti.getTime());

            }
        });
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hos_name = money_noti.getName();
                LoadLocationData(hos_name);
                /*if (Lang!=null && Lat!=null) {
                    new MapsActivity(money_noti.getName(), Lang, Lat);
                }else {
                    Toast.makeText(context, "There is no Lng or Lat", Toast.LENGTH_LONG).show();
                }
                */
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = money_noti.getName()+" announce that \n"
                        +money_noti.getBody()+"\n"
                        +"Post Time: "+money_noti.getTime();
                String Subject = "Money Notification From Donation System";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return money_noti_it.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tim, body, nam;
        Button don, map, share;
        public ViewHolder(View itemView) {
            super(itemView);
            tim = (TextView)itemView.findViewById(R.id.mon_tim_blood);
            body = (TextView)itemView.findViewById(R.id.mon_body_blood);
            nam = (TextView)itemView.findViewById(R.id.mon_nam_blood);
            don = (Button)itemView.findViewById(R.id.money_donate);
            map = (Button)itemView.findViewById(R.id.money_map);
            share = (Button)itemView.findViewById(R.id.money_share);
        }
    }
    private void LoadLocationData(final String name) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://momenshaheen.16mb.com/ShowMap.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(response,"UTF-8");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("location_data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                Lang = object.getString("Lang");
                                Lat = object.getString("Lat");
                                if (Lang!=null && Lat!=null) {
                                    Intent intent = new Intent(context,MapsActivity.class);
                                    intent.putExtra("Lang",Lang);
                                    intent.putExtra("name",name);
                                    intent.putExtra("Lat",Lat);
                                    context.startActivity(intent);
                                    //Toast.makeText(context, lg+"\n"+lt, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(context, "There is no Lng or Lat", Toast.LENGTH_LONG).show();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("name",hos_name);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void donate(final String date) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://momenshaheen.16mb.com/UpdateNotificationRate.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(response,"UTF-8");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("date",date);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
