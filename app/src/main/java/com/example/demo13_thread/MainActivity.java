package com.example.demo13_thread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int SWITCH_LAYOUT = 200;
    private static final int UPDATE_COUNTER = 300;
    private TextView textView;
    private static final String TAG = "THREAD_LAB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView1);
        infiniteLoop();

        //new class way
        findViewById(R.id.button1).setOnClickListener(view -> {
            Log.v(TAG,"button clicked");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                    Log.v(TAG,"time is up");
                    runOnUiThread(new Runnable() { //ui更新是靠主執行續，若沒用runOnUiThread會等同於非主執行續更新ui，會報錯
                        @Override
                        public void run() {
                            setContentView(R.layout.activity_second);//5秒後會切到此layout，但要注意，因為是在同一個activity，按下back會直接結束，而不是上一個activity
                        }
                    });

                }
            }).start();
        });

        //lambda way
        findViewById(R.id.button2).setOnClickListener(v ->
            new Thread(()->{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v(TAG,"time is up");
                runOnUiThread(()->{
                    setContentView(R.layout.activity_second);
                });
            }).start()
        );

        //handler way
        findViewById(R.id.button3).setOnClickListener(v ->
                new Thread(()->{
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.v(TAG,"time is up");
                    handler.sendEmptyMessage(SWITCH_LAYOUT);
//                    runOnUiThread(()->{
//                        setContentView(R.layout.activity_second);
//                    });
                }).start()
        );
    }

    private int counter = 0;
    private void infiniteLoop() {
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(100);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                counter++;
//                runOnUiThread(()->textView.setText(String.format("counter=%d", counter))); //normal way
                handler.sendEmptyMessage(UPDATE_COUNTER); //handler way(better
            }
        }).start();
    }

    private Handler handler = new Handler(new MyEventCallback());

    private class MyEventCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case SWITCH_LAYOUT:
                    setContentView(R.layout.activity_second);
                    return true;
                case UPDATE_COUNTER:
                    runOnUiThread(()->textView.setText(String.format("counter=%d", counter)));
                    return true;
                default:
            }
            setContentView(R.layout.activity_second);
            return true;
        }
    }
}