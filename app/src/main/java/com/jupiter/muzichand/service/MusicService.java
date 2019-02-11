package com.jupiter.muzichand.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.jupiter.muzichand.MainActivity;

import java.io.IOException;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{

    public MediaPlayer mp;
    private static final String Tag = MainActivity.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();
    int CurrentPosition;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public class LocalBinder extends Binder {

        public MusicService getService(){
            Log.i(Tag , "service connected");
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i(Tag , "service created");
        Uri music = Uri.parse("android.resource://com.jupiter.muzichand/raw/oui");
        mp=new MediaPlayer();
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(getApplicationContext(), music);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IOException e) {
            Log.e(Tag, "prepare() failed");
        }

    }

    public int onStartCommand(Intent intent , int flags , int startId) {
        Log.i(Tag , "service start");

        return START_STICKY;
    }

    public void onPause()
    {
        mp.pause();
    }

    public void onPlay()
    {
        mp.start();
    }

    public int seekBarGetCurrentPosition(){    //This method is created to get SongCurrentPosition from mediaplayer for seekbar
        if(mp!=null&&mp.isPlaying()){
            CurrentPosition=mp.getCurrentPosition();
        }
        return CurrentPosition;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mp.stop();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mp.start();

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopSelf();
    }

}
