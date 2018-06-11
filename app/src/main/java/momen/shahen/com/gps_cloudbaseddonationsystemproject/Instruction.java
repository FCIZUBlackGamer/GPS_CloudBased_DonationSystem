package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by fci on 07/03/18.
 */

public class Instruction extends Activity {
    Intent intent;
    String get_email, get_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.instruction );
        intent = getIntent();
        get_email = intent.getStringExtra( "email" );
        get_user = intent.getStringExtra( "user" );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Instruction.this, Main_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra( "email",get_email );
        intent.putExtra( "user",get_user );
        startActivity(intent);
    }
}
