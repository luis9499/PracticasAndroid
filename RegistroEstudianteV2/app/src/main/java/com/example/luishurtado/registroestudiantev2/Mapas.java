package com.example.luishurtado.registroestudiantev2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapas extends AppCompatActivity implements OnMapReadyCallback, /*GoogleMap.OnMapClickListener,*/ GoogleMap.OnMarkerDragListener,GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    public final static String EXTRA_LATITUD = "LATITUD";
    public final static String EXTRA_LONGITUD = "LONGITUD";
    private Marker markerString;
    private FirstMapFragment mFirstMapFragment;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        mFirstMapFragment = FirstMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map_container, mFirstMapFragment).commit();
        mFirstMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        MarkerOptions marker = new MarkerOptions();
        mMap.setMapType(googleMap.MAP_TYPE_HYBRID);

        LatLng fup = new LatLng(2.44207, -76.60818);
        markerString =mMap.addMarker(marker.position(fup).title("Coordenada donde vive").snippet("Coordena de ubicación").draggable(true));

        CameraPosition cameraPosition = CameraPosition.builder().target(fup).zoom(10).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.equals(markerString)) {
            Intent intent = new Intent(this, MarkerDetailActivity.class);
            intent.putExtra(EXTRA_LATITUD, marker.getPosition().latitude);
            intent.putExtra(EXTRA_LONGITUD, marker.getPosition().longitude);

            startActivity(intent);
        }
        return false;
    }

    /*@Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), "Coordenadas: " + latLng.toString(), Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.equals(markerString)) {
            Toast.makeText(this, "Arrastre el marcador para ubicar donde vive", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (marker.equals(markerString)) {
            String newTitle = String.format(Locale.getDefault(),
                    getString(R.string.marker_detail_latlng),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);

            setTitle(newTitle);
        }

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(markerString)) {
            Toast.makeText(this, "Coordenada seleccionada \nPresione el marcador para detalle", Toast.LENGTH_SHORT).show();
        }

    }
}
