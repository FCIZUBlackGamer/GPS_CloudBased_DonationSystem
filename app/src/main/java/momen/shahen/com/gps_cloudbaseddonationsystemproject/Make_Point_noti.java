package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by fci on 17/01/18.
 */

public class Make_Point_noti extends Fragment {

    EditText cont,spon;
    Button button;
    Bundle bundle;
    Activity context;
    String url;
    String getemail;
    String emailkey, contentkey, sponserkey;
    Cursor cursor;
    Database database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.make_point_noti,container,false);
        context = getActivity();
        database = new Database(context);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        button=(Button)getActivity().findViewById(R.id.pointpost);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                cont = (EditText)context.findViewById(R.id.pointcon);
                spon = (EditText)context.findViewById(R.id.pointspon);

                cursor = database.ShowData();
                if (cursor.getCount()==0)
                {
                    Toast.makeText(getActivity(),"No Email Found",Toast.LENGTH_SHORT).show();
                }else {

                    while (cursor.moveToNext()) {
                        getemail = cursor.getString( 1 );

                        url = "http://momenshaheen.16mb.com/InsertPointNotification.php";
                        emailkey = "email";
                        sponserkey = "sponser";
                        contentkey = "content";

                        final ProgressDialog progressDialog = new ProgressDialog( context );
                        progressDialog.setMessage( "Updating Donation Period ... " );
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest( Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {

                                        progressDialog.dismiss();
                                        runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText( context, response, Toast.LENGTH_LONG ).show();
                                            }
                                        } );

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText( context, error.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap hashMap = new HashMap();
                                hashMap.put( sponserkey, spon.getText().toString() );
                                hashMap.put( emailkey, getemail );
                                hashMap.put( contentkey, cont.getText().toString() );
                                return hashMap;
                            }
                        };
                        Volley.newRequestQueue( context ).add( stringRequest );
                    }
                }
            }

        });
    }
}
