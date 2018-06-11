package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by fci on 16/01/18.
 */

public class Hospital_AllPosts_Adapter extends RecyclerView.Adapter<Hospital_AllPosts_Adapter.ViewHolder> {
    private List<blood_noti_item> blood_noti_it;
    private Context context;
    String hos_name;

    Hospital_AllPosts_Adapter(List<blood_noti_item> blood_noti_it, Context context) {
        this.blood_noti_it = blood_noti_it;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_posts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final blood_noti_item blood_noti = blood_noti_it.get(position);
        hos_name = blood_noti.getHosname();
        holder.tim.setText(blood_noti.getTime());
        holder.nam.setText(blood_noti.getHosname());
        holder.body.setText(blood_noti.getBody());

        holder.num_care.setText( blood_noti.getNum_care() );

        if (blood_noti.getType().equals("Blood")) {
            holder.docname.setText( blood_noti.getDocName() );
            holder.state.setText( blood_noti.getState() );
            holder.imageView.setImageResource( R.mipmap.ww );
        }else if (blood_noti.getType().equals("Money")){
            holder.imageView.setImageResource( R.mipmap.dollar );
        }else if (blood_noti.getType().equals("Other")){
            holder.imageView.setImageResource( R.mipmap.various );
        }
        holder.update.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = new LinearLayout( context );
                final EditText post = new EditText( context );
                final Spinner spin = new Spinner( context );
                final EditText doc_name = new EditText( context );
                post.setHint( "Help innocent people!" );
                post.setTextColor( R.color.colorPrimaryDark );
                post.setHintTextColor( R.color.colorPrimaryDark );
                linearLayout.setOrientation( LinearLayout.VERTICAL );
                if (blood_noti.getType().equals("Blood")){

                    final List<String> list = new ArrayList<String>();
                    list.add("Dangerous");
                    list.add("Medium");
                    list.add("For Blood bank");
                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context,
                            android.R.layout.simple_list_item_1, list);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adp1);

                    linearLayout.addView( spin );

                    doc_name.setHint( "Doctor Name" );
                    doc_name.setTextColor( R.color.colorPrimaryDark );
                    doc_name.setHintTextColor( R.color.colorPrimaryDark );
                    linearLayout.addView( doc_name );
                }

                linearLayout.addView( post );
                linearLayout.setBackgroundColor( R.color.back );



                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Update Post!")
                        .setMessage("Make it helpful!")
                        .setView( linearLayout )
                        .setIcon( R.drawable.logo_color )
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog progressDialog = new ProgressDialog( context );
                                progressDialog.setMessage( "Updating Post ... " );
                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest( Request.Method.POST, "http://momenshaheen.16mb.com/UpdatePosts.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(final String response) {

                                                progressDialog.dismiss();
                                                runOnUiThread( new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                                    }
                                                } );

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
                                        hashMap.put("date",blood_noti.getTime());
                                        hashMap.put("post",post.getText().toString());
                                        if (doc_name.getText().toString()!=null) {
                                            hashMap.put( "doc_name", doc_name.getText().toString() );
                                            hashMap.put( "state", spin.getSelectedItem().toString() );
                                        }
                                        return hashMap;
                                    }
                                };
                                Volley.newRequestQueue(context).add(stringRequest);
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
        holder.delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Confirm Message!")
                        .setMessage("Are You Sure To Delete This Post!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                final ProgressDialog progressDialog = new ProgressDialog( context );
                                progressDialog.setMessage( "Deleting Post ... " );
                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest( Request.Method.POST, "http://momenshaheen.16mb.com/DeletePost.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(final String response) {

                                                progressDialog.dismiss();
                                                runOnUiThread( new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                                                    }
                                                } );

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
                                        hashMap.put("date",blood_noti.getTime());
                                        return hashMap;
                                    }
                                };
                                Volley.newRequestQueue(context).add(stringRequest);
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

    @Override
    public int getItemCount() {
        return blood_noti_it.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tim, body, nam, docname, state;
        TextView num_care, update, delete;
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            tim = (TextView)itemView.findViewById(R.id.post_time);
            body = (TextView)itemView.findViewById(R.id.post_body);
            nam = (TextView)itemView.findViewById(R.id.post_hos_name);
            docname = (TextView)itemView.findViewById(R.id.post_doc_name);
            state = (TextView)itemView.findViewById(R.id.post_state);
            num_care = (TextView) itemView.findViewById(R.id.post_num_cares);
            update = (TextView) itemView.findViewById(R.id.post_update);
            delete = (TextView) itemView.findViewById(R.id.post_delete);
            imageView = (ImageView)itemView.findViewById( R.id.post_image );
        }
    }
}
