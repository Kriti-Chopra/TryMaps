package kriti.somevalue.com.trymaps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {

    GoogleMap map;
    LocationManager locationManager;
    List<LatLng> list=null;
    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;
    double pointY[]={105.45526,105.57364,105.53505,105.45523,105.51962,105.77320};
    double pointX[]={9.99222,9.88347,9.84184,9.77197,9.55501,9.67768,9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

        //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.INTERNET}

                        ,10);

            }

            return;
        }
        else if(ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                //map.setMyLocationEnabled(true);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //map.setMyLocationEnabled(true);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world"));

    }

    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(this, "yes!", Toast.LENGTH_SHORT).show();
        map.clear();
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Marker myMarker=map.addMarker(new MarkerOptions().position(currentLocation).title("here!"));
        myMarker.setVisible(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        addHeatMap(map);


//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(currentLocation);
//        markerOptions.title("i'm here");

        //map.addMarker(markerOptions);

       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));

        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "on ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void addHeatMap(GoogleMap map){
        list=new ArrayList<LatLng>();
        for(int i=0;i<pointX.length-1;++i){
            list.add(new LatLng(pointX[i],pointY[i]));
        }

        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }



}

