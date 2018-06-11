package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by fci on 16/01/18.
 */

public class Blood_Adapter extends RecyclerView.Adapter<Blood_Adapter.ViewHolder> {
    private List<blood_noti_item> blood_noti_it;
    private Context context;
    String Lang, Lat, hos_name;

    int x = 0;

    Blood_Adapter(List<blood_noti_item> blood_noti_it, Context context) {
        this.blood_noti_it = blood_noti_it;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blood_nto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final blood_noti_item blood_noti = blood_noti_it.get(position);
        hos_name = blood_noti.getHosname();
        holder.tim.setText(blood_noti.getTime());
        holder.nam.setText(blood_noti.getHosname());
        holder.body.setText(blood_noti.getBody());
        holder.docname.setText(blood_noti.getDocName());
        holder.state.setText(blood_noti.getState());
        holder.don.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (x == 2)
                    x = 0;
                if (x == 0) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://momenshaheen.16mb.com/UpdateNotificationRate.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (context == null)
                                        return;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "Thanks For Caring!", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap hashMap = new HashMap();
                            hashMap.put("date", blood_noti.getTime());
                            return hashMap;
                        }
                    };
                    Volley.newRequestQueue(context).add(stringRequest);
                    x++;
                    holder.don.setBackgroundColor(context.getColor(R.color.back_post));
                } else {
                    x++;
                    holder.don.setBackgroundColor(context.getColor(R.color.colorTextPrimary));
                }

            }
        });
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hos_name = blood_noti.getHosname();
                LoadLocationData(hos_name);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = blood_noti.getHosname() + " announce that \n"
                        + blood_noti.getBody() + "\n"
                        + "Post Time: " + blood_noti.getTime();
                String Subject = "Blood Notification From Donation System";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return blood_noti_it.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tim, body, nam, docname, state;
        Button don, map, share;

        ViewHolder(View itemView) {
            super(itemView);
            tim = (TextView) itemView.findViewById(R.id.hos_tim_blood);
            body = (TextView) itemView.findViewById(R.id.hos_body_blood);
            nam = (TextView) itemView.findViewById(R.id.hos_nam_blood);
            docname = (TextView) itemView.findViewById(R.id.hos_doc_name_blood);
            state = (TextView) itemView.findViewById(R.id.hos_state_blood);
            don = (Button) itemView.findViewById(R.id.blood_donate);
            map = (Button) itemView.findViewById(R.id.blood_map);
            share = (Button) itemView.findViewById(R.id.blood_share);
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
                            String s = URLEncoder.encode(response, "ISO-8859-1");
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("location_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Lang = object.getString("Lang");
                                Lat = object.getString("Lat");
                                if (Lang != null && Lat != null) {
                                    Intent intent = new Intent(context, MapsActivity.class);
                                    intent.putExtra("Lang", Lang);
                                    intent.putExtra("name", name);
                                    intent.putExtra("Lat", Lat);
                                    context.startActivity(intent);
                                    //Toast.makeText(context, lg+"\n"+lt, Toast.LENGTH_LONG).show();
                                } else {
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
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("name", hos_name);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
