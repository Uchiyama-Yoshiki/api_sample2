package com.jawbone.helloup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.datamodel.Datastring;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yoshiki on 2015/10/25.
 */
public class SynchronismActivity extends Activity implements Runnable {

    private static final String TAG = SynchronismActivity.class.getSimpleName();
    private static ProgressDialog waitDialog;
    private String mClientSecret;
    private String mAccessToken;

    public static Datastring data = new Datastring();

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mClientSecret = intent.getStringExtra(UpPlatformSdkConstants.CLIENT_SECRET);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }
        setContentView(R.layout.activity_user);


        /*
        TODO ファイルの読み取り(ファイルは最新か？)
         */
        /*
        ファイルに今日の分がなければ更新
        */
        int a = 1;
        if (a == 1) {

            AAA();
        /*
        TODO 同期失敗時処理（retry　&　skip）
        */
        }
        System.out.println("XXX");

    }

    private void AAA() {
    // プログレスダイアログを開く処理を呼び出す。
        setWait();
    }

    private void setWait() {
    // プログレスダイアログの設定
        waitDialog = new ProgressDialog(this);
        // プログレスダイアログのメッセージを設定します
        waitDialog.setMessage("同期中...");
        // 円スタイル（くるくる回るタイプ）に設定します
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // プログレスダイアログを表示
        waitDialog.show();

        Thread thread = new Thread(this);
        /* show()メソッドでプログレスダイアログを表示しつつ、
        * * 別スレッドを使い、裏で重い処理を行う。
        * */
        thread.start();
    }

    @Override
    public void run() {
        try {
        //ダイアログがしっかり見えるように少しだけスリープ
        // （nnn：任意のスリープ時間・ミリ秒単位）
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
        //スレッドの割り込み処理を行った場合に発生、catchの実装は割愛
            e.printStackTrace();

        }
        //run内でUIの操作をしてしまうと、例外が発生する為、
        // Handlerにバトンタッチ

        handler.sendEmptyMessage(0);

    }
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            // HandlerクラスではActivityを継承してないため
            // 別の親クラスのメソッドにて処理を行うようにした。
             YYY();
            // プログレスダイアログ終了
        }
    };

    private void YYY() {
        // 処理待ち中に行う処理をここに実装
        apiCall();
    }
    private void apiCall(){
        //活動量取得
        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                GetInformation.getMoveEventsListRequestParams(),
                new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        System.out.println("ZZZ");
                        data.setSt(GetInformation.getMoves(o));
                        Log.d(TAG, data.getSt());

                        //睡眠状態取得
                        ApiManager.getRestApiInterface().getSleepEventsList(
                                UpPlatformSdkConstants.API_VERSION_STRING,
                                GetInformation.getMoveEventsListRequestParams(),
                                new Callback<Object>() {
                                    @Override
                                    public void success(Object o, Response response) {
                                        System.out.println("ZZZ");
                                        data.setSt(data.getSt() + GetInformation.getSleeps(o));
                                        waitDialog.dismiss();
                                        waitDialog = null;
                                        Intent intent = new Intent(SynchronismActivity.this, TopActivity.class);
                                        intent.putExtra("keyword", data.getSt());
                                        // サブ画面の呼び出し
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        dialog();
                                    }
                                }
                        );

                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getApplicationContext(),
                                retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                        dialog();

                    }
                }
        );


    }
    private void dialog(){
        new AlertDialog
                .Builder(SynchronismActivity.this)
                .setTitle("同期に失敗しました")
                .setPositiveButton("リトライ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                apiCall();
                            }
                        })
                .setNegativeButton("スキップ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Intent intent = new Intent(SynchronismActivity.this, TopActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                .show();

    }
    @Override
    protected void  onResume(){
        super.onResume();
    }
    @Override
    protected  void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
