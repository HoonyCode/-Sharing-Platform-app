package com.artifly.sharedplatform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private PopupActivity Popup_activity;

    private ArrayList<ShareRecord> records = new ArrayList<>();
    private String userId;

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Popup_activity = PopupActivity.Popup_activity;
        Popup_activity.finish();

        backButton = findViewById(R.id.leftarrow);

        ListView listView = findViewById(R.id.list_view);

        Intent intent = getIntent();
        userId = intent.getStringExtra("id");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Gson gson = new Gson();

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ServerAddress.getInstance().getServerURL() + "getUserShareRecord?ccp=share1&id="+userId);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //전송방식 데이터 읽을 때는 GET, 데이터 쓸 때는 POST
                    connection.setDoOutput(false);       //데이터를 쓸 지 설정, GET 일때는 false
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

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Place");

                    int index = 0;
                    while (index < jsonArray.length()) {
                        ShareRecord temp = gson.fromJson(jsonArray.get(index).toString(), ShareRecord.class);
                        records.add(temp);
                        index++;
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

        ItemAdapter myAdapter = new ItemAdapter();

        listView.setAdapter(myAdapter);

    }

    class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount(){
            return records.size();
        }

        @Override
        public ShareRecord getItem(int position){
            return records.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent){

            LayoutInflater mLayoutInflater = LayoutInflater.from(getApplicationContext());

            View view = mLayoutInflater.inflate(R.layout.list_item, null);

            TextView type = view.findViewById(R.id.type);
            TextView time = view.findViewById(R.id.time);
            TextView address = view.findViewById(R.id.address);

            if(records.get(position).getType().equals("start")){
                type.setText("시작");
            }else{
                type.setText("종료");
            }

            time.setText(records.get(position).getTimestamp());
            address.setText(records.get(position).getLocation());

            return view;
        }
    }
}
