package com.example.pethfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    View rootView;
    MapView mapView;
    private MapCustomDialog mapCustomDialog;
    private GoogleMap mMap;
    List<String[]> csvData;
    List<String> storeNames;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            csvData = loadData();
            storeNames = getStoreName(csvData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        return rootView;
    }

    private void showDatabase(List<String[]> csvData) {
        new MapCSVRead(mMap, csvData);
    }

    private void getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLocation = setMarker(location);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        } else {
                            Context context = getActivity();
                            Toast.makeText(context, "위치 불러오기 불가", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Context context = getActivity();
            Toast.makeText(context, "위치 권한이 없음", Toast.LENGTH_SHORT).show();
        }
    }

    public LatLng setMarker(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocation).title("내 위치");
        changeImage(markerOptions);
        mMap.addMarker(markerOptions);
        this.mMap.setOnMarkerClickListener(markerClickListener);
        return currentLocation;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        getLastKnownLocation();
        showDatabase(csvData);
    }

    public void changeImage(MarkerOptions markerOptions) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dogmarker);
        Bitmap resultMap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resultMap));
    }

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
            for (int i = 0; i < csvData.size(); i++) {
                String[] content = csvData.get(i);
                if (storeNames.size() > i) {
                    String storeName = storeNames.get(i);//
                    if (storeName.equals(marker.getTitle())) {
                        mapCustomDialog = new MapCustomDialog(MapFragment.this.requireActivity(), content,marker.getId());
                        mapCustomDialog.show();
                        return true;
                    }
                }
            }
            return false;
        }
    };

//    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
//        @Override
//        public boolean onMarkerClick(@NonNull Marker marker) {
//            for (int i = 0; i < csvData.size(); i++) {
//                String[] content = csvData.get(i);
//
//                String storeName = content[0];
//                double storeLongitude = Double.parseDouble(content[11]);
//                double storeLatitude = Double.parseDouble(content[12]);
//
//                String markerTitle = marker.getTitle();
//                LatLng markerPosition = marker.getPosition();
//                double markerLongitude = markerPosition.longitude;
//                double markerLatitude = markerPosition.latitude;
//
//                if (storeName.equals(markerTitle) &&
//                        storeLongitude == markerLongitude &&
//                        storeLatitude == markerLatitude) {
//                    customDialog = new CustomDialog(mapFragment.this.requireActivity(), content);
//                    customDialog.show();
//                    return true;
//                }
//            }
//            return false;
//        }
//    };


    private List<String[]> loadData() throws IOException, CsvException {
        AssetManager assetManager = this.getActivity().getAssets();
        InputStream inputStream = assetManager.open("PET_POSBL_DATA.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, "utf-8"));
        return csvReader.readAll();
    }

    private List<String> getStoreName(List<String[]> csvData) {
        List<String> storeNames = new ArrayList<>();
        if (csvData != null && !csvData.isEmpty()) {
            for (String[] rowData : csvData) {
                if (rowData.length > 0) {
                    storeNames.add(rowData[0]);
                }
            }
        }
        return storeNames;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}