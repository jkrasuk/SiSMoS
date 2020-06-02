package com.jk.sismos.main.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.jk.sismos.R;
import com.jk.sismos.main.service.ShakeService;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener {
    private static final String TAG = "HomeActivity";
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("earthquake")) {
                Intent i = new Intent(context, HomeAlertActivity.class);
                context.startActivity(i);
            }
        }
    };
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Intent intent = new Intent(this, ShakeService.class);
        startService(intent);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        drawerLayout.addDrawerListener(this);

        IntentFilter intentFilter = new IntentFilter("earthquake");
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                title = R.string.menu_home;
                break;
            case R.id.nav_history:
                title = R.string.menu_history;
                break;
            case R.id.nav_inpres:
                title = R.string.menu_inpres;
                break;
            case R.id.nav_exit:
                title = R.string.exit;
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }

        switch (title) {
            default:
            case R.string.menu_home:
                Fragment fragment = HomeContentFragment.newInstance(getString(R.string.menu_home));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit();
                break;
            case R.string.menu_history:
                Fragment fragmentFeltHistory = FeltHistoryContentFragment.newInstance(getString(title));
                FragmentManager fragmentFeltHistoryManager = getSupportFragmentManager();
                fragmentFeltHistoryManager.beginTransaction().replace(R.id.home_content, fragmentFeltHistory).commit();
                break;
            case R.string.menu_inpres:
                Fragment fragmentHistory = OfficialHistoryContentFragment.newInstance(getString(title));
                FragmentManager fragmentHistoryManager = getSupportFragmentManager();
                fragmentHistoryManager.beginTransaction().replace(R.id.home_content, fragmentHistory).commit();
                break;
            case R.string.exit:
                finish();
                break;
        }


        setTitle(getString(title));

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
        //cambio en la posición del drawer
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        //el drawer se ha abierto completamente

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        //el drawer se ha cerrado completamente
    }

    @Override
    public void onDrawerStateChanged(int i) {
        //cambio de estado, puede ser STATE_IDLE, STATE_DRAGGING or STATE_SETTLING
    }
}
