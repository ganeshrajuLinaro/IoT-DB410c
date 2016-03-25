package com.example.matias.c2gpslocationtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ConnectivityTestActivity extends ActionBarActivity {

    private static final int REQUEST_LOCATION = 0;
    private static final int REQUEST_WIFI = 1;
    private static final int REQUEST_BLUETOOTH = 2;

    private TextView locationStatus;
    private TextView wifiStatus;
    private TextView bluetoothStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectivity_test);

        locationStatus = (TextView) findViewById(R.id.textViewLocation);
        wifiStatus = (TextView) findViewById(R.id.textViewWifi);
        bluetoothStatus = (TextView) findViewById(R.id.textViewBluetooth);
    }

    public void testLocation(View v) {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        NetworkTestsLocationListener listener = new NetworkTestsLocationListener();

        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Location location = getBestLocation(manager, listener);

            if (location != null) {
                locationStatus.setText(getString(R.string.location_status_result, location.getLatitude(), location.getLongitude()));
            } else {
                locationStatus.setText(R.string.location_status_unavailable);
            }
        } else {
            Intent locIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(locIntent, REQUEST_LOCATION);
        }
    }

    private Location getBestLocation(LocationManager manager, NetworkTestsLocationListener listener) {
        Location location = null;

        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        return location;
    }


    public void toggleWifi(View v) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            wifiStatus.setText(R.string.wifi_status_disabled);
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wifiManager.setWifiEnabled(true);
            wifiStatus.setText(R.string.wifi_status_enabled);

        }
    }


    public void connectWifiNetwork(View v) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiStatus.setText(R.string.wifi_enable_wifi);
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                wifiStatus.setText(getString(R.string.wifi_status_connected, wifiInfo.getSSID()));
            } else {
                wifiStatus.setText(R.string.wifi_status_disconnected);
                Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(wifiIntent, REQUEST_WIFI);
            }
        }
    }


    public void testBluetooth(View v) {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent, REQUEST_BLUETOOTH);
            if (btAdapter.isEnabled()) {
                bluetoothStatus.setText(R.string.bluetooth_status_enabled);
            } else {
                bluetoothStatus.setText(R.string.bluetooth_status_disabled);
            }
        } else {
            btAdapter.disable();
            bluetoothStatus.setText(R.string.bluetooth_status_disabled);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationStatus.setText(R.string.location_status_enabled);
                } else {
                    locationStatus.setText(R.string.try_again);
                }
                break;
            case REQUEST_WIFI:
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    wifiStatus.setText(R.string.wifi_status_enabled);
                } else {
                    wifiStatus.setText(R.string.try_again);
                }
                break;
            case REQUEST_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothStatus.setText(R.string.bluetooth_status_enabled);
                } else {
                    bluetoothStatus.setText(R.string.try_again);
                }
                break;
        }
    }


    public void resetStatuses(View v) {
        bluetoothStatus.setText(R.string.bluetooth_test);
        wifiStatus.setText(R.string.wifi_test);
        locationStatus.setText(R.string.location_test);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connectivity_test, menu);
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
}
