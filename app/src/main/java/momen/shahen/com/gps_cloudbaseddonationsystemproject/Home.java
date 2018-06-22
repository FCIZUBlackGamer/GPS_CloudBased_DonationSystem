package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.AliasActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Server API Key:    AIzaSyCrZ62IpaVNlplujkymp4b3lsMeCe6Tjc4
    //Sender ID: 221676839457

    private TabLayout tabLayout;
    ViewPager viewPager=null;
    Database database;
    Cursor cursor;
    Intent intent;
    String get_email, get_user;
    TextView user_type, user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        database = new Database(this);
        intent = getIntent();
        get_user = intent.getStringExtra( "user" );
        get_email = intent.getStringExtra( "email" );

//        user_type = findViewById( R.id.user_type );
//        user_email = findViewById( R.id.user_email );
//        user_email.setText( get_email );
//        user_type.setText( get_user );

        database = new Database(this);
        cursor = database.ShowData();

        while (cursor.moveToNext()){
            get_email = cursor.getString(1);
            get_user = cursor.getString(3);

        }

        viewPager =(ViewPager)findViewById(R.id.pager);
        FragmentManager fragmentManager =getSupportFragmentManager();
        viewPager.setAdapter(new pager(fragmentManager));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Complain.class));
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
            Intent v = new Intent( Home.this,Settings_activity.class );
            v.putExtra( "user",get_user );
            v.putExtra( "email",get_email );
            startActivity( v );
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        cursor = database.ShowData();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(Home.this,Main_Home.class);
            intent.putExtra( "email",get_email );
            intent.putExtra( "user",get_user );
            startActivity( intent );
        }else if (id == R.id.nav_camera) {
//            Intent intent = new Intent(Make_Notification1.this,Home.class);
//            intent.putExtra( "email",get_email );
//            intent.putExtra( "user",get_user );
//            startActivity( intent );
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Home.this,Profile.class);
            intent.putExtra( "email",get_email );
            intent.putExtra( "user",get_user );
            startActivity( intent );
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Home.this,Reports.class);
            intent.putExtra( "email",get_email );
            intent.putExtra( "user",get_user );
            startActivity( intent );
        }else if (id == R.id.make_notification) {
            if (get_user.equals( "hospital" )) {
                Intent intent = new Intent( Home.this, Make_Notification1.class );
                intent.putExtra( "email", get_email );
                intent.putExtra( "user", get_user );
                startActivity( intent );
            }else {
                Toast.makeText( Home.this,"This Option Available For Hospital Only",Toast.LENGTH_SHORT ).show();
            }
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Home.this,About.class);
            intent.putExtra( "email",get_email );
            intent.putExtra( "user",get_user );
            startActivity( intent );
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            stopService(new Intent( Home.this, NotificationService.class ));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

class pager extends FragmentPagerAdapter
{

    public pager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position==0)
        {
            fragment = new fragBlood();
        }
        else if(position==1)
        {
            fragment = new fragMoney();
        }
        else if(position==2)
        {
            fragment = new fragOther();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
