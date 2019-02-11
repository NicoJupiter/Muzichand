package com.jupiter.muzichand;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.jupiter.muzichand.service.MusicService;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = MainActivity.class.getSimpleName();
    private MusicService musicServiceBinder = null;
    private Button buttonPause;
    private Button buttonStart;
    private SeekBar seekBar;
    boolean mBound = false;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonStart= (Button) this.findViewById(R.id.button_play);
        this.buttonPause= (Button) this.findViewById(R.id.button_pause);
        this.seekBar = (SeekBar) findViewById(R.id.seekbar_audio);

        //Make sure you update Seekbar on UI thread
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(musicServiceBinder != null){
                    int mCurrentPosition = musicServiceBinder.seekBarGetCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicServiceBinder != null && fromUser){
                    musicServiceBinder.mp.seekTo(progress * 1000);
                }
            }
        });

        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Tag , "on start");


    }

    // When user click to "Pause".
    public void doPause(View view)  {
        musicServiceBinder.onPause();
    }

    // When user click to "Pause".
    public void doPlay(View view)  {
        musicServiceBinder.onPlay();
    }





    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicServiceBinder = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };



}
