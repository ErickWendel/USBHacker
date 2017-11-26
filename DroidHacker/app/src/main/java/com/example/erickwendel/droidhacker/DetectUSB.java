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

    private AudioManager audio;
    private MediaPlayer mp;
    private Handler handler;

    @Override
    public void onReceive(Context context, Intent intent) {

        audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        Intent intent1 = new Intent("usb_detect");
        context.sendBroadcast(intent1);

        Toast.makeText(context, "USB CONECTADO", Toast.LENGTH_SHORT).show();


        audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(105500);

        Uri notification = Uri.parse("android.resource://com.example.erickwendel.droidhacker/" + R.raw.gemidao);
        mp = MediaPlayer.create(context, notification);
        mp.setLooping(true);
        mp.start();
        
        Intent i = new Intent(context, Main2Activity.class);
        context.startActivity(i);




    }


}
