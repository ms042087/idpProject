package com.example.idpproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView UserName,License;
    String un, ln;
    private boolean clickBackButtonOrNot = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Show the userName and License in the header
        View header = navigationView.getHeaderView(0);
        UserName = (TextView) header.findViewById(R.id.mpUserName);
        License = (TextView) header.findViewById(R.id.mpLicense);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        un = bundle.getString("userName");
        ln = bundle.getString("licenseNumber");
        UserName.setText(un);
        License.setText(ln);

    }

    @Override
    public void onBackPressed() {

        /*if (!clickBackButtonOrNot) {
            Toast.makeText(this, "Press again to logout", Toast.LENGTH_LONG).show();
            clickBackButtonOrNot = true;
        } else {
                //super.onBackPressed();
        }
        new CountDownTimer(3000,1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                    clickBackButtonOrNot = false;

            }
        }.start();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_AboutUs) {
        // restart the activity
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            //recreate (); This doesnt work since it does not destroy the fragment

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen (int id){
        Fragment fragment = null;

        switch (id){
            case R.id.nav_Records:
                fragment = new Records();
                break;

            case R.id.nav_Reservation:
                fragment = new CarparkSelection();

                break;


        }

        if(fragment != null){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.FragmentTransaction replace = ft.replace(R.id.cotent_main, fragment);
            ft.commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.nav_logout){
            Toast.makeText(this,"Logout Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);

        }



        displaySelectedScreen(id);

        return true;
    }


}
