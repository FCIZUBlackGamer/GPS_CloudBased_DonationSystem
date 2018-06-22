package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Database database;
    Cursor cursor;
    Intent intent;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment newFragment;

    String get_email, get_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        database = new Database(this);
//        cursor = database.ShowData();
        intent = getIntent();

        get_email = intent.getStringExtra("email");
        get_user = intent.getStringExtra("user");
//Toast.makeText( Profile.this,"email: "+get_email+"\n"+"user: "+get_user,Toast.LENGTH_SHORT ).show();

        database = new Database(this);
        cursor = database.ShowData();

        while (cursor.moveToNext()) {
            get_email = cursor.getString(1);
            get_user = cursor.getString(3);

        }

        if (get_user.equals("donor")) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            newFragment = new Prof_don();
            transaction.replace(R.id.prof_frag, newFragment).commit();

        } else if (get_user.equals("hospital")) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            newFragment = new Prof_hosp();
            transaction.replace(R.id.prof_frag, newFragment).commit();

        } else {

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Complain.class));
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
            Intent v = new Intent(Profile.this, Settings_activity.class);
            v.putExtra("user", get_user);
            v.putExtra("email", get_email);
            startActivity(v);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Profile.this, Main_Home.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_camera) {
            if (get_user.equals("hospital")) {
                Intent intent = new Intent(Profile.this, Hospital_post.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Profile.this, Home.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            }
        } else if (id == R.id.nav_gallery) {
//            Intent intent = new Intent(Profile.this,Profile.class);
//            intent.putExtra( "email",get_email );
//            intent.putExtra( "user",get_user );
//            startActivity( intent );
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Profile.this, Reports.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.make_notification) {
            if (get_user.equals("hospital")) {
                Intent intent = new Intent(Profile.this, Make_Notification1.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            } else {
                Toast.makeText(Profile.this, "This Option Available For Hospital Only", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Profile.this, About.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Profile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            database.UpdateData("1", get_email, "0", get_user, "0", "no", "0");
            stopService(new Intent(Profile.this, NotificationService.class));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
