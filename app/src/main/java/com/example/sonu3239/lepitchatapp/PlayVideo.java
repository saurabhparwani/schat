package com.example.sonu3239.lepitchatapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class PlayVideo extends AppCompatActivity {
    private VideoView videoView;
    private String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videoView=findViewById(R.id.video);
        uri=getIntent().getExtras().getString("video");
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();

    }
}
