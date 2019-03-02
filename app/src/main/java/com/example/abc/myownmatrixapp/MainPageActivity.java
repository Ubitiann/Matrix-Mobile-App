package com.example.abc.myownmatrixapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    CardAdapter1 cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview1);
        cardAdapter = new CardAdapter1(MainPageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainPageActivity.this));
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.notifyDataSetChanged();
        Button button = (Button) findViewById(R.id.settingsss);
        Button button1 = (Button) findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, MainPageActivity.class);
                startActivity( intent );
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                startActivity(intent);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.documnt:

                        Intent intent = new Intent(MainPageActivity.this, details.class);
                        startActivity(intent);
                        break;

                    case R.id.myload:
                        Intent intent1 = new Intent(MainPageActivity.this, PostLoadActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent3 = new Intent(MainPageActivity.this, CurrentUserLocation.class);
                        startActivity(intent3);
                        break;
                    case R.id.paymnt:
                        Intent intent4 = new Intent(MainPageActivity.this, CurrentLocationn.class);
                        startActivity(intent4);
                        break;
                    case R.id.search:
                        Intent intent5 = new Intent(MainPageActivity.this, LoadDetail
                                .class);
                        startActivity(intent5);

                    default:
                        return false;
                }
                return  false;
            }
        });
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
        getMenuInflater().inflate(R.menu.main_page, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            // Handle the camera action
        } else if (id == R.id.post) {

        } else if (id == R.id.myload) {

        } else if (id == R.id.paymnt) {

        } else if (id == R.id.notif) {

        } else if (id == R.id.call) {

        }
        else if (id==R.id.documnt)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}