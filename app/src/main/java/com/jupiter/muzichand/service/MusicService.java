package com.jupiter.muzichand.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jupiter.muzichand.R;

public class MusicService extends Service {

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public int onStartCommand(Intent intent , int flags , int startId) {
        player = MediaPlayer.create(this , R.raw.oui);
        player.setLooping(true);
        player.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        player.stop();
    }


}
