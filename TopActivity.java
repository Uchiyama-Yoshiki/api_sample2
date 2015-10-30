package com.jawbone.helloup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Created by yoshiki on 2015/10/29.
 */
public class TopActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_top);
        TextView text = (TextView)findViewById(R.id.textView2);

        Intent intent = getIntent();
        String data = intent.getStringExtra("keyword");
        text.setText(data);
    }
}
