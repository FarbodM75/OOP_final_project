package com.example.project;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    private MediaPlayer backgroundMusic;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Load the mp3 file and start playing
        backgroundMusic = MediaPlayer.create(this, R.raw.rain_audio);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        // This tells Android to keep this running until the app is closed
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the music and clear memory when the app is completely closed
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
        }
    }
}