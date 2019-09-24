package com.example.tourmate;

import android.os.Bundle;

import com.example.tourmate.adapters.EventAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LoginAuth.UserAuthListener, LoginAuth.UserRegListener, RegAuth.UserLoginListener, RegAuth.UserRegListener,
        TripFragment.Addtriplistener, AddTripFragment.OnSaveSuccessfullListener, EventAdapter.OnDetailsTripListener,
        TripDetailsFragment.OneditListener, EditTripFragment.onEditSuccessListener, TripDetailsFragment.OnWalletListener,
        TripDetailsFragment.OnCameraListener, LoginAuth.AlreadyLoginListener {
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        replaceFragment(new LoginAuth(),false);





    }

    private void replaceFragment(Fragment fragment ,boolean flag){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
        if (flag == true){
            ft.addToBackStack(null);
        }
    }

    @Override
    public void onLoginSuccessfull() {
        showAppToolbar();
        replaceFragment(new TripFragment(),false);



    }

    @Override
    public void onRegSuccesfull() {
        replaceFragment(new RegAuth(),false);
    }

    @Override
    public void onlogSuccessfullsignup() {
        replaceFragment(new LoginAuth(),false);

    }

    @Override
    public void onregSuccessfullsignup() {
        replaceFragment(new LoginAuth(),false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.logout) {
            if (firebaseAuth.getCurrentUser() != null){
                firebaseAuth.signOut();
                toolbar.setVisibility(View.GONE);
                replaceFragment(new LoginAuth(),false);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_home:
                // replace code here
                return true;
            case R.id.navigation_trip:
                replaceFragment(new TripFragment(),false);
                return true;
            case R.id.nav_nearby:
                // replace code here
                return true;
            case R.id.nav_weather:
                // replace code here
                return true;
            case R.id.nav_task:
                // replace code here
                return true;
            case R.id.nav_tour_contact:
                // replace code here
                return true;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void addtrips() {
        replaceFragment(new AddTripFragment(),true);
    }

    @Override
    public void onsavelistener() {
        replaceFragment(new TripFragment(),false);
    }

    @Override
    public void onDetailslistener(String eventid) {
        Bundle bundle = new Bundle();
        bundle.putString("id",eventid);
        TripDetailsFragment tripDetailsFragment = new TripDetailsFragment();
        tripDetailsFragment.setArguments(bundle);
        replaceFragment(tripDetailsFragment,true);
    }

    @Override
    public void editlistener(String eventID) {
        Bundle bundle = new Bundle();
        bundle.putString("id",eventID);
        EditTripFragment editTripFragment = new EditTripFragment();
        editTripFragment.setArguments(bundle);
        replaceFragment(editTripFragment,true);
    }

    @Override
    public void onEditSuccess() {
        replaceFragment(new TripFragment(),true);
    }

    @Override
    public void walletlistener(String eventID) {
        Bundle bundle = new Bundle();
        bundle.putString("id",eventID);
        WalletFragment walletFragment = new WalletFragment();
        walletFragment.setArguments(bundle);
        replaceFragment(walletFragment,true);
    }

    @Override
    public void cameralistener(String eventID) {
        Bundle bundle = new Bundle();
        bundle.putString("id",eventID);
        CameraFragment cameraFragment = new CameraFragment();
        cameraFragment.setArguments(bundle);
        replaceFragment(cameraFragment,true);

    }


    @Override
    public void alreadyLogin() {
        showAppToolbar();
        replaceFragment(new TripFragment(),false);
    }

    public void showAppToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view_slide);
        navigationView.setVisibility(View.VISIBLE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setVisibility(View.VISIBLE);
    }
}
