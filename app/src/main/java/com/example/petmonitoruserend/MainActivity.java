package com.example.petmonitoruserend;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothDevice;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.petmonitoruserend.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public float[] xlist = new float[30];
    public float[] ylist = new float[30];
    public float[] zlist = new float[30];
    public float[] tempvals = new float[30];
//    TextView tv4 = (TextView)findViewById(R.id.textview_pls);
    public float[] pulsevals = new float[30];
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
//    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_CONNECT,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setXlist(int numbytes, float[] data, int i){
        if(data[i]!=0){
            ylist[i] = data[i];
        }
    }

    public void setYlist(int numbytes, float[] data, int i){
        if(data[i]!=0){
            ylist[i] = data[i];
        }
    }
    public void setZlist(int numbytes, float[] data, int i){
        if(data[i]!=0){
            zlist[i] = data[i];
        }
    }

    public void setTempvals(int numbytes, float[] data, int i){
//            Log.d("D", String.valueOf(data[i]));
            if(data[i]!=0){
                tempvals[i] = data[i];
            }
    }

    public float[] getXvals(){
        return xlist;
    }

    public float[] getYvals(){
        return ylist;
    }

    public float[] getZvals(){
        return zlist;
    }

    public float[] gettempvals(){
        for(float f:tempvals){
//            Log.d("GetTemp", String.valueOf(f));
        }
        return tempvals;
    }






}