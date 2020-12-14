package com.artifly.sharedplatform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class RunningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        Button endButton = findViewById(R.id.end);

        final Intent intent = getIntent();

        endButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(RunningActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(final Location location) {
                                if (location != null) {
                                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                                    final String address = getCurrentAddress(loc);

                                    Thread t = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                URL url = new URL(ServerAddress.getInstance().getServerURL() + "endShare?ccp=share1&id=" + intent.getStringExtra("id") + "&location=" + address + "&longitude=" + location.getLongitude() + "&latitude=" + location.getLatitude() + "&target=" + intent.getStringExtra("target"));
                                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                                connection.setRequestMethod("POST"); //전송방식 데이터 읽을 때는 GET, 데이터 쓸 때는 POST
                                                connection.setDoOutput(true);       //데이터를 쓸 지 설정, GET 일때는 false
                                                connection.setDoInput(true);        //데이터를 읽어올지 설정

                                                int check = connection.getResponseCode();
                                                Log.d("code", check + "");

                                                InputStream is = connection.getInputStream();
                                                //InputStream is = connection.getErrorStream();
                                                StringBuilder sb = new StringBuilder();
                                                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                                                String result;

                                                while ((result = br.readLine()) != null) {
                                                    sb.append(result);
                                                }

                                                result = sb.toString();
                                                Log.e("result", result);

                                                // activity start
                                                if (result.equals("success")) {
                                                    Toast.makeText(getApplicationContext(), "정상 종료", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e("adsf", "실패!");
                                                }
                                            } catch (Exception e) {
                                                Log.e("ERROR", e.toString());
                                            }
                                        }
                                    };

                                    t.setDaemon(true);

                                    try {
                                        t.start();
                                        t.join();
                                    } catch (Exception e) {
                                        Log.e("ERRRR", e.toString());
                                    }

                                    stopService(new Intent(getApplicationContext(), RunningService.class));

                                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                    main.putExtra("userID", intent.getStringExtra("id"));
                                    main.putExtra("login", 1);
                                    startActivity(main);

                                    finish();
                                }
                            }
                        });
            }
        });

        //endButton.setText(intent.getStringExtra("target"));
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }
}
