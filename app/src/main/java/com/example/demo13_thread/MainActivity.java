package com.example.demo13_thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView1);
        infiniteLoop();
        findViewById(R.id.button1).setOnClickListener(view -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }

                }
            }).start();
//            setContentView(R.layout.activity_second);//5秒後會切到此layout，但要注意，因為是在同一個activity，按下back會直接結束，而不是上一個activity
        });
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
                runOnUiThread(()->textView.setText(String.format("counter=%d", counter)));
            }
        }).start();
    }
}