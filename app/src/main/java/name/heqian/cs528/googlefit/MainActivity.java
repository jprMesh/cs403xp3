package name.heqian.cs528.googlefit;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.IntentFilter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.wobs.TimeInterval;

public class MainActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener {

    public GoogleApiClient mApiClient;
    private ResponseReceiver mReceiver;

    public TextView ourText;
    public ImageView ourImage;
    public GoogleMap mMap;
    public Location mLastLocation;
    public LocationManager mLocationManager;
    public String currentActivity;
    public long startOfCurrentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        ourText = (TextView) findViewById(R.id.textView);
        ourText.setText("Initializing...");
        ourImage = (ImageView) findViewById(R.id.imageView);
        currentActivity = "still";
        startOfCurrentActivity = System.currentTimeMillis()/1000;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 1000, pendingIntent);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18));
        }
        try {
            mLocationManager = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                500, 30, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "name.heqian.cs528.googlefit.intent.action.MESSAGE_PROCESSED";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("ACTION");
            ourText.setText(action);
            switch (action) {
                case "You are in a vehicle":
                    ourImage.setImageResource(R.drawable.in_vehicle);
                    if (currentActivity != "in a vehicle") {
                        long timeDid = System.currentTimeMillis()/1000-startOfCurrentActivity;
                        Toast.makeText(getApplicationContext(), "You were "+currentActivity+" for "+timeDid+" seconds.", Toast.LENGTH_SHORT).show();
                        startOfCurrentActivity = System.currentTimeMillis()/1000;
                    }
                    currentActivity = "in a vehicle";
                    break;
                case "You are walking":
                    ourImage.setImageResource(R.drawable.walking);
                    if (currentActivity != "walking") {
                        long timeDid = System.currentTimeMillis()/1000-startOfCurrentActivity;
                        Toast.makeText(getApplicationContext(), "You were "+currentActivity+" for "+timeDid+" seconds.", Toast.LENGTH_SHORT).show();
                        startOfCurrentActivity = System.currentTimeMillis()/1000;
                    }
                    currentActivity = "walking";
                    break;
                case "You are still":
                    ourImage.setImageResource(R.drawable.still);
                    if (currentActivity != "still") {
                        long timeDid = System.currentTimeMillis()/1000-startOfCurrentActivity;
                        Toast.makeText(getApplicationContext(), "You were "+currentActivity+" for "+timeDid+" seconds.", Toast.LENGTH_SHORT).show();
                        startOfCurrentActivity = System.currentTimeMillis()/1000;
                    }
                    currentActivity = "still";
                    break;
                case "You are running":
                    ourImage.setImageResource(R.drawable.running);
                    if (currentActivity != "running") {
                        long timeDid = System.currentTimeMillis()/1000-startOfCurrentActivity;
                        Toast.makeText(getApplicationContext(), "You were "+currentActivity+" for "+timeDid+" seconds.", Toast.LENGTH_SHORT).show();
                        startOfCurrentActivity = System.currentTimeMillis()/1000;
                    }
                    currentActivity = "running";
                    break;
                default:
                    ourImage.setImageResource(R.drawable.still);
                    currentActivity = "still";
                    break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Activity", "new location baby");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
