package vladimir.gotravel;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MapsActivity extends Activity implements OnMapReadyCallback, View.OnClickListener {

    ImageButton btnBack;
    String sCityFrom, sCityTo;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        sPref = getSharedPreferences("GoTravel_Pref", MODE_PRIVATE);
        sCityFrom = sPref.getString("City_From", "");
        sCityTo = sPref.getString("City_To", "");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng Lvov = new LatLng(49.833333, 24);
        LatLng Rome = new LatLng(41.893056, 12.483333);
        LatLng Berlin = new LatLng(52.500556, 13.398889);
        LatLng London = new LatLng(51.507222, -0.1275);
        LatLng Paris = new LatLng(48.833333, 2.333333);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Berlin, 4));

        if(sCityFrom.equals("Lvov")) {
            map.addMarker(new MarkerOptions()
                    .title("Lvov")
                    .position(Lvov));
        }

        switch (sCityTo) {
            case "Rome":
                map.addMarker(new MarkerOptions()
                        .title("Rome")
                        .position(Rome));
                break;
            case "Berlin":
                map.addMarker(new MarkerOptions()
                        .title("Berlin")
                        .position(Berlin));
                break;
            case "London":
                map.addMarker(new MarkerOptions()
                        .title("London")
                        .position(London));
                break;
            case "Paris":
                map.addMarker(new MarkerOptions()
                        .title("Paris")
                        .position(Paris));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(this, CitiesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_prev, R.animator.out_prev);
                break;
        }
    }
}