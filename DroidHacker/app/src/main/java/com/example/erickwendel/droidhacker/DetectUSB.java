package com.example.erickwendel.droidhacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by erickwendel on 11/26/17.
 */

public class DetectUSB extends BroadcastReceiver {
    private String TAG = "status....";

    private MediaPlayer mp;
    private Handler handler;

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri notification = Uri.parse("android.resource://com.example.erickwendel.droidhacker/" + R.raw.gemidao);
        mp = MediaPlayer.create(context, notification);

        Intent i = new Intent(context, Main2Activity.class);
        context.startActivity(i);

        mp.setLooping(true);
        mp.start();
    }
}
