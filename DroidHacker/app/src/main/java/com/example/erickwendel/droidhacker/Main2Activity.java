package com.example.erickwendel.droidhacker;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class Main2Activity extends AppCompatActivity {

    private WindowManager.LayoutParams layout;
    private int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        layout = getWindow().getAttributes();
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                if(value == 1) {
                    layout.screenBrightness = 0F;
                    value = 0;
                } else {
                    layout.screenBrightness = 1F;
                    value =1;
                }

                getWindow().setAttributes(layout);

                h.postDelayed(this, 1000);
            }
        }, 500);
    }
}
