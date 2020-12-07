package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ScanQR extends AppCompatActivity {

    private IntentIntegrator qrScan;
    ArrayList<String> boradlist = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        makelist();
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("바코드를 사각형 안에 인식시켜주세요");
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // todo
                if(boradlist.contains(result.getContents())) {
                    Intent intent = new Intent(com.example.myapplication.ScanQR.this, SuccessKikborad.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(com.example.myapplication.ScanQR.this, FaildKikborad.class);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void makelist(){
        for(int i = 1 ; i <= 10 ; i++ ){
            boradlist.add("Kikborad" + i);
        }
    }
}