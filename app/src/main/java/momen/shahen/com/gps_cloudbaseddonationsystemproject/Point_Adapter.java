package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class Point_Adapter extends RecyclerView.Adapter<Point_Adapter.ViewHolder> {
    private List<point_noti_item> pointNotiItems;
    private Context context;
//    byte[] paramtersbyt;
//    String connectionparamters;
    String url;
//    String datekey;
    String Lang, Lat, hos_name;
    Database database;
    Cursor cursor;

    public Point_Adapter(List<point_noti_item> point_noti_items, Context context) {
        this.pointNotiItems = point_noti_items;
        this.context = context;
        database = new Database(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announce_point_nto,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final point_noti_item noti_item = pointNotiItems.get(position);


        holder.tim.setText(noti_item.getTime());
        holder.nam.setText(noti_item.getName());
        holder.body.setText(noti_item.getBody());
//        holder.spon.setText(noti_item.getSpon());
//        holder.don.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                url = "http://momenshaheen.16mb.com/UpdateNotificationRate.php";
//                datekey = "date=";
//                try {
//                    connectionparamters = datekey + URLEncoder.encode(noti_item.getTime(), "UTF-8");
//
//                    paramtersbyt = connectionparamters.getBytes("UTF-8");
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                Runnable runnable = new Runnable() {
//                    public void run() {
//                        try {
//
//                            URL insertUserUrl = new URL(url);
//                            HttpURLConnection insertConnection = (HttpURLConnection) insertUserUrl.openConnection();
//                            insertConnection.setRequestMethod("POST");
//                            insertConnection.getOutputStream().write(paramtersbyt);
//                            InputStreamReader resultStreamReader = new InputStreamReader(insertConnection.getInputStream());
//                            BufferedReader resultReader = new BufferedReader(resultStreamReader);
//                            final String result = resultReader.readLine();
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();                                }
//                            });
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//
//                Thread thread = new Thread(runnable);
//                thread.start();
//
//            }
//        });
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hos_name = noti_item.getName();
                LoadLocationData(hos_name);
                /*if (Lang!=null && Lat!=null) {
                    new MapsActivity(noti_item.getName(), Lang, Lat);
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
                String shareBody = noti_item.getName()+" announce that \n"
                        +noti_item.getBody()+"\n"
                        +"Post Time: "+noti_item.getTime();
                String Subject = "Point Notification From Donation System";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pointNotiItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tim, body, nam;
        Button map, share;
        public ViewHolder(View itemView) {
            super(itemView);
            tim = (TextView)itemView.findViewById(R.id.point_tim);
            body = (TextView)itemView.findViewById(R.id.point_bod);
            nam = (TextView)itemView.findViewById(R.id.point_hos_nam);
//            spon = (TextView)itemView.findViewById(R.id.point_spon);
            map = (Button)itemView.findViewById(R.id.point_map);
            share = (Button)itemView.findViewById(R.id.point_share);
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
                            response = URLDecoder.decode(s,"UTF-8");
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
        //Toast.makeText(context,Lang+"\n"+Lat,Toast.LENGTH_SHORT).show();
    }
}
