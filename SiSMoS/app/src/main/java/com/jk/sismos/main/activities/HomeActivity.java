package com.jk.sismos.main.activities;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
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
import com.jk.sismos.main.sensors.accelerometer.ShakeDetector;
import com.jk.sismos.main.sensors.gyroscope.RotationDetector;
import com.jk.sismos.main.sensors.light.LightDetector;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener, ShakeDetector.Listener, LightDetector.Listener, RotationDetector.Listener {
    private static final String TAG = "HomeActivity";
    private DrawerLayout drawerLayout;
    private ShakeDetector sd;
    private LightDetector ld;
    private RotationDetector rd;
    private boolean noHayRotacion;
    private boolean luzApagada;
    private boolean hayMovimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

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

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sd = new ShakeDetector(this);
        sd.start(sensorManager);

        ld = new LightDetector(this);
        ld.start(sensorManager);

        rd = new RotationDetector(this);
        rd.start(sensorManager);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            sd.stop();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                title = R.string.menu_home;
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

    @Override
    public void hearShake() {
//        Toast.makeText(this, "Movimiento detectado", Toast.LENGTH_SHORT).show();
        this.hayMovimiento = true;
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        hayMovimiento = false;
                    }
                });
            }
        }, 2000);
        verificacionSismo();
    }

    @Override
    public void senseNoLight() {
//        Toast.makeText(this, "Luz apagada detectada", Toast.LENGTH_SHORT).show();
        this.luzApagada = false;
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        luzApagada = false;
                    }
                });
            }
        }, 2000);
        verificacionSismo();
    }

    @Override
    public void noRotation() {
//        Toast.makeText(this, "Rotación detectada", Toast.LENGTH_SHORT).show();
        this.noHayRotacion = true;
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        noHayRotacion = false;
                    }
                });
            }
        }, 2000);
        verificacionSismo();
    }

    public void verificacionSismo() {
        Log.d(TAG, "noHayRotacion " + noHayRotacion + " - luzApagada " + luzApagada + " - hayMovimiento " + hayMovimiento);
        if (noHayRotacion && luzApagada && hayMovimiento) {
            final HomeContentFragment homeFragment = (HomeContentFragment) getSupportFragmentManager().findFragmentById(R.id.home_content);
            if (homeFragment != null) {
                homeFragment.setHelpText("PELIGRO");
                Timer t = new Timer(false);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Reseteo las flags
                                homeFragment.setHelpText(getString(R.string.healthCare));
                                noHayRotacion = false;
                                luzApagada = false;
                                hayMovimiento = false;
                            }
                        });
                    }
                }, 5000);
            }
        }
    }
}

