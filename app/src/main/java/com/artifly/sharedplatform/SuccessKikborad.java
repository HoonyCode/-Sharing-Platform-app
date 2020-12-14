package com.artifly.sharedplatform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SuccessKikborad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_kikborad);

        Button start = findViewById(R.id.start);
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("id");
        final String location = intent.getStringExtra("location");
        final String longitude = intent.getStringExtra("longitude");
        final String latitude = intent.getStringExtra("latitude");
        final String target = intent.getStringExtra("target");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(ServerAddress.getInstance().getServerURL() + "startShare?ccp=share1&id=" + userId + "&location=" + location + "&longitude=" + longitude + "&latitude=" + latitude + "&target=" + target);
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

                            while((result = br.readLine())!=null){
                                sb.append(result);
                            }

                            result = sb.toString();
                            Log.e("result", result);

                            // activity start
                            if(result.equals("success")){
                                Intent intent2 = new Intent(SuccessKikborad.this, RunningService.class);
                                intent2.putExtra("id", userId);
                                intent2.putExtra("target", target);

                                SharedPreferences prefs = getSharedPreferences("id", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("target", target);
                                editor.apply();

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    startForegroundService(intent2);
                                } else {
                                    startService(intent2);
                                }
                            }
                            else{
                                Log.e("adsf", "실패!");
                            }
                        } catch (Exception e) {
                            Log.e("ERROR", e.toString());
                        }
                    }
                };

                t.setDaemon(true);

                try{
                    t.start();
                    t.join();
                }
                catch (Exception e){
                    Log.e("ERRRR", e.toString());
                }

                finish();
            }
        });
    }
}