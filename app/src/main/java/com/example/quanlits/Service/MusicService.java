package com.example.quanlits.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.quanlits.R;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Nếu MediaPlayer chưa được tạo, tạo và phát nhạc
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.christmas); // Đảm bảo có tệp christmas.mp3 trong res/raw
            mediaPlayer.setLooping(true);  // Đảm bảo nhạc phát liên tục
            mediaPlayer.start();
        }
        return START_STICKY; // Đảm bảo Service tiếp tục chạy khi bị dừng
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Không cần binding trong trường hợp này
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
