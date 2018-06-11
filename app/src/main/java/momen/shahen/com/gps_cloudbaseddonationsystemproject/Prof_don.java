package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by fci on 31/01/18.
 */

public class Prof_don extends Fragment {
    //GetProfileDonData.php
    Database database;
    Cursor cursor;
    String email;
    TextView name, age, last_don, point,email_field;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new Database(getActivity());
        View view = inflater.inflate(R.layout.prfo_doner,container,false);
        name = view.findViewById(R.id.prof_don_name);
        age = view.findViewById(R.id.prof_don_age);
        last_don = view.findViewById(R.id.prof_don_last_don);
//        point = view.findViewById(R.id.prof_don_point);
        email_field = view.findViewById(R.id.prof_don_email);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        LoadData();
    }

    private void LoadData() {
        cursor = database.ShowData();
        if (cursor.getCount()==0)
        {
            Toast.makeText(getActivity(),"No Privileges to Send",Toast.LENGTH_SHORT).show();

        }
        else {
            while (cursor.moveToNext()) {
                email = cursor.getString(1);
            }
        }
        email = "momen.shaeen2020@gmail.com";
        Toast.makeText(getActivity(),email,Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest( Request.Method.POST, "http://momenshaheen.16mb.com/GetDonarData.php",
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
                            JSONArray jsonArray = jsonObject.getJSONArray("don_data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                final String emai, nam, ag, la_no;

                                        emai = object.getString("Email");
                                        nam = object.getString("name");
                                        ag = object.getString("Age");
                                        la_no = object.getString("Last_Donation");
                                        if(getActivity() == null)
                                            return;
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                name.setText( nam );
                                                email_field.setText( emai );
                                                age.setText( ag );
                                                last_don.setText( la_no );
                                            }
                                        } );

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

}
