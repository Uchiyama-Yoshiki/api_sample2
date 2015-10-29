package com.jawbone.helloup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by yoshiki on 2015/09/28.
 */
public class UserActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        /*
            ApiManager.getRestApiInterface().getMoveEventsList(
                    UpPlatformSdkConstants.API_VERSION_STRING,
                    GetInformation.getMoveEventsListRequestParams(),
                    new Callback<Object>() {
                        @Override
                        public void success(Object o, Response response) {
                            data.setSt(GetInformation.getMoves(o));

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Toast.makeText(getApplicationContext(),
                                    retrofitError.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
            );
            */
        TextView text = (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        String data = intent.getStringExtra("keyword");
        text.setText(data);

    }

}
