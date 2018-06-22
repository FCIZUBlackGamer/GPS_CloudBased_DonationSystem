package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button complain, instr, our_map;
    Intent intent;
    String get_email, get_user;
    TextView user_type, user_email;
    Database database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(Main_Home.this, NotificationService.class));
        intent = getIntent();
        get_email = intent.getStringExtra("email");
        get_user = intent.getStringExtra("user");

        database = new Database(this);
        cursor = database.ShowData();

        while (cursor.moveToNext()) {
            get_email = cursor.getString(1);
            get_user = cursor.getString(3);

        }
        complain = (Button) findViewById(R.id.complain);
        instr = (Button) findViewById(R.id.inst);
        our_map = (Button) findViewById(R.id.our_map);

//        user_type = findViewById( R.id.user_type );
//        user_email = findViewById( R.id.user_email );
//        user_email.setText( get_email +"");
//        user_type.setText( get_user +"");

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Home.this, Complain.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            }
        });
        instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Home.this, Instruction.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            }
        });
        our_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent v = new Intent(Main_Home.this, Settings_activity.class);
            v.putExtra("user", get_user);
            v.putExtra("email", get_email);
            startActivity(v);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_camera) {
            if (get_user.equals("donor")) {
                Intent intent = new Intent(Main_Home.this, Home.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Main_Home.this, Hospital_post.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            }
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Main_Home.this, Profile.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Main_Home.this, Reports.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.make_notification) {
            if (get_user.equals("hospital")) {
                Intent intent = new Intent(Main_Home.this, Make_Notification1.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            } else {
                Toast.makeText(Main_Home.this, "This Option Available For Hospital Only", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Main_Home.this, About.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Main_Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            database.UpdateData("1", get_email, "0", get_user, "0", "no", "0");
            stopService(new Intent(Main_Home.this, NotificationService.class));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
