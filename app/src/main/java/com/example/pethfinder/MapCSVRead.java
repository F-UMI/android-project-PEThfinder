package com.example.pethfinder;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/*
 * 가게명 : content[0]
 * X : content[11]
 * Y : content[12]
 * 가게 주소 : content[14]
 * 전화번호 : content[16]
 */
public class MapCSVRead extends Fragment {
    public MapCSVRead(GoogleMap mMap, List<String[]> csvData) {
        loadData(mMap, csvData);
    }

    private List<String> loadData(GoogleMap mMap, List<String[]> allContent) {
        List<String> suwonMarkerData = new ArrayList<>();
        for (int i = 1; i < allContent.size(); i++) {
            String content[] = allContent.get(i);
            String fcltyNm = content[0];
            double lcLa = Double.parseDouble(content[11]);
            double lcLo = Double.parseDouble(content[12]);

            LatLng currentLocation = new LatLng(lcLa, lcLo);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation).title(fcltyNm);
            if (content[14].contains("수원시")) {
                if (mMap != null) {
                    suwonMarkerData.add(content[0]);
                    mMap.addMarker(markerOptions);
                } else {
                    Log.e("CSVRead", "mMap is null");
                }
            }
        }
        return suwonMarkerData;
    }
}

